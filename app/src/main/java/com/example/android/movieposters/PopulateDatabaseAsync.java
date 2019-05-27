package com.example.android.movieposters.database;

import android.os.AsyncTask;

public class PopulateDatabaseAsync extends AsyncTask<Void, Void, Void> {

    private final MovieDao mMovieDao;

    public PopulateDatabaseAsync(MoviePosterDatabase moviePosterDatabase){
        mMovieDao = moviePosterDatabase.movieDao();
    }

    @Override
    protected Void doInBackground(final Void... voids) {
        mMovieDao.deleteAllMovies();
        return null;
    }
}
