package com.example.recommendationapi.services;

import com.example.recommendationapi.models.SparqlQuery;
import com.example.recommendationapi.models.SparqlResponse;
import com.example.recommendationapi.models.UserPreferences;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Service
public class SparqlService {

    private final SparqlQueryBuilder queryBuilder;
    private final String sparqlEndpoint = "https://recommandationapi-374817.ew.r.appspot.com/recommendationSparQL";

    @Inject
    public SparqlService(SparqlQueryBuilder queryBuilder) {
        this.queryBuilder = queryBuilder;
    }

    public SparqlResponse GetRecommendationWithSparql(UserPreferences userPreferences) {
        SparqlQuery query = queryBuilder.CreateQuery(userPreferences);
        Client client = ClientBuilder.newBuilder().build();
        Response response = client.target(sparqlEndpoint)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(query), Response.class);
        return response.readEntity(SparqlResponse.class);
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

        Client client = ClientBuilder.newBuilder().build();
        Response response = client.target(sparqlEndpoint)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(query), Response.class);
        return response.readEntity(SparqlResponse.class);
    }
}
