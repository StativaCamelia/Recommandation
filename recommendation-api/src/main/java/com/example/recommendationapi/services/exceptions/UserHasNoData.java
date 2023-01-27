package com.example.recommendationapi.services.exceptions;

public class UserHasNoData extends Exception{

    public UserHasNoData(String errorMessage) {
        super(errorMessage);
    }
}
