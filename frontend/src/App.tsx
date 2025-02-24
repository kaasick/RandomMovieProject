import { useState } from 'react';
import { Movie, MovieRange } from './types/movie';
import { getRandomMovie } from './services/movieService';
import './App.css';

function App() {
    const [movie, setMovie] = useState<Movie | null>(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);

    const fetchRandomMovie = async () => {
        try {
            setLoading(true);
            setError(null);
            const result = await getRandomMovie(MovieRange.TOP_100);
            setMovie(result);
        } catch (err) {
            setError('Failed to fetch movie');
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    // Format rating to show X.X/10
    const formatRating = (rating: number | null): string => {
        if (rating === null) return 'N/A';
        return rating.toFixed(1) + '/10';
    };

    // Format release date (assuming YYYY-MM-DD format)
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
                <button onClick={fetchRandomMovie} disabled={loading}>
                    {loading ? 'Loading...' : 'Get Random Movie'}
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
                                    src={`https://image.tmdb.org/t/p/w500${movie.posterPath}`}
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
                    </div>
                )}
            </div>
        </>
    );
}

export default App;