package com.example.recommendationapi.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SparqlResponse {
    public Map<String, List<String>> head = new HashMap<>();
    public Map<String, List<Map<String, Map<String, String>>>> results = new HashMap<>();
    public boolean isError;
    public String message;
    public String code;

    public Result GetResult(Integer top) {
        Result result = new Result();
        List<Map<String, String>> finalList = new ArrayList<>();
        List<Map<String, Map<String, String>>> bindings = this.results.get("bindings");
        for (Map<String, Map<String, String>> entity : bindings) {
            if (finalList.size() == top && top != 0) {
                break;
            }
            Map<String, String> finalValue = new HashMap<>();
            entity.forEach((key, value) -> {
                finalValue.put(key, value.get("value"));
            });
            finalList.add(finalValue);
        }
        result.variables = this.head.get("vars");
        result.results = finalList;

        return result;
    }
}
