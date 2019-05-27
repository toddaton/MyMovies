package com.example.android.movieposters.database;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.example.android.movieposters.moviePoster.MoviePoster;

import java.util.List;

public class MoviePosterRepository {

    private MovieDao mMovieDao;

    private LiveData<List<MoviePoster>> mFavoriteMovies;

    MoviePosterRepository(Application application){
        MoviePosterDatabase moviePosterDatabase = MoviePosterDatabase.getInstance(application);
        mMovieDao = moviePosterDatabase.movieDao();
        mFavoriteMovies = mMovieDao.loadAllTasks();
    }

    LiveData<List<MoviePoster>> loadAllTasks() {
        return mFavoriteMovies;
    }

    public void insert (MoviePoster moviePoster){
        new insertAsyncTask(mMovieDao).execute(moviePoster);
    }

    private static class insertAsyncTask extends AsyncTask<MoviePoster, Void, Void> {

        private MovieDao mAsyncTaskDao;

        insertAsyncTask(MovieDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final MoviePoster... params) {
            mAsyncTaskDao.insertMovie(params[0]);
            return null;
        }
    }
}


