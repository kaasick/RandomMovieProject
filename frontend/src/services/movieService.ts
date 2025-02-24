import axios from 'axios';
import { Movie, MovieRange } from '../types/movie';

const API_URL = 'http://localhost:8080/api';

export const getRandomMovie = async (range: MovieRange): Promise<Movie> => {
    const response = await axios.get(`${API_URL}/movie/random`, {
        params: { range }
    });
    return response.data;
};