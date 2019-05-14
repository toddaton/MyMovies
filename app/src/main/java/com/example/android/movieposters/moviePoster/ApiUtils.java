package com.example.android.movieposters.moviePoster;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.net.URL;

public final class ApiUtils {

    private static final String LOG_TAG = ApiUtils.class.getSimpleName();

    private static URL mUrl;

    private ApiUtils(){
    }

    public static List<MoviePoster> fetchMoviePosterData(String requestUrl){
        //URL url = createUrl(requestUrl);
        mUrl = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(mUrl);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        List<MoviePoster> moviePosters = extractFeatureFromJson(jsonResponse);

        return moviePosters;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the Movie Poster JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<MoviePoster> extractFeatureFromJson(String moviePosterJSON) {

        if (TextUtils.isEmpty(moviePosterJSON)) {
            return null;
        }

        List<MoviePoster> moviePosters = new ArrayList<>();

        try {
            JSONObject baseJsonObject = new JSONObject(moviePosterJSON);
            JSONArray resultsJsonArray = baseJsonObject.getJSONArray("results");


            for (int i = 0; i < resultsJsonArray.length(); i++) {
                JSONObject currentMovie = resultsJsonArray.getJSONObject(i);

                String id = currentMovie.optString("id");

                String movieTitle = currentMovie.optString("original_title");
                String moviePosterImage = currentMovie.optString("poster_path");
                String plotSynopsis = currentMovie.optString("overview");
                String userRating = currentMovie.optString("vote_average");
                String releaseDate = currentMovie.optString("release_date");

                MoviePoster moviePoster = new MoviePoster(id, movieTitle, moviePosterImage, plotSynopsis, userRating, releaseDate);

                moviePosters.add(moviePoster);
            }
        } catch (JSONException e) {
            Log.e("ApiUtils", "Problem parsing JSON results", e);
        }
        return moviePosters;
    }
}

