package com.example.android.movieposters.userReview;

public class UserReview {

    private String mId;

    private String mContent;

    public UserReview(String id, String content){
        mId = id;
        mContent = content;
    }

    public String getUserReviewId(){
        return mId;
    }

    public String getUserReviewContent(){
        return mContent;
    }
}
