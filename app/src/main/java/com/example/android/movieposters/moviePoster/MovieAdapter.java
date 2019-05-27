package com.example.android.movieposters.moviePoster;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.android.movieposters.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAdapter extends BaseAdapter {

    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();

    private Context mContext;

    public static List<MoviePoster> mMovieData;

    private List<MoviePoster> mSetMovies;

    private static LayoutInflater inflater = null;

    public MovieAdapter(){

    }

    public MovieAdapter(Context c, List<MoviePoster> movieData) {
        mContext = c;
        mMovieData = movieData;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        if(mMovieData == null){
            return 0;
        }else{
            return mMovieData.size();
        }
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return 0;
    }

    public void setMovies(List<MoviePoster> moviePosters){
        mMovieData = moviePosters;
        notifyDataSetChanged();
    }

    public class Holder{
        ImageView mainImageView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Holder holder = new Holder();

        View imageView;

        imageView = inflater.inflate(R.layout.main_image, null);

        holder.mainImageView = (ImageView) imageView.findViewById(R.id.image);

        String path = mContext.getString(R.string.url);

        Picasso.with(mContext).load(path + mMovieData.get(position).getImageURL()).into(holder.mainImageView);

        return imageView;
    }

}
