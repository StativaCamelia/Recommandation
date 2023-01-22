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

    public Result GetResult(Integer pageIndex, Integer pageSize) {
        Result result = new Result();
        List<Map<String, String>> finalList = new ArrayList<>();
        List<Map<String, Map<String, String>>> bindings = this.results.get("bindings");

        int totalCount = bindings.size();
        if (pageSize == 0) {
            pageSize = totalCount;
        }
        pageIndex = pageIndex - 1;
        int startIndex = pageIndex * pageSize;
        int endIndex = Math.min((pageIndex + 1) * pageSize, totalCount);
        for (int i = startIndex; i < endIndex; i++) {
            Map<String, String> finalValue = new HashMap<>();
            bindings.get(i).forEach((key, value) -> finalValue.put(key, value.get("value")));

            finalList.add(finalValue);
        }
        result.variables = this.head.get("vars");
        result.results = finalList;
        result.totalCount = totalCount;

        return result;
    }
}
