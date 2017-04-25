package com.example.android.movieappstage2;

import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.android.movieappstage2.Data.MovieAppContract;

public class FavouriteActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>,FavouriteCursorAdapter.OnClickListener{

    private Cursor mCursor=null;
    private RecyclerView recyclerView;
    private FavouriteCursorAdapter adapter;
    private TextView noFavourites;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        noFavourites=(TextView)findViewById(R.id.no_favourites_text);

        recyclerView=(RecyclerView)findViewById(R.id.favourite_recycle);
        adapter=new FavouriteCursorAdapter(this,this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        getSupportLoaderManager().initLoader(0,null,this);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                String id=Integer.toString((int)viewHolder.itemView.getTag());
                Log.d("onSwiped ",id);
                Uri uri= Uri.withAppendedPath(MovieAppContract.MoviesTable.CONTENT_URI,id);

                getContentResolver().delete(uri,null,null);
                getSupportLoaderManager().restartLoader(0,null,FavouriteActivity.this);

            }
        }).attachToRecyclerView(recyclerView);

        noFavourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(this) {
            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                forceLoad();
            }

            @Override
            public void deliverResult(Cursor data) {
                mCursor=data;
                super.deliverResult(data);
            }

            @Override
            public Cursor loadInBackground() {
                Cursor cursor=getContentResolver().query(MovieAppContract.MoviesTable.CONTENT_URI,null,null
                ,null, MovieAppContract.MoviesTable._ID+" DESC ");

                return cursor;
            }
        };

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(data.getCount()==0){
            noFavourites();
        }
        adapter.swapCursor(data);
        adapter.notifyDataSetChanged();
        Log.d("onLoadFinished",""+data.getCount());
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursor=null;
        adapter.swapCursor(null);

    }


    @Override
    public void onClick(int position) {

    }
    public void noFavourites(){
        recyclerView.setVisibility(View.INVISIBLE);
        noFavourites.setVisibility(View.VISIBLE);

    }
}
