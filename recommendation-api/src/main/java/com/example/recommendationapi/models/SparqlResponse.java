package com.example.recommendationapi.models;

import java.util.ArrayList;
import java.util.List;

public class SparqlResponse {
    public Head head;
    public Results results;

    public class Head {
        public List<String> vars = new ArrayList<>();
    }
    public class Results {
        public List<String> bindings = new ArrayList<>();

        public class Data {
        }
    }
}
