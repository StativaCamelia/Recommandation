package com.example.recommendationapi.models;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class SparqlQuery {
    public String query;

    public SparqlQuery(String query) {
        this.query = query;
    }

    public SparqlResponse SendRequest() {
        Client client = ClientBuilder.newBuilder().build();
        String sparqlEndpoint = "https://recommandationapi-374817.ew.r.appspot.com/recommendationSparQL";

        Response response = client.target(sparqlEndpoint)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(this), Response.class);

        return response.readEntity(SparqlResponse.class);
    }
}
