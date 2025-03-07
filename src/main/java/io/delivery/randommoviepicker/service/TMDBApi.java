package io.delivery.randommoviepicker.service;

import io.delivery.randommoviepicker.model.Credits;
import io.delivery.randommoviepicker.model.GenreResponse;
import io.delivery.randommoviepicker.model.Movie;
import io.delivery.randommoviepicker.model.MovieResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TMDBApi {
    @GET("movie/popular")
    Call<MovieResponse> getPopularMovies(
            @Query("api_key") String apiKey,
            @Query("page") Integer page
    );

    @GET("movie/{movie_id}")
    Call<Movie> getMovieDetails(
            @Path("movie_id") Integer movieId,
            @Query("api_key") String apiKey
    );

    @GET("movie/{movie_id}/credits")
    Call<Credits> getMovieCredits(
            @Path("movie_id") Integer movieId,
            @Query("api_key") String apiKey
    );

    @GET("genre/movie/list")
    Call<GenreResponse> getMovieGenres(
            @Query("api_key") String apiKey
    );

    @GET("discover/movie")
    Call<MovieResponse> getMoviesByGenre(
            @Query("api_key") String apiKey,
            @Query("with_genres") Integer genreId,
            @Query("page") Integer page
    );
}