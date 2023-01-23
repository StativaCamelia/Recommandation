package com.example.recommendationapi.discogs.service;

import com.example.recommendationapi.discogs.model.OAuthCredentials;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.UUID;


@Service
public class DiscogsService {

    public static final String SECRET = "nSMWxGCXJOgvXuHCOjXtKvDsACsZLQTx";
    public static final String KEY = "ExSRYFyYoKQkcFRzaUfX";
    static final String AUTHENTICATION_URL = "https://www.discogs.com/oauth/authorize";
    static final String ACCESS_TOKEN_URL = "https://api.discogs.com/oauth/access_token";

    private static final String TOKEN_URL = "https://api.discogs.com/oauth/request_token";
    private HttpClient client;
    private String TOKEN;
    private String TOKEN_SECRET;

    public OAuthCredentials getAuthenticationUrl() throws Exception {

        HttpGet getTempToken = new HttpGet(TOKEN_URL);
        getTempToken.setHeader(HttpHeaders.AUTHORIZATION, getTempTokenHeader());

        try {
            HttpResponse response = HttpClients.createDefault().execute(getTempToken);
            HttpEntity entity = response.getEntity();
            String body = readInputStream(entity.getContent());
            OAuthCredentials cb = new OAuthCredentials();
            return parseTempTokenBody(cb, body);
        } catch (Exception ex) {
            throw ex;
        }
    }

    private OAuthCredentials parseTempTokenBody(OAuthCredentials bean, String body) {

        String[] parts = body.split("&");
        System.out.println(Arrays.toString(parts));
        TOKEN = parts[0].substring(parts[0].indexOf("=") + 1);
        TOKEN_SECRET = parts[1].substring(parts[1].indexOf("=") + 1);
        bean.setAuthorizationUrl(AUTHENTICATION_URL + "?" + parts[0]);

        return bean;
    }

    private String readInputStream(InputStream inStream) throws IOException {

        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;

        while ((length = inStream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }

        return result.toString("UTF-8");
    }


    private String getTempTokenHeader() {
        StringBuilder b = new StringBuilder("OAuth ")
                .append("oauth_consumer_key=\"").append(KEY).append("\", ")
                .append("oauth_nonce=\"").append(UUID.randomUUID().toString()).append("\", ")
                .append("oauth_signature=\"").append(SECRET).append("&\", ")
                .append("oauth_signature_method=\"").append("PLAINTEXT").append("\", ")
                .append("oauth_timestamp=").append(System.currentTimeMillis() / 1000).append("\", ")
                .append("oauth_callback=").append("\"http://localhost:3000/profile\"");
        return b.toString();
    }

    public OAuthCredentials getAccessToken(String verifierCode) throws Exception {

        HttpPost accessRequest = new HttpPost(ACCESS_TOKEN_URL);
        System.out.println(getAccessTokenHeader(verifierCode));
        accessRequest.setHeader(HttpHeaders.AUTHORIZATION, getAccessTokenHeader(verifierCode));

        try {
            HttpResponse response = HttpClients.createDefault().execute(accessRequest);
            String body = readInputStream(response.getEntity().getContent());

            //TODO: Store the tokens in database
            System.out.println(body);
            return parseAccessTokenBody(body);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw new Exception(ex.getMessage());
        }
    }


    private OAuthCredentials parseAccessTokenBody(String body) {

        String[] parts = body.split("&");
        System.out.println(parts);
        return null;
    }

    private String getAccessTokenHeader(String verifier) {

        StringBuilder b = new StringBuilder("OAuth ")
                .append("oauth_consumer_key=\"").append(KEY).append("\", ")
                .append("oauth_nonce=\"").append(UUID.randomUUID().toString()).append("\", ")
                .append("oauth_token=\"").append(TOKEN).append("\", ")
                .append("oauth_signature=\"").append(SECRET).append("&").append(TOKEN_SECRET).append("\", ")
                .append("oauth_signature_method=\"").append("PLAINTEXT").append("\", ")
                .append("oauth_timestamp=\"").append(System.currentTimeMillis() / 1000).append("\", ")
                .append("oauth_verifier=\"").append(verifier).append("\"");
        System.out.println(b);
        return b.toString();
    }
}
