package com.example.android.movieposters.userReview;

import android.text.TextUtils;
import android.util.Log;

import com.example.android.movieposters.R;
import com.example.android.movieposters.moviePoster.ApiUtils;
import com.example.android.movieposters.trailer.Trailer;
import com.example.android.movieposters.trailer.TrailerUtils;

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

public final class UserReviewUtils {

    private static final String LOG_TAG = UserReviewUtils.class.getSimpleName();

    private static URL mUrl;

    private UserReviewUtils(){
    }

    public static List<UserReview> fetchUserReviewData(String requestUrl){
        //URL url = createUrl(requestUrl);
        mUrl = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(mUrl);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        List<UserReview> userReviews = extractFeatureFromJson(jsonResponse);

        return userReviews;
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
            Log.e(LOG_TAG, "Problem retrieving the User Review JSON results.", e);
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

    private static List<UserReview> extractFeatureFromJson(String userReviewJSON) {

        if (TextUtils.isEmpty(userReviewJSON)) {
            return null;
        }

        List<UserReview> userReviews = new ArrayList<>();

        try {

            JSONObject baseJsonObject = new JSONObject(userReviewJSON);
            JSONArray resultsJsonArray = baseJsonObject.getJSONArray("results");

            for(int i = 0; i < resultsJsonArray.length(); i++){
                JSONObject currentUserReview = resultsJsonArray.getJSONObject(i);

                String id = currentUserReview.optString("id");

                String content = currentUserReview.optString("content");

                UserReview userReview = new UserReview(id, content);

                userReviews.add(userReview);
            }

        } catch (JSONException e) {
            Log.e("UserReviewUtils", "Problem parsing user review results", e);
        }
        return userReviews;
    }
}

