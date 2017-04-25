package com.example.android.movieappstage2;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.movieappstage2.NetworkUtils.NetworkUtils;
import com.example.android.movieappstage2.Utils.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements HomeAdapter.OnRecyclerListener , LoaderManager.LoaderCallbacks<String>{
    private RecyclerView recyclerView;
    private List<Movie> movies=new ArrayList<>();
    HomeAdapter adapter;
    private static final int popularSearch=0;
    private static final int topRatedSearch=1;
    private static final int loaderID=0;
    TextView error;
    ProgressBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView=(RecyclerView)findViewById(R.id.movies_recycler);
        error=(TextView)findViewById(R.id.error_text_view);
        bar=(ProgressBar)findViewById(R.id.progress_bar);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.setHasFixedSize(true);
        adapter=new HomeAdapter(this,this);
        recyclerView.setAdapter(adapter);
        if(connected()) {
            Bundle bundle = new Bundle();
            bundle.putInt("searchType", popularSearch);
            getSupportLoaderManager().initLoader(loaderID, bundle, this);
        }
        else {
            getSupportLoaderManager().initLoader(loaderID,null,this);
            noInternet();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch (id){
            case R.id.action_popular:
                if(connected()){
                Bundle bundle=new Bundle();
                bundle.putInt("searchType",popularSearch);
                getSupportLoaderManager().restartLoader(loaderID,bundle,this);

                    }
                    else noInternet();
                break;
            case R.id.action_top_rated:
                if(connected()){
                Bundle bundle1=new Bundle();
                bundle1.putInt("searchType",topRatedSearch);
                getSupportLoaderManager().restartLoader(loaderID,bundle1,this);
                }
                else noInternet();

                break;
            case R.id.action_favourites:
                startActivity(new Intent(this,FavouriteActivity.class));
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
    public boolean connected(){
        ConnectivityManager manager=(ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        return manager.getActiveNetworkInfo()!=null;
    }
    public void noInternet(){
        Toast.makeText(this,R.string.no_internet,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClickListener(int position) {
        //start the details activity
        Intent intent=new Intent(this,DetailsActivity.class);
        intent.putExtra("movie",movies.get(position));
        startActivity(intent);
    }
    public void showError(){
        recyclerView.setVisibility(View.INVISIBLE);
        bar.setVisibility(View.INVISIBLE);
        error.setVisibility(View.VISIBLE);
    }
    public void statrLoading(){
        recyclerView.setVisibility(View.INVISIBLE);
        error.setVisibility(View.INVISIBLE);
        bar.setVisibility(View.VISIBLE);
    }
    public void finishLoading(){
        bar.setVisibility(View.INVISIBLE);
        error.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public Loader<String> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<String>(this) {
            String response;
            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                if(args==null){
                    return;
                }

                if(response!=null){
                    //if there is data don't search
                    deliverResult(response);
                }
                //initialize onPreExecute
                statrLoading();

                forceLoad();
            }

            @Override
            public void deliverResult(String data) {
                response=data;
                super.deliverResult(data);
            }

            @Override
            public String loadInBackground() {
                int search=args.getInt("searchType");
                switch (search){
                    case popularSearch:
                        try {
                            response=NetworkUtils.getMoviesResponse(NetworkUtils.makePopularSearchURL());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if(response!=null){
                            parseJSON(response);
                        }
                        return  response;
                    case topRatedSearch:
                        try {
                            response=NetworkUtils.getMoviesResponse(NetworkUtils.makeTopRatedSearchURL());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if(response!=null){
                            parseJSON(response);
                        }
                        return response;
                }
                return null;
            }
            public void parseJSON(String res)  {
                movies.clear();
                try {
                    JSONObject object=new JSONObject(res);
                    JSONArray array=object.getJSONArray("results");
                    int size=array.length();
                    for(int i=0;i<size;++i){
                        JSONObject movieJson=array.getJSONObject(i);
                      /*  (String title, String description, float vote,
                        String posterPath, String date, String language,
                        float movieID, boolean adult)*/
                        movies.add(new Movie(movieJson.getString("title"),movieJson.getString("overview"),
                                (float) movieJson.getDouble("vote_average"),movieJson.getString("poster_path"),
                                movieJson.getString("release_date"),movieJson.getString("original_language"),
                                movieJson.getInt("id"),movieJson.getString("adult")));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        if(data!=null){
            finishLoading();
            adapter.setMovies(movies);
            adapter.notifyDataSetChanged();
        }
        else {
            showError();
        }

    }

    @Override
    public void onLoaderReset(Loader<String> loader) {
        movies.clear();
        adapter.setMovies(movies);
        adapter.notifyDataSetChanged();

    }

}
