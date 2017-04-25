package com.example.android.movieappstage2.Data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by mina essam on 23-Apr-17.
 */

public class MoviesContentProvider extends ContentProvider {
    private static final int MOVIES=100;
    private static final int MOVIES_ID=101;
    private MovieDbHelper dbHelper;
    private static UriMatcher uriMatcher=buildUriMatcher();


    public static UriMatcher buildUriMatcher(){
        UriMatcher matcher=new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(MovieAppContract.AUTHORITY,MovieAppContract.MOVIES_PATH,MOVIES);
        matcher.addURI(MovieAppContract.AUTHORITY,MovieAppContract.MOVIES_PATH+"/#",MOVIES_ID);
        return matcher;
    }
    @Override
    public boolean onCreate() {
        dbHelper=new MovieDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder)
    { int match=uriMatcher.match(uri);
        SQLiteDatabase database;
        Cursor cursor;
        switch (match){
            case MOVIES:
                database=dbHelper.getReadableDatabase();
                 cursor=database.query(MovieAppContract.MoviesTable.TABLE_NAME,projection,
                        selection,selectionArgs,null,null,sortOrder);
                break;
            case MOVIES_ID:
                database=dbHelper.getReadableDatabase();
                String id=uri.getPathSegments().get(1);
                selection= MovieAppContract.MoviesTable.MOVIE_COLUMN_ID+"=?";
                String args[]=new String [1];
                args[0]=id;
                cursor=database.query(MovieAppContract.MoviesTable.TABLE_NAME,projection,selection,args,null,null,sortOrder);
                Log.d("query Uri ",uri.toString());
                break;

            default:
                throw new UnsupportedOperationException("Unknown Uri : "+uri);

        }
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        SQLiteDatabase database;
        int match=uriMatcher.match(uri);
        long id;
        Uri returnUri=null;
        switch (match){
            case MOVIES:
                database=dbHelper.getWritableDatabase();
               id=database.insert(MovieAppContract.MoviesTable.TABLE_NAME,null,contentValues);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri : "+uri);

        }
        if(id>0){
            returnUri=ContentUris.withAppendedId(MovieAppContract.MoviesTable.CONTENT_URI,id);
        }else {
            throw new android.database.SQLException("Failed to insert the row");
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        int match=uriMatcher.match(uri);
        int deletedRows;
        switch (match){
            case MOVIES_ID:
                SQLiteDatabase database=dbHelper.getWritableDatabase();
                String[] args={uri.getPathSegments().get(1)};
                deletedRows=database.delete(MovieAppContract.MoviesTable.TABLE_NAME, MovieAppContract.MoviesTable.MOVIE_COLUMN_ID+"=?" ,
                        args);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri : "+uri.toString() );

        }
        getContext().getContentResolver().notifyChange(uri,null);
        return deletedRows;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        int match=uriMatcher.match(uri);
        int updatedRows;
        switch (match){
            case MOVIES_ID:
                SQLiteDatabase database=dbHelper.getWritableDatabase();
                String[] args={uri.getPathSegments().get(1)};
                updatedRows=database.update(MovieAppContract.MoviesTable.TABLE_NAME,contentValues, MovieAppContract.MoviesTable.MOVIE_COLUMN_ID
                +"=?",args);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri : "+uri.toString());
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return updatedRows;
    }


}