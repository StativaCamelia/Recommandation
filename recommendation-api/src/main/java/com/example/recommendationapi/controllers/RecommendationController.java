package com.example.recommendationapi.controllers;

import com.example.recommendationapi.models.Result;
import com.example.recommendationapi.models.SparqlResponse;
import com.example.recommendationapi.models.TopVinylByCount;
import com.example.recommendationapi.models.UserPreferences;
import com.example.recommendationapi.services.SparqlService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

@RestController
@RequestMapping("/recommendation")
public class RecommendationController {

    private final SparqlService sparqlService;

    @Inject
    public RecommendationController(SparqlService sparqlService) {
        this.sparqlService = sparqlService;
    }

    @PostMapping("/preferences")
    public Result getRecommendedPreferences(@RequestBody UserPreferences userPreferences) {
        SparqlResponse sparqlResponse = sparqlService.GetRecommendationByPreferences(userPreferences);
        return sparqlResponse.GetResult(userPreferences.recommendationLimit);
    }

    @PostMapping("/count")
    public Result getTopByCount(@RequestBody TopVinylByCount topVinylByCount) {
        SparqlResponse sparqlResponse = sparqlService.GetTopByCount(topVinylByCount.field);
        return sparqlResponse.GetResult(topVinylByCount.top);
    }
}
