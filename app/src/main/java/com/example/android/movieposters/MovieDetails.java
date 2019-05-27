package com.example.android.movieposters;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.movieposters.database.MoviePosterDatabase;
import com.example.android.movieposters.moviePoster.MovieAdapter;
import com.example.android.movieposters.moviePoster.MoviePoster;
import com.example.android.movieposters.trailer.MovieTrailerAdapter;
import com.example.android.movieposters.trailer.Trailer;
import com.example.android.movieposters.trailer.TrailerLoader;
import com.example.android.movieposters.userReview.UserReview;
import com.example.android.movieposters.userReview.UserReviewAdapter;
import com.example.android.movieposters.userReview.UserReviewLoader;
import com.squareup.picasso.Picasso;
//import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MovieDetails extends AppCompatActivity {

    private MoviePoster mCurrentMoviePoster;

    public static final String POSITION = "position";

    private static final int DEFAULT_POSITION = -1;

    public static String mMovieTrailerUrl;

    private MovieTrailerAdapter mMovieTrailerAdapter;

    private RecyclerView mRecyclerView;

    public static CheckBox mFavoritesCheckBox;

    private boolean[] mFavoritesList = {false, false, false, false, false, false, false, false, false, false,
            false, false, false, false, false, false, false, false, false, false};


    private MoviePosterDatabase mMoviePosterDatabase;

    private String mUserReviewUrl;

    private UserReviewAdapter mUserReviewAdapter;

    private RecyclerView mUserReviewRecyclerView;

    private static final int DEFAULT_MOVIE_ID = -1;

    public static final String INSTANCE_MOVIE_ID = "instanceMovieId";

    private int mMovieId = DEFAULT_MOVIE_ID;

    public static final String EXTRA_MOVIE_ID = "extraMovieId";

    private static final String LIFECYCLE_CALLBACKS_TITLE_KEY = "movieTitle";

    private static final String LIFECYCLE_CALLBACKS_IMAGE_KEY = "movieImage";

    private static final String LIFECYCLE_CALLBACKS_PLOT_KEY = "moviePlotSynopsis";

    private static final String LIFECYCLE_CALLBACKS_RATING_KEY = "movieRating";

    private static final String LIFECYCLE_CALLBACKS_DATE_KEY = "movieReleaseDate";

    private TextView mOriginalTitleTV;

    private TextView mPlotSynopsisTV;

    private TextView mUserRatingTV;

    private TextView mReleaseDateTV;

    public static final String EXTRA_ID = "ID";

    public static final String EXTRA_ORIGINAL_TITLE = "ORIGINALTITLE";

    public static final String EXTRA_IMAGE_URL = "IMAGEURL";

    public static final String EXTRA_PLOT_SYNOPSIS = "PLOTSYNOPSIS";

    public static final String EXTRA_USER_RATING = "USERRATING";

    public static final String EXTRA_RELEASE_DATE = "RELEASEDATE";

    private static final int TRAILER_LOADER = 1;

    private static final int USER_REVIEW_LOADER = 2;

    private RecyclerView trailerRecyclerView;

    private static int mScreenPosition = 0;

    private LinearLayoutManager mLinearLayoutManager;

    private Parcelable mListState;

    public static final String SCROLLVIEW = "scrollview";

    private ScrollView mScrollView;


    /**
     * @Override protected void onSaveInstanceState(Bundle outState) {
     * outState.putInt(INSTANCE_MOVIE_ID, mMovieId);
     * super.onSaveInstanceState(outState);
     * }
     **/

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putIntArray(SCROLLVIEW, new int[]{mScrollView.getScrollX(), mScrollView.getScrollY()});



        /**outState.putInt(INSTANCE_MOVIE_ID, mMovieId);

        String originalTitle = mCurrentMoviePoster.getOriginalTitle();
        outState.putString(LIFECYCLE_CALLBACKS_TITLE_KEY, originalTitle);

        String imageURL = mCurrentMoviePoster.getImageURL();
        outState.putString(LIFECYCLE_CALLBACKS_IMAGE_KEY, imageURL);

        String plotSynopsis = mCurrentMoviePoster.getPlotSynopsis();
        outState.putString(LIFECYCLE_CALLBACKS_PLOT_KEY, plotSynopsis);

        String userRating = mCurrentMoviePoster.getUserRating();
        outState.putString(LIFECYCLE_CALLBACKS_RATING_KEY, userRating);

        String releaseDate = mCurrentMoviePoster.getReleaseDate();
        outState.putString(LIFECYCLE_CALLBACKS_DATE_KEY, releaseDate);**/
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        final int[] scrollPosition = savedInstanceState.getIntArray(SCROLLVIEW);
        if(scrollPosition != null){
            mScrollView.post(new Runnable() {
                @Override
                public void run() {
                    mScrollView.scrollTo(scrollPosition[0], scrollPosition[1]);
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        mScrollView = (ScrollView) findViewById(R.id.scrollView);

        mOriginalTitleTV = (TextView) findViewById(R.id.originalTitle);

        mPlotSynopsisTV = (TextView) findViewById(R.id.plotSynopsis);

        mUserRatingTV = (TextView) findViewById(R.id.userRating);

        mReleaseDateTV = (TextView) findViewById(R.id.releaseDate);

        ImageView imageView = (ImageView) findViewById(R.id.detailsImage);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(LIFECYCLE_CALLBACKS_TITLE_KEY)) {
                String titleKey = savedInstanceState
                        .getString(LIFECYCLE_CALLBACKS_TITLE_KEY);
                //mCurrentMoviePoster.setOriginalTitle(titleKey);
                mOriginalTitleTV.setText(titleKey);
            }
            if (savedInstanceState.containsKey(LIFECYCLE_CALLBACKS_IMAGE_KEY)) {
                String imageKey = savedInstanceState
                        .getString(LIFECYCLE_CALLBACKS_IMAGE_KEY);
                String path = getString(R.string.url);
                Picasso.with(this).load(path + imageKey).into(imageView);
            }
            if (savedInstanceState.containsKey(LIFECYCLE_CALLBACKS_PLOT_KEY)) {
                String plotSynopsisKey = savedInstanceState
                        .getString(LIFECYCLE_CALLBACKS_PLOT_KEY);
                //mCurrentMoviePoster.setPlotSynopsis(plotSynopsisKey);
                mPlotSynopsisTV.setText(plotSynopsisKey);
            }
            if (savedInstanceState.containsKey(LIFECYCLE_CALLBACKS_RATING_KEY)) {
                String ratingKey = savedInstanceState
                        .getString(LIFECYCLE_CALLBACKS_RATING_KEY);
                //mCurrentMoviePoster.setUserRating(ratingKey);
                mUserRatingTV.setText(ratingKey);
            }
            if (savedInstanceState.containsKey(LIFECYCLE_CALLBACKS_DATE_KEY)) {
                String releaseDateKey = savedInstanceState
                        .getString(LIFECYCLE_CALLBACKS_DATE_KEY);
                //mCurrentMoviePoster.setReleaseDate(releaseDateKey);
                mReleaseDateTV.setText(releaseDateKey);
            }
        }

        initViews();

        mMoviePosterDatabase = MoviePosterDatabase.getInstance(getApplicationContext());

        if (savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_MOVIE_ID)) {
            mMovieId = savedInstanceState.getInt(INSTANCE_MOVIE_ID, DEFAULT_MOVIE_ID);
        }

        Intent intent = getIntent();

        int imageNumber = intent.getIntExtra(POSITION, DEFAULT_POSITION);

        if (imageNumber == DEFAULT_POSITION) {
            Toast.makeText(this, R.string.error, Toast.LENGTH_LONG).show();
        } else {

            mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

            mLinearLayoutManager = new LinearLayoutManager(this);

            //mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

            mRecyclerView.setLayoutManager(mLinearLayoutManager);

            mUserReviewRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewUserReview);

            mUserReviewRecyclerView.setLayoutManager(new LinearLayoutManager(this));

            mCurrentMoviePoster = MovieAdapter.mMovieData.get(imageNumber);

            mFavoritesCheckBox = (CheckBox) findViewById(R.id.checkbox);

            mFavoritesCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent checkBoxClickedIntent = new Intent();

                    String moviePosterId = mCurrentMoviePoster.getId();
                    checkBoxClickedIntent.putExtra(EXTRA_ID, moviePosterId);

                    String moviePosterOriginalTitle = mCurrentMoviePoster.getOriginalTitle();
                    checkBoxClickedIntent.putExtra(EXTRA_ORIGINAL_TITLE, moviePosterOriginalTitle);

                    String moviePosterImageURL = mCurrentMoviePoster.getImageURL();
                    checkBoxClickedIntent.putExtra(EXTRA_IMAGE_URL, moviePosterImageURL);

                    String moviePosterPlotSynopsis = mCurrentMoviePoster.getPlotSynopsis();
                    checkBoxClickedIntent.putExtra(EXTRA_PLOT_SYNOPSIS, moviePosterPlotSynopsis);

                    String moviePosterUserRating = mCurrentMoviePoster.getUserRating();
                    checkBoxClickedIntent.putExtra(EXTRA_USER_RATING, moviePosterUserRating);

                    String moviePosterReleaseDate = mCurrentMoviePoster.getReleaseDate();
                    checkBoxClickedIntent.putExtra(EXTRA_RELEASE_DATE, moviePosterReleaseDate);
                    setResult(RESULT_OK, checkBoxClickedIntent);
                }
            });

            Uri.Builder trailerBuilder = new Uri.Builder();
            trailerBuilder.scheme("http")
                    .authority("api.themoviedb.org")
                    .appendPath("3")
                    .appendPath("movie")
                    .appendPath(mCurrentMoviePoster.getId())
                    .appendPath("videos")
                    .appendQueryParameter("api_key", "Insert Api Key");

            mMovieTrailerUrl = trailerBuilder.build().toString();

            Uri.Builder reviewBuilder = new Uri.Builder();
            reviewBuilder.scheme("https")
                    .authority("api.themoviedb.org")
                    .appendPath("3")
                    .appendPath("movie")
                    .appendPath(mCurrentMoviePoster.getId())
                    .appendPath("reviews")
                    .appendQueryParameter("api_key", "Insert Api Key");

            mUserReviewUrl = reviewBuilder.build().toString();

            populateUi();

            String path = getString(R.string.url);

            Picasso.with(this).load(path + mCurrentMoviePoster.getImageURL()).into(imageView);

            mRecyclerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    launchTrailer(v);
                }
            });

            LoaderManager.LoaderCallbacks<List<Trailer>> trailerLoaderManager = new LoaderManager.LoaderCallbacks<List<Trailer>>() {
                @Override
                public Loader<List<Trailer>> onCreateLoader(int i, Bundle bundle) {
                    return new TrailerLoader(MovieDetails.this, mMovieTrailerUrl);
                }

                @Override
                public void onLoadFinished(Loader<List<Trailer>> loader, List<Trailer> trailer) {
                    mMovieTrailerAdapter = new MovieTrailerAdapter(MovieDetails.this, trailer);
                    mRecyclerView.setAdapter(mMovieTrailerAdapter);
                }

                @Override
                public void onLoaderReset(Loader<List<Trailer>> loader) {
                }
            };

            LoaderManager.LoaderCallbacks<List<UserReview>> userReviewLoaderManager = new LoaderManager.LoaderCallbacks<List<UserReview>>() {
                @Override
                public Loader<List<UserReview>> onCreateLoader(int i, Bundle bundle) {
                    return new UserReviewLoader(MovieDetails.this, mUserReviewUrl);
                }

                @Override
                public void onLoadFinished(Loader<List<UserReview>> loader, List<UserReview> userReviews) {
                    mUserReviewAdapter = new UserReviewAdapter(MovieDetails.this, userReviews);
                    mUserReviewRecyclerView.setAdapter(mUserReviewAdapter);
                }

                @Override
                public void onLoaderReset(Loader<List<UserReview>> loader) {
                }
            };

            getLoaderManager().initLoader(TRAILER_LOADER, new Bundle(), trailerLoaderManager);

            getLoaderManager().initLoader(USER_REVIEW_LOADER, new Bundle(), userReviewLoaderManager);
        }


    }

    /**@Override
    protected void onPause() {
        super.onPause();
        mScreenPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
    }**/

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void populateUi() {
        TextView originalTitle = (TextView) findViewById(R.id.originalTitle);
        originalTitle.setText(mCurrentMoviePoster.getOriginalTitle());

        TextView plotSynopsis = (TextView) findViewById(R.id.plotSynopsis);
        plotSynopsis.setText(mCurrentMoviePoster.getPlotSynopsis());

        TextView userRating = (TextView) findViewById(R.id.userRating);
        userRating.setText(mCurrentMoviePoster.getUserRating());

        TextView releaseDate = (TextView) findViewById(R.id.releaseDate);
        releaseDate.setText(mCurrentMoviePoster.getReleaseDate());

    }

    private void initViews() {
        mFavoritesCheckBox = (CheckBox) findViewById(R.id.checkbox);
        mFavoritesCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveCheckBoxClicked();
            }
        });
    }

    public void onSaveCheckBoxClicked() {
        boolean isChecked = mFavoritesCheckBox.isChecked();

        MoviePoster moviePoster = new MoviePoster(mCurrentMoviePoster.getId(), mCurrentMoviePoster.getOriginalTitle(),
                mCurrentMoviePoster.getImageURL(), mCurrentMoviePoster.getPlotSynopsis(),
                mCurrentMoviePoster.getUserRating(), mCurrentMoviePoster.getReleaseDate());

        mMoviePosterDatabase.movieDao().insertMovie(moviePoster);

        finish();
    }

    public void launchTrailer(View v) {
        Intent youtubeIntent = new Intent();
        youtubeIntent.setAction(Intent.ACTION_VIEW);
        startActivity(youtubeIntent);
    }

}
