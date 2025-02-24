package io.delivery.randommoviepicker.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class APIConfig {

    @Value("${tmdb.api.key}")
    private String apiKey;

    private static final String BASE_URL = "https://api.themoviedb.org/3/";

    public String getApiKey() {
        System.out.println("Using API key: " + apiKey); // Temporary debug line
        return apiKey;
    }

    public String getBaseUrl() {
        return BASE_URL;
    }
}