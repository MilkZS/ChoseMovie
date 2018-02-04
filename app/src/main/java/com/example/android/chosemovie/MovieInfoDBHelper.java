package com.example.android.chosemovie;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.chosemovie.db.MovieInfoContract;
import com.example.android.chosemovie.data.SQLBaseInfo;

/**
 * Created by milkdz on 2018/2/4.
 */

public class MovieInfoDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "MovieInfo.db";
    private static final int DATABASE_VERSION = 1;

    public MovieInfoDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(searchSQLFromTableMovieInfo());
    }

    private String searchSQLFromTableMovieInfo(){
        return SQLBaseInfo.CREATE_TABLE + MovieInfoContract.MovieInfos.TABLE_NAME + " ( "
                + MovieInfoContract.MovieInfos._ID + SQLBaseInfo.INT_PRIMARY_KEY
                + MovieInfoContract.MovieInfos.COLUMN_MOVIE_NAME + SQLBaseInfo.TEXT_NO_NULL
                + MovieInfoContract.MovieInfos.COLUMN_MOVIE_DATE + SQLBaseInfo.TEXT_NO_NULL
                + MovieInfoContract.MovieInfos.COLUMN_MOVIE_VOTE + SQLBaseInfo.TEXT_NO_NULL
                + MovieInfoContract.MovieInfos.COLUMN_MOVIE_TRAILER + SQLBaseInfo.TEXT_NO_NULL
                + MovieInfoContract.MovieInfos.COLUMN_MOVIE_REVIEW + SQLBaseInfo.TEXT_NO_NULL
                + " ); ";
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQLBaseInfo.DROP_TABLE + MovieInfoContract.MovieInfos.TABLE_NAME);
        onCreate(db);
    }
}