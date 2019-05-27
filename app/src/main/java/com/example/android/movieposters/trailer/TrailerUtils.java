package com.example.android.movieposters.trailer;

import android.text.TextUtils;
import android.util.Log;

import com.example.android.movieposters.R;
import com.example.android.movieposters.moviePoster.ApiUtils;
import com.example.android.movieposters.trailer.Trailer;

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

public final class TrailerUtils {

    private static final String LOG_TAG = TrailerUtils.class.getSimpleName();

    private static URL mUrl;

    private TrailerUtils(){
    }

    public static List<Trailer> fetchTrailerData(String requestUrl){
        //URL url = createUrl(requestUrl);
        mUrl = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(mUrl);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        List<Trailer> trailers = extractFeatureFromJson(jsonResponse);

        return trailers;
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
            Log.e(LOG_TAG, "Problem retrieving the Trailer JSON results.", e);
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

    private static List<Trailer> extractFeatureFromJson(String trailerJSON) {

        if (TextUtils.isEmpty(trailerJSON)) {
            return null;
        }

        List<Trailer> trailers = new ArrayList<>();

        try {

            JSONObject baseJsonObject = new JSONObject(trailerJSON);
            JSONArray resultsJsonArray = baseJsonObject.getJSONArray("results");

            for(int i = 0; i < resultsJsonArray.length(); i++){
                JSONObject currentTrailer = resultsJsonArray.getJSONObject(i);

                String key = currentTrailer.optString("key");

                String youtubeUrl = "https://www.youtube.com/watch?v=" + key;

                Trailer trailer = new Trailer(R.drawable.play_icon, "Video "+i, youtubeUrl);

                trailers.add(trailer);
            }

        } catch (JSONException e) {
            Log.e("TrailerUtils", "Problem parsing trailer results", e);
        }
        return trailers;
    }
}

