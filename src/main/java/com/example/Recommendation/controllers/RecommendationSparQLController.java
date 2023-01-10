package com.example.Recommendation.controllers;

import com.example.Recommendation.models.SparQLQuery;
import com.example.Recommendation.service.RecommendationStardogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

@RestController
@RequestMapping("/recommendationSparQL")
public class RecommendationSparQLController {

    private final RecommendationStardogService recommendationStardogService;

    @Inject
    public RecommendationSparQLController(RecommendationStardogService recommendationStardogService) {
        this.recommendationStardogService = recommendationStardogService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<String> testGET() {

        return new ResponseEntity<>("Test", HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<String> selectData(final HttpServletRequest request, final @RequestBody SparQLQuery query) {

        Client client = ClientBuilder.newClient();
        Form form = new Form().param("query", query.getQuery()).param("reasoning", "true");
        Entity<Form> payload = Entity.form(form);

        Response response = client.target("https://sd-28c1a0f2.stardog.cloud:5820/vinyl/query")
                .request(MediaType.TEXT_PLAIN)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED)
                .header(HttpHeaders.AUTHORIZATION, "Basic c3RhdGl2YTUwQGdtYWlsLmNvbTozVFFaNDRnUXNyZ0tUcjY=")
                .header(HttpHeaders.ACCEPT, "application/sparql-results+json")
                .header(HttpHeaders.CACHE_CONTROL, "no-cache")
                .post(payload);

        HttpStatus status = Optional
                .of(HttpStatus.valueOf(response.getStatus()))
                .orElse(HttpStatus.SERVICE_UNAVAILABLE);

        return new ResponseEntity<>(response.readEntity(String.class), status);
    }
}
