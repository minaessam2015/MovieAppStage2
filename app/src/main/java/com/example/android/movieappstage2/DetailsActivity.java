package com.example.android.movieappstage2;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.android.movieappstage2.Data.MovieAppContract;
import com.example.android.movieappstage2.Data.MovieAppContract.MoviesTable;
import com.example.android.movieappstage2.NetworkUtils.NetworkUtils;
import com.example.android.movieappstage2.Utils.Movie;
import com.example.android.movieappstage2.Utils.Review;
import com.example.android.movieappstage2.Utils.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

;

public class DetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>,
        DetailsAdapter.OnClickListener
{

    private List<Trailer> trailers=new ArrayList<>();
    private List<Review> reviews=new ArrayList<>();
    private static int TRAILERS_TASK_ID=0;
    private static int REVIEWS_TASK_ID=1;
    private RecyclerView recyclerView;
    private Movie movie;
    private int movieId;
    private boolean state=false;



    DetailsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        recyclerView=(RecyclerView)findViewById(R.id.details_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //extract first row fields
        //poster  title  date  rate  overview  movieID
        Intent intent=getIntent();
        //pass it to the adapter
        movie=intent.getExtras().getParcelable("movie");
        adapter=new DetailsAdapter(this,movie,this);
        recyclerView.setAdapter(adapter);
         movieId=(int)movie.getMovieID();
        Bundle bundle=new Bundle();
        bundle.putString(MovieAppContract.MoviesTable.MOVIE_COLUMN_ID,String.valueOf(movieId));
        if(connected()){
            getSupportLoaderManager().initLoader(TRAILERS_TASK_ID,bundle,this);
            getSupportLoaderManager().initLoader(REVIEWS_TASK_ID,bundle,this);
            Log.d("Loaderinit","with args");
        }
        else {
            getSupportLoaderManager().initLoader(TRAILERS_TASK_ID,null,this);
            getSupportLoaderManager().initLoader(REVIEWS_TASK_ID,null,this);
            Log.d("Loaderinit","without args");
        }
        state=checkMovie(movieId);


    }

    public boolean checkMovie(int id){
        boolean isFavourite=false;
        Uri uri=Uri.withAppendedPath(MoviesTable.CONTENT_URI,Integer.toString(id));
        Log.d("checkMovie Uri : ",uri.toString());
        Cursor cursor=getContentResolver().query(uri,null,null,null,null);
        Log.d("checkMovie count"," "+cursor.getCount());
        if(cursor.getCount()>0) {return true;}
        return isFavourite;
    }

    public boolean connected(){
        ConnectivityManager manager=(ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        return manager.getActiveNetworkInfo()!=null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.details_menu,menu);
        MenuItem item=menu.findItem(R.id.action_add_favourite);
        if(state){
            item.setIcon(R.drawable.add_to_favorites_yellow);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_add_favourite:

                if (state) {
                    item.setIcon(R.drawable.add_to_favorites_black);
                    int rows=deleteMovie();
                    if(rows>0){
                        Toast.makeText(this,"Removed !",Toast.LENGTH_LONG).show();
                        state=!state;
                        return true;
                    }
                    else {

                        Toast.makeText(this,"cannot remove movie : "+rows,Toast.LENGTH_LONG).show();
                        return false;
                    }
                }
                else {
                    item.setIcon(R.drawable.add_to_favorites_yellow);
                    Uri uri=insertMovie();
                    if(uri!=null){
                        Toast.makeText(this,"Added to favourites !",Toast.LENGTH_LONG).show();
                        state=!state;
                        return true;
                    }else {
                        Toast.makeText(this,"cannot add uri : "+uri.toString(),Toast.LENGTH_LONG).show();
                        return false;
                    }
                }


            default:
                return super.onOptionsItemSelected(item);
        }

    }
    public Uri insertMovie(){
        ContentValues values=new ContentValues();
        //(String title, String description, float vote, String posterPath, String date, String language, float movieID,
        //String adult)
        values.put(MoviesTable.MOVIE_COLUMN_TITLE,movie.getTitle());
        values.put(MoviesTable.MOVIE_COLUMN_OVERVIEW,movie.getDescription());
        values.put(MoviesTable.MOVIE_COLUMN_VOTE,movie.getVote());
        values.put(MoviesTable.MOVIE_COLUMN_POSTER_PATH,movie.getPosterPath());
        values.put(MoviesTable.MOVIE_COLUMN_DATE,movie.getDate());
        values.put(MoviesTable.MOVIE_COLUMN_LANGUAGE,movie.getLanguage());
        values.put(MoviesTable.MOVIE_COLUMN_ID,movie.getMovieID());
        values.put(MoviesTable.MOVIE_COLUMN_ADULT,movie.isAdult());
        Uri uri=getContentResolver().insert(MoviesTable.CONTENT_URI,values);
        return uri;
    }
    public int deleteMovie(){
        String id=Integer.toString(movieId);
        Uri uri=Uri.withAppendedPath(MoviesTable.CONTENT_URI,id);
        int rows=getContentResolver().delete(uri,null,null);
        return rows;
    }

    @Override
    public Loader<String> onCreateLoader( final int id, final Bundle args) {

        return new AsyncTaskLoader<String>(this) {
            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                if(args==null){
                    Log.d("onCreateLoader","Null loader");
                    return;
                }
                forceLoad();
            }

            @Override
            public String loadInBackground() {
                int taskId=id;
                Log.d("loadInBackground","id "+taskId);
                String key=args.getString(MovieAppContract.MoviesTable.MOVIE_COLUMN_ID);
                String response;
                if(taskId==TRAILERS_TASK_ID){

                    response=NetworkUtils.getTrailersResponse(NetworkUtils.makeTrailersURL(key));
                    parseTrailersJSON(response);
                }else {
                    response=NetworkUtils.getReviewsResponse(NetworkUtils.makeReviewsURL(key));
                    parseReviewsJSON(response);
                }
                return response;
            }

            public void parseTrailersJSON(String response){
                if(response==null){return;}
                try{
                    JSONObject object=new JSONObject(response);
                    JSONArray array=object.getJSONArray("results");
                    int size=array.length();
                    for(int i=0;i<size;++i){
                        JSONObject trailer=array.getJSONObject(i);
                        trailers.add(new Trailer(trailer.getString("name"),trailer.getString("key"),trailer.getString("size")));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            public void parseReviewsJSON(String response){
                if(response==null) {
                    return;}
                Log.d("parseReviewsJSON",response);
                try{
                    JSONObject object=new JSONObject(response);
                    JSONArray array=object.getJSONArray("results");
                    int size=array.length();
                    for(int i=0;i<size;++i){
                        JSONObject review=array.getJSONObject(i);
                        reviews.add(new Review(review.getString("author"),review.getString("content")));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        //notify the adapter
        if (data==null){

            return;}
        int loaderID=loader.getId();
        Log.d("LoaderID",String .valueOf(loaderID));
        if(loaderID==TRAILERS_TASK_ID){
            adapter.setTrailers(trailers);
            adapter.notifyDataSetChanged();
        }else {
            adapter.setReviews(reviews);
            adapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onLoaderReset(Loader<String> loader) {
        trailers.clear();
        reviews.clear();

    }

    @Override
    public void onClick(int position) {
        URL url=NetworkUtils.makeTrailerURL(trailers.get(position).getKey());
        Uri uri=Uri.parse(url.toString());
        Intent intent=new Intent(Intent.ACTION_VIEW,uri);
        if(intent.resolveActivity(getPackageManager())!=null){
            startActivity(intent);
        }
        Log.d("DetailsOnClick","Intent Started");
    }
}
