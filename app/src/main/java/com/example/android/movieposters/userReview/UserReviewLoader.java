package com.example.android.movieposters.userReview;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.example.android.movieposters.trailer.Trailer;
import com.example.android.movieposters.trailer.TrailerUtils;

import java.util.List;

public class UserReviewLoader extends AsyncTaskLoader<List<UserReview>> {

    private String mUrl;

    public UserReviewLoader(Context context, String url){
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<UserReview> loadInBackground() {
        if(mUrl == null){
            return null;
        }

        List<UserReview> userReviews = UserReviewUtils.fetchUserReviewData(mUrl);
        return userReviews;
    }

}
