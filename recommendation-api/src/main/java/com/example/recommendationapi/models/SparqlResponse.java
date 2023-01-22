package com.example.recommendationapi.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SparqlResponse {
    public Map<String, List<String>> head = new HashMap<>();
    public Map<String, List<Map<String, Map<String, String>>>> results = new HashMap<>();
}
