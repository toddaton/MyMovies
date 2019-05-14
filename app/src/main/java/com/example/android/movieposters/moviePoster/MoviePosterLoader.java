package com.example.android.movieposters.moviePoster;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.example.android.movieposters.moviePoster.ApiUtils;
import com.example.android.movieposters.moviePoster.MoviePoster;

import java.util.List;

public class MoviePosterLoader extends AsyncTaskLoader<List<MoviePoster>> {

    private String mUrl;

    public MoviePosterLoader(Context context, String url){
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<MoviePoster> loadInBackground() {
        if(mUrl == null){
            return null;
        }

        List<MoviePoster> moviePosters = ApiUtils.fetchMoviePosterData(mUrl);
        return moviePosters;
    }
}
