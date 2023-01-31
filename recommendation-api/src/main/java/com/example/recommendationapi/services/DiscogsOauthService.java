package com.example.recommendationapi.services;

import com.example.recommendationapi.models.OAuthDiscogsCredentials;
import com.example.recommendationapi.services.utils.DiscogsCallsUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.UUID;


@Service
public class DiscogsOauthService {

    private static final String SECRET = "nSMWxGCXJOgvXuHCOjXtKvDsACsZLQTx";
    private static final String KEY = "ExSRYFyYoKQkcFRzaUfX";
    private static final String AUTHENTICATION_URL = "https://www.discogs.com/oauth/authorize";
    private static final String ACCESS_TOKEN_URL = "https://api.discogs.com/oauth/access_token";

    private static final String TOKEN_URL = "https://api.discogs.com/oauth/request_token";
    private static final String USER_API_URL = "https://user-microservice-wade.herokuapp.com/edit";

    private String TOKEN;
    private String TOKEN_SECRET;

    private OAuthDiscogsCredentials bean;

    public OAuthDiscogsCredentials getAuthenticationUrl() throws Exception {

        HttpGet getTempToken = new HttpGet(TOKEN_URL);
        getTempToken.setHeader(HttpHeaders.AUTHORIZATION, getTempTokenHeader());

        HttpResponse response = HttpClients.createDefault().execute(getTempToken);
        HttpEntity entity = response.getEntity();
        String body = DiscogsCallsUtils.readInputStream(entity.getContent());
        bean = new OAuthDiscogsCredentials();
        return parseTempTokenBody(bean, body);
    }

    private OAuthDiscogsCredentials parseTempTokenBody(OAuthDiscogsCredentials bean, String body) {

        String[] parts = body.split("&");
        System.out.println("User Token Body" + body);
        TOKEN = parts[0].substring(parts[0].indexOf("=") + 1);
        TOKEN_SECRET = parts[1].substring(parts[1].indexOf("=") + 1);
        System.out.println(TOKEN);
        System.out.println(TOKEN_SECRET);
        bean.setAuthorizationUrl(AUTHENTICATION_URL + "?" + parts[0]);

        return bean;
    }


    private String getTempTokenHeader() {
        return "OAuth " +
                "oauth_consumer_key=\"" + KEY + "\", " +
                "oauth_nonce=\"" + UUID.randomUUID() + "\", " +
                "oauth_signature=\"" + SECRET + "&\", " +
                "oauth_signature_method=\"" + "PLAINTEXT" + "\", " +
                "oauth_timestamp=" + System.currentTimeMillis() / 1000 + "\", " +
                "oauth_callback=" + "\"http://localhost:3000/profile\"";
    }

    public OAuthDiscogsCredentials getAccessToken(String verifierCode, String auth) throws Exception {

        HttpPost accessRequest = new HttpPost(ACCESS_TOKEN_URL);
        accessRequest.setHeader(HttpHeaders.AUTHORIZATION, getAccessTokenHeader(verifierCode));

        System.out.println("Access Body");
        System.out.println(getAccessTokenHeader(verifierCode));
        try {
            HttpResponse response = HttpClients.createDefault().execute(accessRequest);
            if (response.getStatusLine().getStatusCode() == 200) {

                String body = DiscogsCallsUtils.readInputStream(response.getEntity().getContent());

                return parseAccessTokenBody(this.bean, body, auth);
            } else {
                System.out.println("Access_Token" + response.getStatusLine());
                return null;
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw new Exception(ex.getMessage());
        }
    }


    private OAuthDiscogsCredentials parseAccessTokenBody(OAuthDiscogsCredentials bean, String body, String auth) throws IOException {

        String[] parts = body.split("&");

        bean.setUserTokenSecret(parts[1].substring(parts[1].lastIndexOf("=") + 1));
        bean.setUserToken(parts[0].substring(parts[0].lastIndexOf("=") + 1));

        saveTokenData(bean, auth);

        return bean;
    }

    private void saveTokenData(OAuthDiscogsCredentials bean, String auth) throws IOException {
        HttpPost userRequest = getHttpPost(bean, auth);

        HttpResponse response = HttpClients.createDefault().execute(userRequest);

        if (response.getStatusLine().getStatusCode() != 200)
            System.out.println("Profile" + response.getStatusLine());
    }

    private HttpPost getHttpPost(OAuthDiscogsCredentials bean, String auth) throws UnsupportedEncodingException {
        HttpPost userRequest = new HttpPost(USER_API_URL);
        userRequest.setHeader(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", auth));
        userRequest.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        JSONObject json = getBody(bean);
        StringEntity params = new StringEntity(json.toString());
        userRequest.setEntity(params);
        return userRequest;
    }

    private JSONObject getBody(OAuthDiscogsCredentials bean) {
        JSONObject json = new JSONObject();
        json.put("discogs_token", bean.getUserToken());
        json.put("discogs_secret", bean.getUserTokenSecret());
        return json;
    }

    private String getAccessTokenHeader(String verifier) {

        return "OAuth " +
                "oauth_consumer_key=\"" + KEY + "\", " +
                "oauth_nonce=\"" + UUID.randomUUID() + "\", " +
                "oauth_token=\"" + TOKEN + "\", " +
                "oauth_signature=\"" + SECRET + "&" + TOKEN_SECRET + "\", " +
                "oauth_signature_method=\"" + "PLAINTEXT" + "\", " +
                "oauth_timestamp=\"" + System.currentTimeMillis() / 1000 + "\", " +
                "oauth_verifier=\"" + verifier + "\"";
    }


}
