package io.delivery.randommoviepicker.service;

import io.delivery.randommoviepicker.config.APIConfig;
import io.delivery.randommoviepicker.model.Credits;
import io.delivery.randommoviepicker.model.Genre;
import io.delivery.randommoviepicker.model.GenreResponse;
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

    // Method to get all movie genres
    public List<Genre> getMovieGenres() throws IOException {
        Response<GenreResponse> response = api.getMovieGenres(apiConfig.getApiKey()).execute();

        if (!response.isSuccessful() || response.body() == null) {
            throw new IOException("Failed to fetch movie genres");
        }

        return response.body().getGenres();
    }

    // Method to get movies by genre
    public MovieResponse getMoviesByGenre(Integer genreId, Integer page) throws IOException {
        Response<MovieResponse> response = api.getMoviesByGenre(
                apiConfig.getApiKey(),
                genreId,
                page
        ).execute();

        if (!response.isSuccessful() || response.body() == null) {
            throw new IOException("Failed to fetch movies for genre " + genreId);
        }

        return response.body();
    }

    // Get a random movie from a specific genre
    public Movie getRandomMovieByGenre(Integer genreId, MovieRange range) throws IOException {
        // Get list of movies by genre
        List<Movie> allMovies = new ArrayList<>();
        int page = 1;

        while (allMovies.size() < range.getLimit()) {
            Response<MovieResponse> response = api.getMoviesByGenre(
                    apiConfig.getApiKey(),
                    genreId,
                    page
            ).execute();

            if (!response.isSuccessful() || response.body() == null) {
                throw new IOException("Failed to fetch movies on page " + page);
            }

            List<Movie> pageMovies = response.body().getResults();
            if (pageMovies.isEmpty()) {
                break;
            }

            allMovies.addAll(pageMovies);
            page++;
        }

        if (allMovies.size() > range.getLimit()) {
            allMovies = allMovies.subList(0, range.getLimit());
        }

        // Get random movie
        Movie selectedMovie = allMovies.get(random.nextInt(allMovies.size()));

        // Fetch complete movie details
        return fetchMovieDetails(selectedMovie);
    }

    // Get random movie from popular movies
    public Movie getRandomMovie(MovieRange range) throws IOException {
        // Get list of popular movies
        List<Movie> allMovies = new ArrayList<>();
        int page = 1;

        while (allMovies.size() < range.getLimit()) {
            Response<MovieResponse> response = api.getPopularMovies(apiConfig.getApiKey(), page)
                    .execute();

            if (!response.isSuccessful() || response.body() == null) {
                throw new IOException("Failed to fetch movies on page " + page);
            }

            List<Movie> pageMovies = response.body().getResults();
            if (pageMovies.isEmpty()) {
                break;
            }

            allMovies.addAll(pageMovies);
            page++;
        }


        if (allMovies.size() > range.getLimit()) {
            allMovies = allMovies.subList(0, range.getLimit());
        }


        Movie selectedMovie = allMovies.get(random.nextInt(allMovies.size()));


        return fetchMovieDetails(selectedMovie);
    }

    // Helper method to fetch complete movie details
    private Movie fetchMovieDetails(Movie movie) throws IOException {

        Response<Movie> detailsResponse = api.getMovieDetails(
                movie.getId(),
                apiConfig.getApiKey()
        ).execute();

        if (detailsResponse.isSuccessful() && detailsResponse.body() != null) {
            Movie detailedMovie = detailsResponse.body();


            if (detailedMovie.getPosterPath() != null) {
                detailedMovie.setPosterPath("https://image.tmdb.org/t/p/w500" + detailedMovie.getPosterPath());
            }

            movie = detailedMovie;
        }


        addDirectorInfo(movie);

        return movie;
    }

    // Helper method to add director information to a movie
    // Because the director info is not stored on the most basic API call, it is stored with the crew information
    // from where it needs to be extracted
    private void addDirectorInfo(Movie movie) throws IOException {
        Response<Credits> creditsResponse = api.getMovieCredits(
                movie.getId(),
                apiConfig.getApiKey()
        ).execute();

        if (creditsResponse.isSuccessful() && creditsResponse.body() != null) {
            String director = creditsResponse.body().getCrew().stream()
                    .filter(crew -> "Director".equals(crew.getJob()))
                    .map(crew -> crew.getName())
                    .findFirst()
                    .orElse("Unknown Director");

            movie.setDirector(director);
        }
    }
}