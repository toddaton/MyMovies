package com.example.android.movieposters;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.android.movieposters.moviePoster.MovieAdapter;
import com.example.android.movieposters.moviePoster.MoviePoster;
import com.example.android.movieposters.moviePoster.MoviePosterLoader;

import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<MoviePoster>> {

    private static final int MOVIE_POSTER_LOADER_ID = 1;

    public static String mPopularUrl;

    public static String mTopRatedUrl;

    public static String mSettingsUrl;

    private GridView mGridView;

    public MovieAdapter mMovieAdapter;

    private Database mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Uri.Builder popularBuilder = new Uri.Builder();
        popularBuilder.scheme("http")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("movie")
                .appendPath("popular")
                .appendQueryParameter("api_key", Insert API Key here!);

        mPopularUrl = popularBuilder.build().toString();

        Uri.Builder topRatedBuilder = new Uri.Builder();
        topRatedBuilder.scheme("http")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("movie")
                .appendPath("top_rated")
                .appendQueryParameter("api_key", Insert API Key here!);

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
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMovieAdapter.setMovies(mDatabase.movieDao().loadAllTasks());
    }

    private void launchMovieDetails(int position){
        Intent intent = new Intent(this, com.example.android.movieposters.MovieDetails.class);

        intent.putExtra(com.example.android.movieposters.MovieDetails.POSITION, position);

        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int clickedOption = item.getItemId();
        if(clickedOption == R.id.top_rated){
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
        return new MoviePosterLoader(this, mPopularUrl);
    }

    @Override
    public void onLoadFinished(Loader<List<MoviePoster>> loader, List<MoviePoster> data) {
        mMovieAdapter = new MovieAdapter(this, data);
        mGridView.setAdapter(mMovieAdapter);
    }

    @Override
    public void onLoaderReset(Loader<List<MoviePoster>> loader) {
    }

}


