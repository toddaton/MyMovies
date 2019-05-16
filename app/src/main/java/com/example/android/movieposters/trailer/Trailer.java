package com.example.android.movieposters.trailer;

public class Trailer {

    private int mIcon;

    private String mTrailerNumber;

    private String mYoutubeUrl;

    public Trailer(int icon, String trailerNumber, String url){
        mIcon = icon;
        mTrailerNumber = trailerNumber;
        mYoutubeUrl = url;
    }

    public int getIcon(){
        return mIcon;
    }

    public String getTrailerNumber(){
        return mTrailerNumber;
    }

    public String getYoutubeUrl(){
        return mYoutubeUrl;
    }
}
