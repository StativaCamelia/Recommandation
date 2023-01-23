package com.example.Recommendation.service;

import com.example.Recommendation.models.OAuthCredentials;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;


@Service
public class DiscogsService {

    private static final String TOKEN_URL = "https://api.discogs.com/oauth/request_token";
    public static final String SECRET = "nSMWxGCXJOgvXuHCOjXtKvDsACsZLQTx";
    public static final String KEY = "ExSRYFyYoKQkcFRzaUfX";
    static final String AUTHENTICATION_URL = "https://www.discogs.com/de/oauth/authorize";
    static final String ACCESS_TOKEN_URL = "https://api.discogs.com/oauth/access_token";

    private HttpClient client;

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
                .append("oauth_timestamp=").append(System.currentTimeMillis() / 1000);
        return b.toString();

    }
}
