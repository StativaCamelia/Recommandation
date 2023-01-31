package com.example.recommendationapi.controllers;

import com.example.recommendationapi.models.OAuthDiscogsCredentials;
import com.example.recommendationapi.services.DiscogsOauthService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

@RestController
@RequestMapping("/discogs")
@CrossOrigin
public class DiscogsController {


    private final DiscogsOauthService discogsOauthService;

    @Inject
    public DiscogsController(DiscogsOauthService discogsOauthService) {

        this.discogsOauthService = discogsOauthService;
    }

    @GetMapping(value = "request_token")
    public ResponseEntity<OAuthDiscogsCredentials> getDiscogsRequestToken() throws Exception {

        OAuthDiscogsCredentials authenticationUrl = discogsOauthService.getAuthenticationUrl();
        if(authenticationUrl != null) {
            return new ResponseEntity<>(authenticationUrl, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(null, HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @GetMapping(value = "access_token")
    public ResponseEntity<OAuthDiscogsCredentials> getDiscogsAccessToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth, @RequestParam("verifier") String verifier) throws Exception {

        OAuthDiscogsCredentials token = discogsOauthService.getAccessToken(verifier, auth);
        if (token != null) {
            return new ResponseEntity<>(token, HttpStatus.OK);
        } else {

            return new ResponseEntity<>(null, HttpStatus.SERVICE_UNAVAILABLE);
        }
    }


}
