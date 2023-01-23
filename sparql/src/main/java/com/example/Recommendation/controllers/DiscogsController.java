package com.example.Recommendation.controllers;

import com.example.Recommendation.models.OAuthCredentials;
import com.example.Recommendation.service.DiscogsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

@RestController
@RequestMapping("/disco")
public class DiscogsController {


    private final DiscogsService discogsService;

    @Inject
    public DiscogsController(DiscogsService discogsService) {

        this.discogsService = discogsService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<OAuthCredentials> getDiscogsRequestToken() throws Exception {

        return new ResponseEntity<>(discogsService.getAuthenticationUrl(), HttpStatus.OK);
    }
}
