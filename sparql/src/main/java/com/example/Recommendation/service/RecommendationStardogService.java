package com.example.Recommendation.service;

import com.example.Recommendation.models.SparQLQuery;
import com.example.Recommendation.repo.RecommendationStardogRepo;
import com.stardog.stark.query.SelectQueryResult;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service
public class RecommendationStardogService {

    private final RecommendationStardogRepo recommendationStardogRepo;

    @Inject
    public RecommendationStardogService(RecommendationStardogRepo recommendationStardogRepo) {
        this.recommendationStardogRepo = recommendationStardogRepo;
    }


    public SelectQueryResult getRecommendation(SparQLQuery query) {

        return recommendationStardogRepo.getRecommendation(query);
    }
}
