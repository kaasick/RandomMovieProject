import axios from 'axios';
import { Movie, MovieRange, Genre } from '../types/movie';

const API_URL = 'http://localhost:8080/api';
//const API_URL = '/api';

export const getRandomMovie = async (range: MovieRange): Promise<Movie> => {
    const response = await axios.get(`${API_URL}/movie/random`, {
        params: { range }
    });
    return response.data;
};

// Add this function to fetch all movie genres
export const getMovieGenres = async (): Promise<Genre[]> => {
    const response = await axios.get(`${API_URL}/movie/genres`);
    return response.data;
};

// Add this function to fetch a random movie from a specific genre
export const getRandomMovieByGenre = async (genreId: number, range: MovieRange): Promise<Movie> => {
    const response = await axios.get(`${API_URL}/movie/random/genre/${genreId}`, {
        params: { range }
    });
    return response.data;
};