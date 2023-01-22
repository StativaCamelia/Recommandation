package com.example.recommendationapi.controllers;

import com.example.recommendationapi.models.Result;
import com.example.recommendationapi.models.SparqlResponse;
import com.example.recommendationapi.models.TopVinylByCount;
import com.example.recommendationapi.models.UserPreferences;
import com.example.recommendationapi.services.SparqlService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

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
        if (sparqlResponse.isError) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "The request query is incorrect" + sparqlResponse.message);
        }
        return sparqlResponse.GetResult(userPreferences.pageIndex, userPreferences.pageSize);
    }

    @PostMapping("/count")
    public Result getTopByCount(@RequestBody TopVinylByCount topVinylByCount) {
        SparqlResponse sparqlResponse = sparqlService.GetTopByCount(topVinylByCount.field, topVinylByCount.limit);
        return sparqlResponse.GetResult(topVinylByCount.pageIndex, topVinylByCount.pageSize);
    }
}
