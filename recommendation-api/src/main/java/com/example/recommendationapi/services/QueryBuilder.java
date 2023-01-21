package com.example.recommendationapi.services;

import com.example.recommendationapi.models.SparqlQuery;
import com.example.recommendationapi.models.UserPreferences;
import org.springframework.stereotype.Service;

@Service
public class QueryBuilder {

    public SparqlQuery CreateQuery(UserPreferences userPreferences){
        String query = "PREFIX res: <http://www.w3.org/2005/sparql-results#> PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> SELECT ?genre (COUNT(?vinyl) AS ?recordsCount) WHERE {  ?result a res:ResultSet .  ?result res:solution ?solution .   ?solution res:binding [ res:variable \"vinyl\" ; res:value ?vinyl ] .  ?solution res:binding [ res:variable \"genre\" ; res:value ?genre ] . } GROUP BY ?genre ORDER BY desc(?recordsCount)";
        return new SparqlQuery(query);
    }
}
