package com.example.android.movieappstage2.NetworkUtils;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by mina essam on 24-Apr-17.
 */

public class NetworkUtils  {
    private static final String MOVIE_BASE_URL="http://api.themoviedb.org/3/movie/";
    private static final String SEARCH_TYPE_POPULAR="popular/";
    private static final String SEARCH_TYPE_TOP_RATED="top_rated/";
    private static final String API_PARAM="api_key";
    private static final String API_KEY="80d165558137e1d2cd4d07092d2292df";

    public static URL makePopularSearchURL(){
        Uri uri=Uri.parse(MOVIE_BASE_URL+SEARCH_TYPE_POPULAR).buildUpon().appendQueryParameter(API_PARAM,API_KEY).build();
        URL url=null;
        try {
            url=new URL(uri.toString());
        }catch (MalformedURLException e){
            e.printStackTrace();
        }
        Log.d("NetworkUtils",url.toString());
        return url;
    }

    public static URL makeTopRatedSearchURL(){
        Uri uri=Uri.parse(MOVIE_BASE_URL+SEARCH_TYPE_TOP_RATED).buildUpon().appendQueryParameter(API_PARAM,API_KEY).build();
        URL url=null;
        try{
            url= new URL(uri.toString());

        }catch (MalformedURLException e){
            e.printStackTrace();
        }
        Log.d("NetworkUtils",url.toString());
        return url;
    }

    private static final String POSTER_BASE_URL="http://image.tmdb.org/t/p/w342";
    public static URL makePosterPathURL(String path){
        Uri uri=Uri.parse(POSTER_BASE_URL+path).buildUpon().build();
        URL url=null;
        try {
            url=new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.d("NetworkUtilsPoster",url.toString());
        return url;
    }
    private static final String TRAILER_BASE_URL="https://www.youtube.com/watch";
    private static final String WATCH_PARAM="v";
    private static String WATCH_VALUE="";

    public static URL makeTrailerURL(String value){
        WATCH_VALUE=value;
        Uri uri=Uri.parse(TRAILER_BASE_URL).buildUpon().appendQueryParameter(WATCH_PARAM,WATCH_VALUE).build();
        URL url=null;
        try {
            url=new URL(uri.toString());
        }catch (MalformedURLException e){
            e.printStackTrace();
        }
        Log.d("NetworkUtilsTrailer",url.toString());
        return url;
    }

    private static final String BY_ID_BASE_URL ="https://api.themoviedb.org/3/movie/";
    private static String KEY="";

    public static URL makeTrailersURL(String key){
        KEY=key;
        Uri uri=Uri.parse(BY_ID_BASE_URL +KEY+"/videos").buildUpon().appendQueryParameter(API_PARAM,API_KEY).build();
        URL url=null;
        try {
            url=new URL(uri.toString());
        }catch (MalformedURLException e){
            e.printStackTrace();
        }
        Log.d("NetworkUtilsTrailers",url.toString());
        return url;
    }

    public static String getTrailersResponse(URL url) {
        HttpURLConnection connection=null;
        String response=null;
        try {
            connection=(HttpURLConnection) url.openConnection();
            InputStream inputStream=connection.getInputStream();
            Scanner scanner=new Scanner(inputStream).useDelimiter("\\A");
            if(scanner.hasNext()){
                response= scanner.next();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            connection.disconnect();
        }
        return response;
    }

    public static String getReviewsResponse(URL url){
        String response=null;
        HttpURLConnection connection=null;
        try {
            connection=(HttpURLConnection)url.openConnection();
            InputStream inputStream=connection.getInputStream();
            Scanner scanner=new Scanner(inputStream).useDelimiter("\\A");
            if(scanner.hasNext()){
                response= scanner.next();
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            connection.disconnect();
        }
        return response;
    }

    public static URL makeReviewsURL(String key){
        KEY=key;
        Uri uri=Uri.parse(BY_ID_BASE_URL +KEY+"/reviews").buildUpon().appendQueryParameter(API_PARAM,API_KEY).build();
        URL url=null;
        try {
            url=new URL(uri.toString());
        }catch (MalformedURLException e){
            e.printStackTrace();
        }
        Log.d("NetworkUtilsReviews",url.toString());
        return url;
    }

    public static String getMoviesResponse(URL url) throws IOException {
        HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
        String response=null;
        try {
            InputStream inputStream=urlConnection.getInputStream();
            Scanner scanner =new Scanner(inputStream).useDelimiter("\\A");
            boolean hasNext=scanner.hasNext();
            if(hasNext){
                response= scanner.next();
            }

        }catch (MalformedURLException e){
            e.printStackTrace();
        }finally {
            urlConnection.disconnect();
        }
        return response;
    }

}
