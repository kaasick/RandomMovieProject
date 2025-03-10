package io.delivery.randommoviepicker.config;

import io.delivery.randommoviepicker.service.TMDBApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Configuration
public class RetrofitConfig {
    private final APIConfig apiConfig;

    public RetrofitConfig(APIConfig apiConfig) {
        this.apiConfig = apiConfig;
    }

    @Bean
    public TMDBApi tmdbApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(apiConfig.getBaseUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(TMDBApi.class);
    }
}