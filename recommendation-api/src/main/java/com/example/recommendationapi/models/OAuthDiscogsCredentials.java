package com.example.recommendationapi.models;

public class OAuthDiscogsCredentials {

    private String verifierCode;

    private String authorizationUrl;

    private String userToken;

    private String userTokenSecret;

    public OAuthDiscogsCredentials() { }


    public String getAuthorizationUrl() {
        return this.authorizationUrl;
    }

    public void setAuthorizationUrl(String authorizationUrl) {
        this.authorizationUrl = authorizationUrl;
    }

    public String getVerifierCode() {
        return verifierCode;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public void setUserTokenSecret(String userTokenSecret) {
        this.userTokenSecret = userTokenSecret;
    }

    public String getUserTokenSecret() {
        return userTokenSecret;
    }
}