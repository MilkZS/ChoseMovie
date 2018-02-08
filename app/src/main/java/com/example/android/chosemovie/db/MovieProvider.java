package com.example.android.chosemovie.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by milkdz on 2018/2/4.
 */

public class MovieProvider extends ContentProvider {

    public static final int CODE_MOVIE = 100;
    public static final int CODE_MOVIE_ID = 101;

    private static final UriMatcher uriMatcher = buildUriMatcher();
    private MovieInfoDBHelper movieInfoDBHelper;

    @Override
    public boolean onCreate() {
        movieInfoDBHelper = new MovieInfoDBHelper(getContext());
        return true;
    }

    /**
     * Insert data into db
     *
     * @param uri content URI
     * @param values a row data
     * @return rowId
     */
    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = movieInfoDBHelper.getWritableDatabase();
        int rowId = 0;
        switch (uriMatcher.match(uri)){
            case CODE_MOVIE:{
                db.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MovieInfoContract.MovieInfos.TABLE_NAME, null, value);
                        if (_id != -1) {
                            rowId++;
                        }
                    }
                    db.setTransactionSuccessful();
                }finally {
                    db.endTransaction();
                }
                if(rowId > 0){
                    getContext().getContentResolver().notifyChange(uri,null);
                }
                return rowId;
            }
        }
        return super.bulkInsert(uri,values);
    }

    /**
     * Query data from db
     *
     * @param uri Content uri
     * @param projection
     * @param selection query row name
     * @param selectionArgs target row array
     * @param sortOrder sort auto
     * @return cursor include data
     */
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;
        switch (uriMatcher.match(uri)){
            case CODE_MOVIE_ID:{
                String normalIdString = uri.getLastPathSegment();
                String[] IdStringsArr = new String[]{normalIdString};
                cursor = movieInfoDBHelper.getReadableDatabase().query(
                        MovieInfoContract.MovieInfos.TABLE_NAME,IdStringsArr,
                        MovieInfoContract.MovieInfos.COLUMN_MOVIE_ID+"=",
                        selectionArgs,null,null,sortOrder);
            }break;
            case CODE_MOVIE:{
                cursor = movieInfoDBHelper.getReadableDatabase().query(
                        MovieInfoContract.MovieInfos.TABLE_NAME,projection,selection,selectionArgs,
                        null,null,sortOrder);
            }break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
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
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    /**
     * Delete data from db
     *
     * @param uri content uri
     * @param selection row name select
     * @param selectionArgs select values
     * @return delete row numbers
     */
    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int rowId = 0;
        if (null == selection) selection = "1";

        switch (uriMatcher.match(uri)){
            case CODE_MOVIE:{
                rowId = movieInfoDBHelper.getWritableDatabase().delete(
                        MovieInfoContract.MovieInfos.TABLE_NAME, selection,selectionArgs);
            }break;
            default:
                throw new UnsupportedOperationException("Un know uri :" + uri);
        }
        if(rowId != -1){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return rowId;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public void shutdown() {
        movieInfoDBHelper.close();
        super.shutdown();
    }

    /**
     * Add content uri in urimatcher
     *
     * @return urimatcher
     */
    public static UriMatcher buildUriMatcher(){
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieInfoContract.CONTENT_AUTHORITY;
        matcher.addURI(authority,MovieInfoContract.PATH_MOVIE,CODE_MOVIE);
        matcher.addURI(authority,MovieInfoContract.PATH_MOVIE+"/#",CODE_MOVIE_ID);
        return matcher;
    }
}
