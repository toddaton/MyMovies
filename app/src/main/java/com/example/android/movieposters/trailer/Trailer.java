package com.example.android.movieposters.trailer;

public class Trailer {

    private int mIcon;

    private String mTrailerNumber;

    private String mUrl;

    public Trailer(int icon, String trailerNumber, String url){
        mIcon = icon;
        mTrailerNumber = trailerNumber;
        mUrl = url;
    }

    public int getIcon(){
        return mIcon;
    }

    public String getTrailerNumber(){
        return mTrailerNumber;
    }

    public String getUrl(){
        return mUrl;
    }
}
