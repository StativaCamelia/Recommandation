package com.example.recommendationapi.discogs.model;

public class OAuthCredentials {

    String verifierCode;

    String authorizationUrl;

    public OAuthCredentials() { }


    public String getAuthorizationUrl() {
        return this.authorizationUrl;
    }

    public void setAuthorizationUrl(String authorizationUrl) {
        this.authorizationUrl = authorizationUrl;
    }

}