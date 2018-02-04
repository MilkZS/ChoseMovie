package com.example.android.chosemovie.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.android.chosemovie.MovieInfoDBHelper;

/**
 * Created by milkdz on 2018/2/4.
 */

public class MovieProvider extends ContentProvider {

    public static final int CODE_MOVIE = 100;

    private static final UriMatcher uriMatcher = buildUriMatcher();
    private MovieInfoDBHelper movieInfoDBHelper;

    @Override
    public boolean onCreate() {
        movieInfoDBHelper = new MovieInfoDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }


    public static UriMatcher buildUriMatcher(){
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieInfoContract.CONTENT_AUTHORITY;
        matcher.addURI(authority,MovieInfoContract.PATH_MOVIE,CODE_MOVIE);
        return matcher;
    }
}
