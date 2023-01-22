package com.example.recommendationapi.controllers;

import com.example.recommendationapi.models.Result;
import com.example.recommendationapi.models.SparqlResponse;
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
    public Result postBody(@RequestBody UserPreferences userPreferences) {
        SparqlResponse sparqlResponse = sparqlService.GetRecommendationWithSparql(userPreferences);
        List<Map<String, Map<String, String>>> bindings = sparqlResponse.results.get("bindings");

        List<Map<String, String>> finalList = new ArrayList<>();

        for (Map<String, Map<String, String>> entity : bindings)
        {
            if (finalList.size() == userPreferences.recommendationLimit &&
                    userPreferences.recommendationLimit != 0)
            {
                break;
            }
            Map<String, String> finalValue = new HashMap<>();
            entity.forEach((key, value) -> {
                finalValue.put(key, value.get("value"));
            });
            finalList.add(finalValue);
        }

        Result result = new Result();
        result.variables = sparqlResponse.head.get("vars");
        result.results = finalList;

        return result;
    }
}
