# Random Movie Picker

## Overview
🎬 A work in progress application that randomly selects movies using The Movie Database (TMDB) API. Built with Spring Boot backend and React frontend.

## Tech Stack
- Backend: Spring Boot
- Frontend: React + Vite
- API: TMDB (The Movie Database)

## Prerequisites
- Java 21
- Node.js
- TMDB API key

## Setup Instructions

1. Clone the repository
```bash
git clone [your-repository-url]
cd RandomMovieProject
```

2. Set up environment variables
```bash
# Copy .env.example to .env
# Add your TMDB API key to .env:
TMDB_API_KEY=your_api_key_here
```

3. Install dependencies
```bash
# Install all dependencies (both frontend and backend)
npm run install:all
```

## Running the Application

To run both frontend and backend concurrently:
```bash
npm run dev
```

This will start:
- Backend server at http://localhost:8080
- Frontend development server at http://localhost:5173

## Development
- Frontend code is in the `frontend` directory
- Backend code is in the `src` directory

## Notes
- This is a work in progress project
- More features coming soon