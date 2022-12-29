package com.example.Recommendation.controllers;

import com.example.Recommendation.models.SparQLQuery;
import com.example.Recommendation.service.RecommendationStardogService;
import com.stardog.stark.query.SelectQueryResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

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
    public ResponseEntity<SelectQueryResult> selectData(final @RequestBody SparQLQuery query){

        SelectQueryResult tupleQueryResult = recommendationStardogService.getRecommendation(query);

        System.out.println(tupleQueryResult);
        return new ResponseEntity<>(tupleQueryResult, HttpStatus.OK);
    }
}
