package io.delivery.randommoviepicker.Controller;

import io.delivery.randommoviepicker.model.Movie;
import io.delivery.randommoviepicker.model.MovieRange;
import io.delivery.randommoviepicker.service.MovieService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class MovieController {
    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

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
}
