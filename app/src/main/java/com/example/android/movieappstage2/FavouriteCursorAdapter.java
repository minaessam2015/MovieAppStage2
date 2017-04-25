package com.example.android.movieappstage2;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.movieappstage2.Data.MovieAppContract;
import com.example.android.movieappstage2.NetworkUtils.NetworkUtils;
import com.squareup.picasso.Picasso;

/**
 * Created by mina essam on 25-Apr-17.
 */

public class FavouriteCursorAdapter extends RecyclerView.Adapter<FavouriteCursorAdapter.ViewHolder>
{
    private Context mContext;
    private Cursor mCursor=null;
    private OnClickListener  mListener;

    interface OnClickListener{
        void onClick(int position);
    }

    public FavouriteCursorAdapter(Context context, OnClickListener listener ){
        mContext=context;
        mListener=listener;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(mContext).inflate(R.layout.details_first_row,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if(mCursor.moveToPosition(position)){

            holder.title.setText(mCursor.getString(mCursor.getColumnIndexOrThrow(MovieAppContract.MoviesTable.MOVIE_COLUMN_TITLE)));
            holder.overview.setText(mCursor.getString(mCursor.getColumnIndexOrThrow(MovieAppContract.MoviesTable.MOVIE_COLUMN_OVERVIEW)));
            holder.vote.setText(String .valueOf(mCursor.getFloat(mCursor.getColumnIndexOrThrow(MovieAppContract.MoviesTable.MOVIE_COLUMN_VOTE))));
            holder.date.setText(mCursor.getString(mCursor.getColumnIndexOrThrow(MovieAppContract.MoviesTable.MOVIE_COLUMN_DATE)));
            Picasso.with(mContext).load(NetworkUtils.makePosterPathURL(
                    mCursor.getString(mCursor.getColumnIndexOrThrow(MovieAppContract.MoviesTable.MOVIE_COLUMN_POSTER_PATH))).toString()).
                    error(R.drawable.icon_cloud_error).into(holder.imageView);
            int id =mCursor.getInt(mCursor.getColumnIndexOrThrow(MovieAppContract.MoviesTable.MOVIE_COLUMN_ID));
            Log.d("onBindViewHolder "," "+" "+id);
            holder.itemView.setTag(id);

        }


    }


    @Override
    public int getItemCount() {
        if(mCursor==null){
            Log.d("getItemCount "," 0");
            return 0;
        }else {
            Log.d("getItemCount "," "+mCursor.getCount());
            return mCursor.getCount();
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final ImageView imageView;
        private final TextView title;
        private final TextView date;
        private final TextView overview;
        private final TextView vote;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView=(ImageView)itemView.findViewById(R.id.movie_image_onDetails);
            title=(TextView)itemView.findViewById(R.id.title_text_onDetails);
            date=(TextView)itemView.findViewById(R.id.date_text_view);
            overview=(TextView)itemView.findViewById(R.id.overview_text);
            vote=(TextView)itemView.findViewById(R.id.rating_text_onDetails);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            mListener.onClick(getAdapterPosition());
        }
    }

    public Cursor swapCursor(Cursor cursor){
        if(mCursor==cursor){
            return null;
        }
        Cursor tmp=mCursor;
        mCursor=cursor;
        notifyDataSetChanged();
        return tmp;
    }

}
