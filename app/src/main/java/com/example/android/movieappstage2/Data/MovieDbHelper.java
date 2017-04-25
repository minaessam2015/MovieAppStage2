package com.example.android.movieappstage2.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by mina essam on 23-Apr-17.
 */

public class MovieDbHelper extends SQLiteOpenHelper {

    private static  String DATABASE_NAME="movies.db";
    private static int DATABASE_VERSION=1;

    public MovieDbHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);

    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(MovieAppContract.MoviesTable.CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists "+ MovieAppContract.MoviesTable.TABLE_NAME);
        onCreate(sqLiteDatabase);

    }

}
