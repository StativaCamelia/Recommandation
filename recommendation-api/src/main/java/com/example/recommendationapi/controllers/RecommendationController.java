package com.example.recommendationapi.controllers;

import com.example.recommendationapi.models.UserPreferences;
import com.example.recommendationapi.services.SparqlService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/recommendation")
public class RecommendationController {

    private final SparqlService sparqlService;

    @Inject
    public RecommendationController(SparqlService sparqlService) {
        this.sparqlService = sparqlService;
    }

    @PostMapping("/preferences")
    public Map<String, Map<String, List<Object>>> postBody(@RequestBody UserPreferences userPreferences) {
        Response response = sparqlService.GetRecommendationWithSparql(userPreferences);
        return response.readEntity(new GenericType<Map<String, Map<String, List<Object>>>>() {});
    }
}
