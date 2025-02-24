export enum MovieRange {
    TOP_100 = "TOP_100",
    TOP_250 = "TOP_250",
    TOP_500 = "TOP_500",
    TOP_1000 = "TOP_1000"
}

export interface Movie {
    id: number;
    title: string;
    overview: string;
    releaseDate: string;
    rating: number;
    director: string;
    posterPath: string;
    runtime: number;
}

