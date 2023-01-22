package com.example.recommendationapi.controllers;

import com.example.recommendationapi.models.Result;
import com.example.recommendationapi.models.SparqlResponse;
import com.example.recommendationapi.models.TopVinylGenre;
import com.example.recommendationapi.models.UserPreferences;
import com.example.recommendationapi.services.SparqlService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
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
    public Result getRecommendedPreferences(@RequestBody UserPreferences userPreferences) {
        SparqlResponse sparqlResponse = sparqlService.GetRecommendationWithSparql(userPreferences);
        return sparqlResponse.GetResult(userPreferences.recommendationLimit);
    }

    @PostMapping("/genres")
    public Result getTopGenre(@RequestBody TopVinylGenre topVinylGenre) {
        SparqlResponse sparqlResponse = sparqlService.GetTopGenres();
        return sparqlResponse.GetResult(topVinylGenre.top);
    }
}
