package com.example.recommendationapi.services;

import com.example.recommendationapi.models.SparqlQuery;
import com.example.recommendationapi.models.UserPreferences;
import org.springframework.stereotype.Service;

@Service
public class SparqlQueryBuilder {

    String query = "";

    public static SparqlQuery CreateQuery(UserPreferences userPreferences) {
        String query = "PREFIX res: <http://www.w3.org/2005/sparql-results#> PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> SELECT ?genre (COUNT(?vinyl) AS ?recordsCount) WHERE {  ?result a res:ResultSet .  ?result res:solution ?solution .   ?solution res:binding [ res:variable \"vinyl\" ; res:value ?vinyl ] .  ?solution res:binding [ res:variable \"genre\" ; res:value ?genre ] . } GROUP BY ?genre ORDER BY desc(?recordsCount)";
        return new SparqlQuery(query);
    }

    public SparqlQueryBuilder AddPrefix(String prefixName, String prefixNamespace) {
        this.query += "PREFIX " + prefixName + ": <" + prefixNamespace + "#>\n";
        return this;
    }

    public SparqlQueryBuilder AddSparqlResultPrefix() {
        this.query += "PREFIX res: <http://www.w3.org/2005/sparql-results#>";
        return this;
    }

    public SparqlQueryBuilder AddRdfPrefix() {
        this.query += "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>";
        return this;
    }

    public SparqlQueryBuilder AddXsdPrefix() {
        this.query += "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>";
        return this;
    }

    public SparqlQueryBuilder AddSelect() {
        this.query += "\nSELECT ";
        return this;
    }

    public SparqlQueryBuilder AddSelectDistinct() {
        this.query += "\nSELECT DISTINCT ";
        return this;
    }

    public SparqlQueryBuilder AddSelectedField(String field) {
        this.query += "?" + field + " ";
        return this;
    }

    public SparqlQueryBuilder AddSelectedCountField(String field, String countAlias) {
        this.query += "(COUNT(?" + field + ") AS ?" + countAlias + ") ";
        return this;
    }

    public SparqlQueryBuilder AddWhere() {
        this.query += "\nWHERE {\n";
        this.query += "?result a res:ResultSet .\n";
        this.query += "?result res:solution ?solution .\n";
        return this;
    }

    public SparqlQueryBuilder AddBindingVariableInWhere(String field) {
        this.query += "?solution res:binding [ res:variable \"" + field + "\" ; res:value ?" + field + " ] .\n";
        return this;
    }

    public SparqlQueryBuilder AddFilterInWhere() {
        this.query += "\nFILTER (";
        return this;
    }

    public SparqlQueryBuilder AddContainsStringInFilter(String field, String containedValue) {
        this.query += "contains(?" + field + ", \"" + containedValue + "\") ";
        return this;
    }

    public SparqlQueryBuilder AddDateConditionInFilter(String field, String operator, String date) {
        this.query += "?" + field + " " + operator + " xsd:dateTime('" + date + "')";
        return this;
    }

    public SparqlQueryBuilder AddORInFilter() {
        this.query += "||\n";
        return this;
    }

    public SparqlQueryBuilder AddANDInFilter() {
        this.query += "&&\n";
        return this;
    }

    public SparqlQueryBuilder CloseFilter(){
        this.query += ")\n";
        return this;
    }

    public SparqlQueryBuilder CloseWhere() {
        this.query += "}";
        return this;
    }

    public SparqlQueryBuilder AddGroupBy(String field) {
        this.query += "\nGROUP BY ?" + field;
        return this;
    }

    public SparqlQueryBuilder AddOrderBy(String field, boolean asc) {
        String direction = asc ? "asc" : "desc";
        this.query += "\nORDER BY " + direction + "(?" + field + ")";
        return this;
    }

    public SparqlQuery Build(){
        return new SparqlQuery(this.query);
    }
}
