package com.example.android.movieposters;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

    private MovieTrailerAdapter mMovieTrailerAdapter;

    private RecyclerView mRecyclerView;

    public static CheckBox mFavoritesCheckBox;

    private Database mDatabase;

    private static final int DEFAULT_MOVIE_ID = -1;

    public static final String INSTANCE_MOVIE_ID = "instanceMovieId";

    private int mMovieId = DEFAULT_MOVIE_ID;

    public static final String EXTRA_MOVIE_ID = "extraMovieId";


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(INSTANCE_MOVIE_ID, mMovieId);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        initViews();

        mDatabase = Database.getInstance(getApplicationContext());

        if (savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_MOVIE_ID)) {
            mMovieId = savedInstanceState.getInt(INSTANCE_MOVIE_ID, DEFAULT_MOVIE_ID);
        }

        Intent intent = getIntent();

        int imageNumber = intent.getIntExtra(POSITION, DEFAULT_POSITION);

        if (imageNumber == DEFAULT_POSITION) {
            Toast.makeText(this, R.string.error, Toast.LENGTH_LONG).show();
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mFavoritesCheckBox = (CheckBox) findViewById(R.id.checkbox);

        mCurrentMoviePoster = MovieAdapter.mMovieData.get(imageNumber);

        ImageView imageView = (ImageView) findViewById(R.id.detailsImage);

        Uri.Builder trailerBuilder = new Uri.Builder();
        trailerBuilder.scheme("https")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("movie")
                .appendPath(mCurrentMoviePoster.getId())
                .appendQueryParameter("api_key", Insert API Key here!);

        mMovieTrailerUrl = trailerBuilder.build().toString();

        Uri.Builder reviewBuilder = new Uri.Builder();
        reviewBuilder.scheme("https")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("review")
                .appendPath(mCurrentMoviePoster.getId())
                .appendQueryParameter("api_key", Insert API Key here!);

        populateUi();

        String path = getString(R.string.url);

        Picasso.with(this).load(path + mCurrentMoviePoster.getImageURL()).into(imageView);

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
}
