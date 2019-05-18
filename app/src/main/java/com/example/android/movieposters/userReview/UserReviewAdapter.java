package com.example.android.movieposters.userReview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.movieposters.R;
import com.example.android.movieposters.trailer.MovieTrailerAdapter;
import com.example.android.movieposters.trailer.Trailer;

import java.util.List;

public class UserReviewAdapter extends RecyclerView.Adapter<UserReviewAdapter.UserReviewViewHolder> {

    private Context mContext;

    private List<UserReview> mListUserReviews;

    public UserReviewAdapter(Context context, List<UserReview> listUserReviews){
        mContext = context;
        mListUserReviews = listUserReviews;
    }

    @Override
    public UserReviewViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View userReviewView = LayoutInflater.from(mContext).inflate(R.layout.user_review, null);
        UserReviewViewHolder viewHolder = new UserReviewViewHolder(userReviewView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(UserReviewViewHolder holder, int position) {
        UserReview userReview = mListUserReviews.get(position);

        holder.mTextView.setText(userReview.getUserReviewContent());
    }

    @Override
    public int getItemCount() {
        return mListUserReviews.size();
    }

    class UserReviewViewHolder extends RecyclerView.ViewHolder {

        TextView mTextView;

        public UserReviewViewHolder(View itemView){
            super(itemView);

            mTextView = itemView.findViewById(R.id.user_reviewTV);
        }

    }
}
