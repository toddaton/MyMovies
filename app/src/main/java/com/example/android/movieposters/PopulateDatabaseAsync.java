package com.example.android.movieposters.database;

import android.os.AsyncTask;

import com.example.android.movieposters.database.Database;
import com.example.android.movieposters.database.MovieDao;

public class PopulateDatabaseAsync extends AsyncTask<Void, Void, Void> {

    private final MovieDao mMovieDao;

    public PopulateDatabaseAsync(Database database){
        mMovieDao = database.movieDao();
    }

    @Override
    protected Void doInBackground(final Void... voids) {
        mMovieDao.deleteAllMovies();
        return null;
    }
}
