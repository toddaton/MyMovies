package com.example.android.movieposters.database;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.example.android.movieposters.moviePoster.MoviePoster;

import java.util.List;

public class MoviePosterViewModel extends AndroidViewModel {

    private MoviePosterRepository mMoviePosterRepository;

    private LiveData<List<MoviePoster>> mFavoriteMovies;

    public MoviePosterViewModel (Application application){
        super(application);
        mMoviePosterRepository = new MoviePosterRepository(application);
        mFavoriteMovies = mMoviePosterRepository.loadAllTasks();
    }

    public LiveData<List<MoviePoster>> getFavoriteMovies(){
        return mFavoriteMovies;
    }

    public void insert(MoviePoster moviePoster){
        mMoviePosterRepository.insert(moviePoster);
    }
}
