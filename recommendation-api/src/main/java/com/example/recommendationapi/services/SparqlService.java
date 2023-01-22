package com.example.recommendationapi.services;

import com.example.recommendationapi.models.SparqlQuery;
import com.example.recommendationapi.models.SparqlResponse;
import com.example.recommendationapi.models.UserPreferences;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service
public class SparqlService {

    private final SparqlQueryBuilder queryBuilder;

    @Inject
    public SparqlService(SparqlQueryBuilder queryBuilder) {
        this.queryBuilder = queryBuilder;
    }

    public SparqlResponse GetRecommendationWithSparql(UserPreferences userPreferences) {
        SparqlQuery query = queryBuilder.CreateQuery(userPreferences);
        return query.SendRequest();
    }

    public SparqlResponse GetTopGenres() {
        SparqlQuery query = new SparqlQueryBuilder()
                .AddRdfPrefix()
                .AddSparqlResultPrefix()
                .AddXsdPrefix()
                .AddSelect()
                .AddSelectedField("genre")
                .AddSelectedCountField("vinyl", "recordsCount")
                .AddWhere()
                .AddBindingVariableInWhere("vinyl")
                .AddBindingVariableInWhere("genre")
                .CloseWhere()
                .AddGroupBy("genre")
                .AddOrderBy("recordsCount", false)
                .Build();

        return query.SendRequest();
    }
}
