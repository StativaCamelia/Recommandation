package com.example.recommendationapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class RecommendationApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(RecommendationApiApplication.class, args);
    }

}
