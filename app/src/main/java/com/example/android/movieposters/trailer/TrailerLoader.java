package com.example.android.movieposters.trailer;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class TrailerLoader extends AsyncTaskLoader<List<Trailer>> {

    private String mUrl;

    public TrailerLoader(Context context, String url){
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Trailer> loadInBackground() {
        if(mUrl == null){
            return null;
        }

        List<Trailer> trailers = TrailerUtils.fetchTrailerData(mUrl);
        return trailers;
    }

}

