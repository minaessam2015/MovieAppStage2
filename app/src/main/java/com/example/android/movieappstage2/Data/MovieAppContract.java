package com.example.android.movieappstage2.Data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by mina essam on 23-Apr-17.
 */

public final class MovieAppContract {
    public static final String AUTHORITY="com.example.android.movieappstage2";
    public static final String SCHEME="content://";
    public static final String MOVIES_PATH="Movies";
    public static final String BASE_URI=SCHEME+AUTHORITY;

    public static final class MoviesTable implements BaseColumns{
        public static final Uri CONTENT_URI=Uri.parse(BASE_URI).buildUpon().appendPath(MOVIES_PATH).build();
        public static final String TABLE_NAME="Movies";
        public static final String _ID=BaseColumns._ID;
        public static final String MOVIE_COLUMN_TITLE="title";
        public static final String MOVIE_COLUMN_LANGUAGE="language";
        public static final String MOVIE_COLUMN_VOTE="vote";
        public static final String MOVIE_COLUMN_OVERVIEW="overview";
        public static final String MOVIE_COLUMN_DATE="date";
        public static final String MOVIE_COLUMN_POSTER_PATH="poster_path";
        public static final String MOVIE_COLUMN_ADULT="adult";
        public static final String MOVIE_COLUMN_ID="movieID";

        public static final String CREATE_MOVIE_TABLE="create table "+TABLE_NAME+" ( "+_ID+" "+INTEGER_TYPE+" "+PRIMARY_KEY+" "+
                AUTO_INCREMENT+" , "+MOVIE_COLUMN_TITLE+" "+ TEXT_TYPR+" "+ NOT_NULL+" , "+MOVIE_COLUMN_LANGUAGE+" "+TEXT_TYPR+" "
                +NOT_NULL+" , "+MOVIE_COLUMN_VOTE+REAL_TYPE+" "+NOT_NULL+" , "+MOVIE_COLUMN_OVERVIEW+" "+TEXT_TYPR+" "+NOT_NULL
                +" , "+MOVIE_COLUMN_DATE+" "+TEXT_TYPR+" "+NOT_NULL+" , "+MOVIE_COLUMN_POSTER_PATH+" "+TEXT_TYPR+" "+NOT_NULL+" , "
                +MOVIE_COLUMN_ADULT+" "+REAL_TYPE+" "+NOT_NULL+" , "+MOVIE_COLUMN_ID+" "+REAL_TYPE+" "+NOT_NULL+" ) ;";

    }
    public static final String REAL_TYPE=" REAL ";
    public static final String INTEGER_TYPE=" INTEGER ";
    public static final String TEXT_TYPR=" TEXT ";
    public static final String PRIMARY_KEY=" PRIMARY KEY ";
    public static final String NOT_NULL=" NOT NULL ";
    public static final String AUTO_INCREMENT=" AUTOINCREMENT ";
}
