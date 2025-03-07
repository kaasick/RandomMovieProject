import { useState, useEffect } from 'react';
import { Movie, MovieRange, Genre } from './types/movie';
import { getRandomMovie, getMovieGenres, getRandomMovieByGenre } from './services/movieService';
import './App.css';

function App() {
    const [movie, setMovie] = useState<Movie | null>(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);
    const [genres, setGenres] = useState<Genre[]>([]);
    const [selectedGenreId, setSelectedGenreId] = useState<number | null>(null);

    // Fetch genres when component mounts
    useEffect(() => {
        const fetchGenres = async () => {
            try {
                const genreData = await getMovieGenres();
                setGenres(genreData);
            } catch (err) {
                console.error('Failed to load genres:', err);
            }
        };

        fetchGenres();
    }, []);

    const fetchRandomMovie = async () => {
        try {
            setLoading(true);
            setError(null);

            let result: Movie;
            if (selectedGenreId) {
                // Fetch random movie from selected genre
                result = await getRandomMovieByGenre(selectedGenreId, MovieRange.TOP_100);
            } else {
                // Fetch random movie from any genre
                result = await getRandomMovie(MovieRange.TOP_100);
            }

            setMovie(result);
        } catch (err) {
            setError('Failed to fetch movie');
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    // Handle genre selection change
    const handleGenreChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
        const value = e.target.value;
        if (value === 'popular') {
            setSelectedGenreId(null);
        } else {
            const genreId = parseInt(value);
            setSelectedGenreId(isNaN(genreId) ? null : genreId);
        }
    };

    // Format rating to show X.X/10
    const formatRating = (rating: number | null): string => {
        if (rating === null) return 'N/A';
        return rating.toFixed(1) + '/10';
    };

    //  YYYY-MM-DD format
    const formatReleaseDate = (date: string | null): string => {
        if (!date) return 'Unknown';
        try {
            const [year, month, day] = date.split('-');
            const formattedDate = new Date(
                parseInt(year),
                parseInt(month) - 1, // Month is 0-indexed in JS
                parseInt(day)
            );
            return formattedDate.toLocaleDateString('en-US', {
                year: 'numeric',
                month: 'long',
                day: 'numeric'
            });
        } catch (e) {
            return date;
        }
    };

    return (
        <>
            <h1>Random Movie Picker</h1>
            <div className="card">
                {/* Genre selection dropdown */}
                <div className="genre-selector">
                    <label htmlFor="genre-select">Choose a genre: </label>
                    <select
                        id="genre-select"
                        value={selectedGenreId === null ? 'popular' : (selectedGenreId || '')}
                        onChange={handleGenreChange}
                    >
                        <option value="popular">Popular Movies</option>
                        <option value="">All Genres</option>
                        {genres.map(genre => (
                            <option key={genre.id} value={genre.id}>
                                {genre.name}
                            </option>
                        ))}
                    </select>
                </div>

                <button onClick={fetchRandomMovie} disabled={loading}>
                    {loading ? 'Loading...' :
                        selectedGenreId === null ? 'Get Random Popular Movie' :
                            (selectedGenreId ? `Get Random ${genres.find(g => g.id === selectedGenreId)?.name} Movie` : 'Get Random Movie')}
                </button>

                {error && <p style={{ color: 'red' }}>{error}</p>}

                {movie && (
                    <div className="movie-details">
                        <h2>{movie.title}</h2>

                        <div className="movie-meta">
                            <p><strong>Director:</strong> {movie.director || 'Unknown'}</p>
                            <p><strong>Runtime:</strong> {movie.runtime ? `${movie.runtime} minutes` : 'N/A'}</p>
                            <p><strong>Rating:</strong> {formatRating(movie.rating)}</p>
                            <p><strong>Release Date:</strong> {formatReleaseDate(movie.releaseDate)}</p>
                        </div>

                        {movie.posterPath && (
                            <div className="movie-poster">
                                <img
                                    src={movie.posterPath}
                                    alt={`${movie.title} poster`}
                                    style={{ maxWidth: '300px', marginTop: '20px' }}
                                    onError={(e) => {
                                        // Hide the image if it fails to load
                                        (e.target as HTMLImageElement).style.display = 'none';
                                    }}
                                />
                            </div>
                        )}

                        <div className="movie-overview">
                            <h3>Overview</h3>
                            <p>{movie.overview}</p>
                        </div>

                        {/* Debug */}
                        {/*
                        <details>
                            <summary>Debug Info</summary>
                            <pre style={{fontSize: '12px'}}>
                                {JSON.stringify(movie, null, 2)}
                            </pre>
                        </details>
                        */}

                        {/* Additional Debug for Genre */}
                        {/*
                        <details>
                            <summary>Genre Debug Info</summary>
                            <pre style={{fontSize: '12px'}}>
                                <p>Selected Genre ID: {selectedGenreId || 'None'}</p>
                                <p>Selected Genre Name: {selectedGenreId ? genres.find(g => g.id === selectedGenreId)?.name : 'Any'}</p>
                                <p>Available Genres:</p>
                                {JSON.stringify(genres, null, 2)}
                            </pre>
                        </details>
                        */}
                    </div>
                )}
            </div>
        </>
    );
}

export default App;