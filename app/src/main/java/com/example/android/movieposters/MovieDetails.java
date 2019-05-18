package com.example.android.movieposters;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.movieposters.database.Database;
import com.example.android.movieposters.moviePoster.MovieAdapter;
import com.example.android.movieposters.moviePoster.MoviePoster;
import com.example.android.movieposters.trailer.MovieTrailerAdapter;
import com.example.android.movieposters.trailer.Trailer;
import com.example.android.movieposters.trailer.TrailerLoader;
import com.squareup.picasso.Picasso;
//import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieDetails extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Trailer>>{

    private MoviePoster mCurrentMoviePoster;

    public static final String POSITION = "position";

    private static final int DEFAULT_POSITION = -1;

    public static String mMovieTrailerUrl;

    public static String mUserReviewUrl;

    private MovieTrailerAdapter mMovieTrailerAdapter;

    private RecyclerView mRecyclerView;

    private RecyclerView mUserReviewRecyclerView;

    public static CheckBox mFavoritesCheckBox;

    private Database mDatabase;

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


    /**@Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(INSTANCE_MOVIE_ID, mMovieId);
        super.onSaveInstanceState(outState);
    }**/

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(INSTANCE_MOVIE_ID, mMovieId);

        String originalTitle = mCurrentMoviePoster.getOriginalTitle();
        outState.putString(LIFECYCLE_CALLBACKS_TITLE_KEY, originalTitle);

        String imageURL = mCurrentMoviePoster.getImageURL();
        outState.putString(LIFECYCLE_CALLBACKS_IMAGE_KEY, imageURL);

        String plotSynopsis = mCurrentMoviePoster.getPlotSynopsis();
        outState.putString(LIFECYCLE_CALLBACKS_PLOT_KEY, plotSynopsis);

        String userRating = mCurrentMoviePoster.getUserRating();
        outState.putString(LIFECYCLE_CALLBACKS_RATING_KEY, userRating);

        String releaseDate = mCurrentMoviePoster.getReleaseDate();
        outState.putString(LIFECYCLE_CALLBACKS_DATE_KEY, releaseDate);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        mOriginalTitleTV = (TextView) findViewById(R.id.originalTitle);

        mPlotSynopsisTV = (TextView) findViewById(R.id.plotSynopsis);

        mUserRatingTV = (TextView) findViewById(R.id.userRating);

        mReleaseDateTV = (TextView) findViewById(R.id.releaseDate);

        ImageView imageView = (ImageView) findViewById(R.id.detailsImage);

        if(savedInstanceState != null) {
            if (savedInstanceState.containsKey(LIFECYCLE_CALLBACKS_TITLE_KEY)) {
                String titleKey = savedInstanceState
                        .getString(LIFECYCLE_CALLBACKS_TITLE_KEY);
                //mCurrentMoviePoster.setOriginalTitle(titleKey);
                mOriginalTitleTV.setText(titleKey);
            }
            if(savedInstanceState.containsKey(LIFECYCLE_CALLBACKS_IMAGE_KEY)){
                String imageKey = savedInstanceState
                        .getString(LIFECYCLE_CALLBACKS_IMAGE_KEY);
                String path = getString(R.string.url);
                Picasso.with(this).load(path + imageKey).into(imageView);
            }
            if(savedInstanceState.containsKey(LIFECYCLE_CALLBACKS_PLOT_KEY)){
                String plotSynopsisKey = savedInstanceState
                        .getString(LIFECYCLE_CALLBACKS_PLOT_KEY);
                //mCurrentMoviePoster.setPlotSynopsis(plotSynopsisKey);
                mPlotSynopsisTV.setText(plotSynopsisKey);
            }
            if(savedInstanceState.containsKey(LIFECYCLE_CALLBACKS_RATING_KEY)){
                String ratingKey = savedInstanceState
                        .getString(LIFECYCLE_CALLBACKS_RATING_KEY);
                //mCurrentMoviePoster.setUserRating(ratingKey);
                mUserRatingTV.setText(ratingKey);
            }
            if(savedInstanceState.containsKey(LIFECYCLE_CALLBACKS_DATE_KEY)){
                String releaseDateKey = savedInstanceState
                        .getString(LIFECYCLE_CALLBACKS_DATE_KEY);
                //mCurrentMoviePoster.setReleaseDate(releaseDateKey);
                mReleaseDateTV.setText(releaseDateKey);
            }
        }

        initViews();

        mDatabase = Database.getInstance(getApplicationContext());

        if (savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_MOVIE_ID)) {
            mMovieId = savedInstanceState.getInt(INSTANCE_MOVIE_ID, DEFAULT_MOVIE_ID);
        }

        Intent intent = getIntent();

        int imageNumber = intent.getIntExtra(POSITION, DEFAULT_POSITION);

        if (imageNumber == DEFAULT_POSITION) {
            Toast.makeText(this, R.string.error, Toast.LENGTH_LONG).show();
        }else{

            mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

            mFavoritesCheckBox = (CheckBox) findViewById(R.id.checkbox);

            mCurrentMoviePoster = MovieAdapter.mMovieData.get(imageNumber);

            Uri.Builder trailerBuilder = new Uri.Builder();
            trailerBuilder.scheme("http")
                    .authority("api.themoviedb.org")
                    .appendPath("3")
                    .appendPath("movie")
                    .appendPath(mCurrentMoviePoster.getId())
                    .appendPath("videos")
                    .appendQueryParameter("api_key", "Insert API KEY Here");

            mMovieTrailerUrl = trailerBuilder.build().toString();

            populateUi();

            String path = getString(R.string.url);

            Picasso.with(this).load(path + mCurrentMoviePoster.getImageURL()).into(imageView);

            mRecyclerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    launchTrailer(v);
                }
            });

            getLoaderManager().initLoader(R.layout.activity_movie_details, new Bundle(), new LoaderManager.LoaderCallbacks<List<Trailer>>(){

                @Override
                public Loader<List<Trailer>> onCreateLoader(int id, Bundle args) {
                    return new TrailerLoader(MovieDetails.this, mMovieTrailerUrl);
                }

                @Override
                public void onLoadFinished(Loader<List<Trailer>> loader, List<Trailer> data) {
                    mMovieTrailerAdapter= new MovieTrailerAdapter(MovieDetails.this, data);
                    mRecyclerView.setAdapter(mMovieTrailerAdapter);
                }

                @Override
                public void onLoaderReset(Loader<List<Trailer>> loader) {
                }
            });
        }
    }


    @Override
    public Loader<List<Trailer>> onCreateLoader(int id, Bundle args) {
        return new TrailerLoader(this, mMovieTrailerUrl);
    }

    @Override
    public void onLoadFinished(Loader<List<Trailer>> loader, List<Trailer> data) {
        mMovieTrailerAdapter= new MovieTrailerAdapter(this, data);
        mRecyclerView.setAdapter(mMovieTrailerAdapter);
    }

    @Override
    public void onLoaderReset(Loader<List<Trailer>> loader) {
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

    private void initViews(){
        mFavoritesCheckBox = (CheckBox) findViewById(R.id.checkbox);
        mFavoritesCheckBox.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                onSaveCheckBoxClicked();
            }
        });
    }

    public void onSaveCheckBoxClicked(){
        boolean isChecked = mFavoritesCheckBox.isChecked();

        MoviePoster moviePoster = new MoviePoster(mCurrentMoviePoster.getId(), mCurrentMoviePoster.getOriginalTitle(),
                mCurrentMoviePoster.getImageURL(), mCurrentMoviePoster.getPlotSynopsis(),
                mCurrentMoviePoster.getUserRating(), mCurrentMoviePoster.getReleaseDate());

        mDatabase.movieDao().insertMovie(moviePoster);

        finish();
    }

    public void launchTrailer(View v){
        Intent youtubeIntent = new Intent();
        youtubeIntent.setAction(Intent.ACTION_VIEW);
        startActivity(youtubeIntent);
    }
}
