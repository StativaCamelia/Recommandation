package com.example.recommendationapi.discogs.controllers;

import com.example.recommendationapi.discogs.model.OAuthCredentials;
import com.example.recommendationapi.discogs.service.DiscogsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

@RestController
@RequestMapping("/discogs")
public class DiscogsController {


    private final DiscogsService discogsService;

    @Inject
    public DiscogsController(DiscogsService discogsService) {

        this.discogsService = discogsService;
    }

    @GetMapping(value = "request_token")
    public ResponseEntity<OAuthCredentials> getDiscogsRequestToken() throws Exception {

        return new ResponseEntity<>(discogsService.getAuthenticationUrl(), HttpStatus.OK);
    }

    @GetMapping(value = "access_token")
    public ResponseEntity<OAuthCredentials> getDiscogsAccessToken(@RequestParam("oauth_verifier") String verifier) throws Exception {

        return new ResponseEntity<>(discogsService.getAccessToken(verifier), HttpStatus.OK);
    }
}
