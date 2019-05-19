package com.example.android.movieposters;

import android.app.LoaderManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.android.movieposters.database.Database;
import com.example.android.movieposters.database.MoviePosterViewModel;
import com.example.android.movieposters.moviePoster.MovieAdapter;
import com.example.android.movieposters.moviePoster.MoviePoster;
import com.example.android.movieposters.moviePoster.MoviePosterLoader;
import com.example.android.movieposters.trailer.MovieTrailerAdapter;

import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<MoviePoster>> {

    private static final String LIFECYCLE_CALLBACKS_TEXT_KEY = "callbacks";

    private static final String ON_SAVE_INSTANCE_STATE = "onSaveInstanceState";

    public static final int NEW_MOVIE_ACTIVITY_REQUEST_CODE = 1;

    private static final String URL_BOOLEAN = "urlBoolean";

    private static final int MOVIE_POSTER_LOADER_ID = 1;

    public static String mPopularUrl;

    public static String mTopRatedUrl;

    public static String mSettingsUrl;

    private GridView mGridView;

    public MovieAdapter mMovieAdapter;

    private Database mDatabase;

    private boolean mUrlBool;

    private MoviePosterViewModel mMoviePosterViewModel;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(URL_BOOLEAN, mUrlBool);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState != null) {
            if (savedInstanceState.containsKey(URL_BOOLEAN)) {
                Boolean urlKey = savedInstanceState
                        .getBoolean(URL_BOOLEAN);
                if (!urlKey) {
                    getLoaderManager().initLoader(R.layout.activity_main, new Bundle(), new LoaderManager.LoaderCallbacks<List<MoviePoster>>(){

                        @Override
                        public Loader<List<MoviePoster>> onCreateLoader(int id, Bundle args) {
                            return new MoviePosterLoader(MainActivity.this, mTopRatedUrl);
                        }

                        @Override
                        public void onLoadFinished(Loader<List<MoviePoster>> loader, List<MoviePoster> data) {
                            mMovieAdapter = new MovieAdapter(MainActivity.this, data);
                            mGridView.setAdapter(mMovieAdapter);
                        }

                        @Override
                        public void onLoaderReset(Loader<List<MoviePoster>> loader) {
                        }
                    });
                }
                if(urlKey){
                    getLoaderManager().initLoader(R.id.detailsImage, new Bundle(), new LoaderManager.LoaderCallbacks<List<MoviePoster>>(){
                        @Override
                        public Loader<List<MoviePoster>> onCreateLoader(int id, Bundle args) {
                            return new MoviePosterLoader(MainActivity.this, mPopularUrl);
                        }

                        @Override
                        public void onLoadFinished(Loader<List<MoviePoster>> loader, List<MoviePoster> data) {
                            mMovieAdapter = new MovieAdapter(MainActivity.this, data);
                            mGridView.setAdapter(mMovieAdapter);
                        }

                        @Override
                        public void onLoaderReset(Loader<List<MoviePoster>> loader) {
                        }
                    });
                }
            }
        }

        Uri.Builder popularBuilder = new Uri.Builder();
        popularBuilder.scheme("http")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("movie")
                .appendPath("popular")
                .appendQueryParameter("api_key", "Insert API KEY Here");

        mPopularUrl = popularBuilder.build().toString();

        Uri.Builder topRatedBuilder = new Uri.Builder();
        topRatedBuilder.scheme("http")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("movie")
                .appendPath("top_rated")
                .appendQueryParameter("api_key", "Insert API KEY Here");

        mTopRatedUrl = topRatedBuilder.build().toString();

        mSettingsUrl = mPopularUrl;

        mGridView = (GridView) findViewById(R.id.gridView);

        mGridView.setVisibility(View.VISIBLE);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                launchMovieDetails(position);
            }
        });

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(MOVIE_POSTER_LOADER_ID, null, this);
        }

        mDatabase = Database.getInstance(getApplicationContext());

        mMoviePosterViewModel = ViewModelProviders.of(this).get(MoviePosterViewModel.class);

        mMoviePosterViewModel.getFavoriteMovies().observe(this, new Observer<List<MoviePoster>>() {
            @Override
            public void onChanged(@Nullable final List<MoviePoster> moviePosters) {
                mMovieAdapter.setMovies(moviePosters);
            }
        });
    }

    private void launchMovieDetails(int position){
        Intent intent = new Intent(this, com.example.android.movieposters.MovieDetails.class);

        intent.putExtra(com.example.android.movieposters.MovieDetails.POSITION, position);

        startActivity(intent);
    }

    /**private void launchUserReviewDetails(int position){
        Intent intent = new Intent(this, UserReviewDetails.class);

        intent.putExtra(UserReviewDetails.USER_POSITION, position);

        startActivity(intent);
    }**/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int clickedOption = item.getItemId();
        if(clickedOption == R.id.top_rated){
            mUrlBool = false;
            getLoaderManager().initLoader(R.layout.activity_main, new Bundle(), new LoaderManager.LoaderCallbacks<List<MoviePoster>>(){

                @Override
                public Loader<List<MoviePoster>> onCreateLoader(int id, Bundle args) {
                    return new MoviePosterLoader(MainActivity.this, mTopRatedUrl);
                }

                @Override
                public void onLoadFinished(Loader<List<MoviePoster>> loader, List<MoviePoster> data) {
                    mMovieAdapter = new MovieAdapter(MainActivity.this, data);
                    mGridView.setAdapter(mMovieAdapter);
                }

                @Override
                public void onLoaderReset(Loader<List<MoviePoster>> loader) {
                }
            });

            return true;

        }else if(clickedOption == R.id.popular){
            mUrlBool = true;
            getLoaderManager().initLoader(R.id.detailsImage, new Bundle(), new LoaderManager.LoaderCallbacks<List<MoviePoster>>(){
                @Override
                public Loader<List<MoviePoster>> onCreateLoader(int id, Bundle args) {
                    return new MoviePosterLoader(MainActivity.this, mPopularUrl);
                }

                @Override
                public void onLoadFinished(Loader<List<MoviePoster>> loader, List<MoviePoster> data) {
                    mMovieAdapter = new MovieAdapter(MainActivity.this, data);
                    mGridView.setAdapter(mMovieAdapter);
                }

                @Override
                public void onLoaderReset(Loader<List<MoviePoster>> loader) {
                }
            });

            return true;

        }else if(clickedOption == R.id.favorites){

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<List<MoviePoster>> onCreateLoader(int id, Bundle args) {
        if(mUrlBool){
            return new MoviePosterLoader(this, mPopularUrl);
        }
        return new MoviePosterLoader(this, mSettingsUrl);
    }

    @Override
    public void onLoadFinished(Loader<List<MoviePoster>> loader, List<MoviePoster> data) {
        mMovieAdapter = new MovieAdapter(this, data);
        mGridView.setAdapter(mMovieAdapter);
    }

    @Override
    public void onLoaderReset(Loader<List<MoviePoster>> loader) {
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == NEW_MOVIE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
            MoviePoster moviePoster = new MoviePoster(data.getStringExtra(MovieDetails.EXTRA_ID),
                    data.getStringExtra(MovieDetails.EXTRA_ORIGINAL_TITLE), data.getStringExtra(MovieDetails.EXTRA_IMAGE_URL),
                    data.getStringExtra(MovieDetails.EXTRA_PLOT_SYNOPSIS), data.getStringExtra(MovieDetails.EXTRA_USER_RATING),
                    data.getStringExtra(MovieDetails.EXTRA_RELEASE_DATE));
            mMoviePosterViewModel.insert(moviePoster);
        }
    }

}


