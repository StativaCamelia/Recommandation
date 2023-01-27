package com.example.recommendationapi.controllers;

import com.example.recommendationapi.models.Result;
import com.example.recommendationapi.models.SparqlResponse;
import com.example.recommendationapi.models.TopVinylByCount;
import com.example.recommendationapi.models.UserPreferences;
import com.example.recommendationapi.services.SparqlService;
import com.example.recommendationapi.services.exceptions.UserHasNoData;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.inject.Inject;
import java.io.IOException;

@RestController
@RequestMapping("/recommendation")
@CrossOrigin
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

    @PostMapping("/discogs")
    public Result getPreferencesFromDiscogsByUserId(@RequestBody String userId) {
        SparqlResponse sparqlResponse = sparqlService.getRecommendationFromDiscogsByUser(userId);
        if (sparqlResponse.isError) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "The request query is incorrect" + sparqlResponse.message);
        }
        return null;
    }

    @GetMapping("/discogs")
    public Result getPreferencesFromDiscogsByDiscogsToken(@RequestParam("discogs_token") String discogsToken,
                                                          @RequestParam("discogs_token_secret") String discogsTokenSecret, @RequestParam("page_index") Integer pageIndex, @RequestParam("page_size") Integer pageSize) throws IOException, UserHasNoData {

        final SparqlResponse sparqlResponse = sparqlService.getRecommendationFromDiscogsByToken(discogsToken, discogsTokenSecret);

        if (sparqlResponse.isError) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "The request query is incorrect" + sparqlResponse.message);
        }
        return sparqlResponse.GetResult(pageIndex, pageSize);
    }

}
