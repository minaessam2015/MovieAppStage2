package com.example.android.movieappstage2;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.movieappstage2.NetworkUtils.NetworkUtils;
import com.example.android.movieappstage2.Utils.Movie;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mina essam on 23-Apr-17.
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {
    private Context mContext;
    private  List<Movie> movies=new ArrayList<>();
    private OnRecyclerListener mListener;

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

    interface OnRecyclerListener{
        void onClickListener(int position);
    }
    public HomeAdapter(Context context, OnRecyclerListener listener){
        mContext=context;
        mListener=listener;
    }
    public HomeAdapter(Context context, List<Movie> movies, OnRecyclerListener listener){
        mContext=context;
        this.movies=movies;
        mListener=listener;
    }

     class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final ImageView imageView;

        public ViewHolder(View itemView)  {
            super(itemView);
            imageView=(ImageView)itemView.findViewById(R.id.movie_image_view);
            imageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position=getAdapterPosition();
            mListener.onClickListener(position);
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.home_cell_item,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        URL url= NetworkUtils.makePosterPathURL(movies.get(position).getPosterPath());
        Picasso.with(mContext).load(url.toString()).error(R.drawable.icon_cloud_error).into(holder.imageView);

    }



    @Override
    public int getItemCount() {
        return movies.size();
    }
}
