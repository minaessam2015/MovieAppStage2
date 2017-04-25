package com.example.android.movieappstage2;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.movieappstage2.NetworkUtils.NetworkUtils;
import com.example.android.movieappstage2.Utils.Movie;
import com.example.android.movieappstage2.Utils.Review;
import com.example.android.movieappstage2.Utils.Trailer;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mina essam on 24-Apr-17.
 */

public class DetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<Trailer> trailers=new ArrayList<>();
    private List<Review> reviews=new ArrayList<>();
    private Movie mMovie;
    private final int firstRow=0;
    private final int trailerType=1;
    private final int reviewType=2;
    private  int trailerSize;
    private  int reviewSize;
    private OnClickListener mListener;

    interface OnClickListener{
        void onClick(int position);
    }


    public void setTrailers(List<Trailer> trailers) {
        this.trailers = trailers;
        trailerSize=trailers.size();
        Log.d("trailerSize",String.valueOf(trailerSize));
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
        reviewSize=reviews.size();
        Log.d("reviewSize",String.valueOf(reviewSize));
    }

    public DetailsAdapter(Context context, Movie movie,OnClickListener listener){
        mContext=context;
        mMovie=movie;
        mListener=listener;
    }

    public class FirstItemViewHolder extends RecyclerView.ViewHolder{
        private final ImageView imageView;
        private final TextView title;
        private final TextView date;
        private final TextView overview;
        private final TextView vote;

        public FirstItemViewHolder(View itemView) {
            super(itemView);
            imageView=(ImageView)itemView.findViewById(R.id.movie_image_onDetails);
            title=(TextView)itemView.findViewById(R.id.title_text_onDetails);
            date=(TextView)itemView.findViewById(R.id.date_text_view);
            overview=(TextView)itemView.findViewById(R.id.overview_text);
            vote=(TextView)itemView.findViewById(R.id.rating_text_onDetails);
        }
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final TextView name,quality;


        public TrailerViewHolder(View itemView) {
            super(itemView);
            name=(TextView)itemView.findViewById(R.id.trailer_name_text);
            quality=(TextView)itemView.findViewById(R.id.trailer_quality_text);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mListener.onClick(getAdapterPosition());
        }
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder{
        private final TextView author,content;
        public ReviewViewHolder(View itemView) {
            super(itemView);
            author=(TextView)itemView.findViewById(R.id.review_author_text);
            content=(TextView)itemView.findViewById(R.id.review_text);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        Log.d("onCreateViewHolder",String.valueOf(viewType));
        switch (viewType){
            case firstRow:
                view= LayoutInflater.from(mContext).inflate(R.layout.details_first_row,parent,false);
                FirstItemViewHolder holder=new FirstItemViewHolder(view);
                return holder;
            case trailerType:
                view=LayoutInflater.from(mContext).inflate(R.layout.details_trailers_rows,parent,false);
                TrailerViewHolder holder1=new TrailerViewHolder(view);
                return holder1;
            case reviewType:
                view=LayoutInflater.from(mContext).inflate(R.layout.details_reviews_rows,parent,false);
                ReviewViewHolder holder2=new ReviewViewHolder(view);
                return holder2;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int type=getItemViewType(position);
        Log.d("onBindViewHolder",String.valueOf(type));
        switch (type){
            case firstRow:
                ((FirstItemViewHolder)holder).title.setText(mMovie.getTitle());
                ((FirstItemViewHolder)holder).overview.setText(mMovie.getDescription());
                ((FirstItemViewHolder)holder).vote.setText(String .valueOf(mMovie.getVote()));
                ((FirstItemViewHolder)holder).date.setText(mMovie.getDate());
                Picasso.with(mContext).load((NetworkUtils.makePosterPathURL(mMovie.getPosterPath())).toString())
                        .error(R.drawable.icon_cloud_error).into(((FirstItemViewHolder)holder).imageView);
                return;

            case trailerType:
                ((TrailerViewHolder)holder).name.setText(trailers.get(position-1).getName());
                ((TrailerViewHolder)holder).quality.setText(trailers.get(position-1).getQuality()+"P");
                return;

            case reviewType:
                ((ReviewViewHolder)holder).author.setText(reviews.get((position-trailers.size())-1).getAuthor());
                ((ReviewViewHolder)holder).content.setText(reviews.get((position-trailers.size())-1).getContent());
                return;

        }
    }

    @Override
    public int getItemCount() {
        Log.d("getItemCountTrailer",String .valueOf((trailerSize)));
        Log.d("getItemCountReview",String .valueOf((reviewSize)));
        int size=1+trailerSize+reviewSize;
        return size;

    }

    @Override
    public int getItemViewType(int position) {

        if(position==0){
            return firstRow;
        }
        else if(position<=trailers.size()){
            return trailerType;
        }
        else {return reviewType;}

    }

}
