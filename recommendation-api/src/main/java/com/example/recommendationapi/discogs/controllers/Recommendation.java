package com.example.recommendationapi.discogs.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/recommendationSparQL")
public class Recommendation {

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<String> testGET() {

        return new ResponseEntity<>("Test", HttpStatus.OK);
    }
}
