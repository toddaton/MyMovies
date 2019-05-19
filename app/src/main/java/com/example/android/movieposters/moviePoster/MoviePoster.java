package com.example.android.movieposters.moviePoster;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.media.Image;
import android.net.Uri;
import android.support.annotation.NonNull;

import java.net.URI;
import java.net.URL;

@Entity(tableName = "movieposter")
public class MoviePoster {

    @PrimaryKey
    @NonNull
    private String mId;

    private String mOriginalTitle;

    private String mImageURL;

    private String mPlotSynopsis;

    private String mUserRating;

    private String mReleaseDate;

    public MoviePoster(String id, String originalTitle, String imageURL, String plotSynopsis, String userRating, String releaseDate){
        mId = id;
        mOriginalTitle = originalTitle;
        mImageURL = imageURL;
        mPlotSynopsis = plotSynopsis;
        mUserRating = userRating;
        mReleaseDate = releaseDate;
    }

    public String getId(){
        return mId;
    }

    public String getOriginalTitle(){
        return mOriginalTitle;
    }

    public String getImageURL(){
        return mImageURL;
    }

    public String getPlotSynopsis(){
        return mPlotSynopsis;
    }

    public String getUserRating(){
        return mUserRating;
    }

    public String getReleaseDate(){
        return mReleaseDate;
    }

    public void setMoviePosterId(String id){
        mId = id;
    }

    public void setOriginalTitle(String originalTitle){
        mOriginalTitle = originalTitle;
    }

    public void setImageURL(String imageURL){
        mImageURL = imageURL;
    }

    public void setPlotSynopsis(String plotSynopsis){
        mPlotSynopsis = plotSynopsis;
    }

    public void setUserRating(String userRating){
        mUserRating = userRating;
    }

    public void setReleaseDate(String releaseDate){
        mReleaseDate = releaseDate;
    }

}
