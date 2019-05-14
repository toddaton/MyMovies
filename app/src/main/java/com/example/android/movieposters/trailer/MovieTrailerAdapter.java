package com.example.android.movieposters.trailer;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.movieposters.R;

import java.util.List;

public class MovieTrailerAdapter extends RecyclerView.Adapter<MovieTrailerAdapter.MovieTrailerViewHolder> {

    private Context mContext;

    private List<Trailer> mListTrailers;

    public MovieTrailerAdapter(Context context, List<Trailer> listMoviePosters){
        mContext = context;
        mListTrailers = listMoviePosters;
    }

    @Override
    public MovieTrailerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View movieTrailerView = LayoutInflater.from(mContext).inflate(R.layout.trailer, null);
        MovieTrailerViewHolder viewHolder = new MovieTrailerViewHolder(movieTrailerView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MovieTrailerViewHolder holder, int position) {
        Trailer trailer = mListTrailers.get(position);

        holder.mTextView.setText(R.string.trailer + position);

        holder.mImageView.setImageResource(R.drawable.ic_launcher_foreground);
    }

    @Override
    public int getItemCount() {
        return mListTrailers.size();
    }

    class MovieTrailerViewHolder extends RecyclerView.ViewHolder {

        ImageView mImageView;

        TextView mTextView;

        public MovieTrailerViewHolder(View itemView){
            super(itemView);

            mImageView = itemView.findViewById(R.id.trailer_icon);

            mTextView = itemView.findViewById(R.id.trailer_number);
        }

    }
}
