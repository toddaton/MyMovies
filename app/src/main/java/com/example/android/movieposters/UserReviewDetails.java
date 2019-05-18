package com.example.android.movieposters;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.android.movieposters.moviePoster.MovieAdapter;
import com.example.android.movieposters.moviePoster.MoviePoster;
import com.example.android.movieposters.trailer.MovieTrailerAdapter;
import com.example.android.movieposters.trailer.Trailer;
import com.example.android.movieposters.trailer.TrailerLoader;
import com.example.android.movieposters.userReview.UserReview;
import com.example.android.movieposters.userReview.UserReviewAdapter;
import com.example.android.movieposters.userReview.UserReviewLoader;

import java.util.List;

public class UserReviewDetails extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<UserReview>>{

    public static final String USER_POSITION = "position";

    private static final int DEFAULT_POSITION = -1;

    private RecyclerView mUserReviewRecyclerView;

    private MoviePoster mCurrentMoviePoster;

    private String mUserReviewUrl;

    private UserReviewAdapter mUserReviewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        int imageNumber = intent.getIntExtra(USER_POSITION, DEFAULT_POSITION);

        if (imageNumber == DEFAULT_POSITION) {
            Toast.makeText(this, R.string.error, Toast.LENGTH_LONG).show();
        }else {

            mUserReviewRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewUserReview);

            mCurrentMoviePoster = MovieAdapter.mMovieData.get(imageNumber);

            Uri.Builder reviewBuilder = new Uri.Builder();
            reviewBuilder.scheme("https")
                    .authority("api.themoviedb.org")
                    .appendPath("3")
                    .appendPath("review")
                    .appendPath(mCurrentMoviePoster.getId())
                    .appendQueryParameter("api_key", "207feff8f2c6bd2e000f64463742abea");

            mUserReviewUrl = reviewBuilder.build().toString();
        }
    }

    @Override
    public Loader<List<UserReview>> onCreateLoader(int id, Bundle args) {
        return new UserReviewLoader(UserReviewDetails.this, mUserReviewUrl);    }

    @Override
    public void onLoadFinished(Loader<List<UserReview>> loader, List<UserReview> data) {
        mUserReviewAdapter = new UserReviewAdapter(UserReviewDetails.this, data);
        mUserReviewRecyclerView.setAdapter(mUserReviewAdapter);
    }

    @Override
    public void onLoaderReset(Loader<List<UserReview>> loader) {

    }
}
