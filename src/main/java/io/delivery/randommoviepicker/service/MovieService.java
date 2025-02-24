package io.delivery.randommoviepicker.service;

import io.delivery.randommoviepicker.config.APIConfig;
import io.delivery.randommoviepicker.model.Credits;
import io.delivery.randommoviepicker.model.Movie;
import io.delivery.randommoviepicker.model.MovieRange;
import io.delivery.randommoviepicker.model.MovieResponse;
import org.springframework.stereotype.Service;
import retrofit2.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class MovieService {
    private final TMDBApi api;
    private final APIConfig apiConfig;
    private final Random random;

    public MovieService(TMDBApi api, APIConfig apiConfig) {
        this.api = api;
        this.apiConfig = apiConfig;
        this.random = new Random();
    }

    public Movie getRandomMovie(MovieRange range) throws IOException {
        // Get list of popular movies
        List<Movie> allMovies = new ArrayList<>();
        int page = 1;
        int moviesPerPage = 20;

        while (allMovies.size() < range.getLimit()) {
            Response<MovieResponse> response = api.getPopularMovies(apiConfig.getApiKey(), page)
                    .execute();

            if (!response.isSuccessful() || response.body() == null) {
                throw new IOException("Failed to fetch movies on page " + page);
            }

            List<Movie> pageMovies = response.body().getResults();
            if (pageMovies.isEmpty()) {
                break;  // No more movies to fetch
            }

            allMovies.addAll(pageMovies);
            page++;
        }

        // Trim to exact limit
        if (allMovies.size() > range.getLimit()) {
            allMovies = allMovies.subList(0, range.getLimit());
        }

        // Get random movie
        Movie selectedMovie = allMovies.get(random.nextInt(allMovies.size()));

        System.out.println("Details URL: " + api.getMovieDetails(selectedMovie.getId(), apiConfig.getApiKey()).request().url());
        // Get detailed movie info
        Response<Movie> detailsResponse = api.getMovieDetails(
                selectedMovie.getId(),
                apiConfig.getApiKey()
        ).execute();

        if (detailsResponse.isSuccessful() && detailsResponse.body() != null) {
            Movie detailedMovie = detailsResponse.body();

            // Add this line to see the complete raw response
            System.out.println("COMPLETE RAW RESPONSE: " + detailsResponse.toString());

            // Add this debug block
            System.out.println("RAW API RESPONSE DATA:");
            System.out.println("Runtime: " + detailedMovie.getRuntime());
            System.out.println("Rating: " + detailedMovie.getRating());
            System.out.println("Release Date: " + detailedMovie.getReleaseDate());
            System.out.println("Poster Path: " + detailedMovie.getPosterPath());

            // Add base URL to poster path if it exists
            if (detailedMovie.getPosterPath() != null) {
                detailedMovie.setPosterPath("https://image.tmdb.org/t/p/w500" + detailedMovie.getPosterPath());
            }

            selectedMovie = detailedMovie;  // Update with detailed info
        }

        // Get director info
        Response<Credits> creditsResponse = api.getMovieCredits(
                selectedMovie.getId(),
                apiConfig.getApiKey()
        ).execute();

        if (creditsResponse.isSuccessful() && creditsResponse.body() != null) {
            String director = creditsResponse.body().getCrew().stream()
                    .filter(crew -> "Director".equals(crew.getJob()))
                    .map(crew -> crew.getName())
                    .findFirst()
                    .orElse("Unknown Director");

            selectedMovie.setDirector(director);
        }

        return selectedMovie;
    }

    public void testApiConnection() {
        try {
            Response<MovieResponse> response = api.getPopularMovies(apiConfig.getApiKey(), 1).execute();
            if (response.isSuccessful()) {
                System.out.println("API Connection Successful");
            } else {
                System.out.println("API Connection Failed: " + response.code());
            }
        } catch (Exception e) {
            System.out.println("API Connection Failed: " + e.getMessage());
        }
    }
}