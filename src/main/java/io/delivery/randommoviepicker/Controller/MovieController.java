package io.delivery.randommoviepicker.Controller;

import io.delivery.randommoviepicker.model.Genre;
import io.delivery.randommoviepicker.model.Movie;
import io.delivery.randommoviepicker.model.MovieRange;
import io.delivery.randommoviepicker.model.MovieResponse;
import io.delivery.randommoviepicker.service.MovieService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
public class MovieController {
    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    //Endpoint - get a random movie from popular movies
    @GetMapping("/api/movie/random")
    public ResponseEntity<?> getRandomMovie(@RequestParam(defaultValue = "TOP_100") MovieRange range) {
        try {
            Movie movie = movieService.getRandomMovie(range);
            return ResponseEntity.ok(movie);
        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .body("Error fetching movie: " + e.getMessage());
        }
    }

    // Endpoint to get all genres
    @GetMapping("/api/movie/genres")
    public ResponseEntity<?> getMovieGenres() {
        try {
            List<Genre> genres = movieService.getMovieGenres();
            return ResponseEntity.ok(genres);
        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .body("Error fetching genres: " + e.getMessage());
        }
    }

    // Endpoint - movies by genre
    @GetMapping("/api/movie/genre/{genreId}")
    public ResponseEntity<?> getMoviesByGenre(
            @PathVariable Integer genreId,
            @RequestParam(defaultValue = "1") Integer page) {
        try {
            MovieResponse response = movieService.getMoviesByGenre(genreId, page);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .body("Error fetching movies for genre " + genreId + ": " + e.getMessage());
        }
    }

    // Endpoint - random movie from a specific genre
    @GetMapping("/api/movie/random/genre/{genreId}")
    public ResponseEntity<?> getRandomMovieByGenre(
            @PathVariable Integer genreId,
            @RequestParam(defaultValue = "TOP_100") MovieRange range) {
        try {
            Movie movie = movieService.getRandomMovieByGenre(genreId, range);
            return ResponseEntity.ok(movie);
        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .body("Error fetching random movie for genre " + genreId + ": " + e.getMessage());
        }
    }
}