package com.example.android.chosemovie.db;

import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;

import com.example.android.chosemovie.data.BaseDataInfo;
import com.example.android.chosemovie.data.SQLBaseInfo;

import static com.example.android.chosemovie.db.MovieInfoContract.MovieInfos.CONTENT_URI;

/**
 * Created by milkdz on 2018/2/4.
 */

public class MovieInfoContract {

    public static final String CONTENT_AUTHORITY = "com.example.android.chosemovie";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_MOVIE = "movie";

    public static final class MovieInfos implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE)
                .build();

        public static final String TABLE_NAME = "movieInfo";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_MOVIE_NAME = "title";
        public static final String COLUMN_MOVIE_DATE = "pub_date";
        public static final String COLUMN_MOVIE_VOTE = "vote";
        public static final String COLUMN_MOVIE_OVER_VIEW = "over_view";
        public static final String COLUMN_MOVIE_REVIEW = "review";
        public static final String COLUMN_MOVIE_TRAILER = "trailers";
        public static final String COLUMN_MOVIE_POSTER_IMAGE = "poster_image";
        public static final String COLUMN_MOVIE_BACK_IMAGE = "back_image";
        public static final String COLUMN_MOVIE_SORT = "sort";
    }

    public static final String[] MAIN_MOVIE_UI = new String[]{
            MovieInfos._ID,
            MovieInfos.COLUMN_MOVIE_POSTER_IMAGE,
            MovieInfos.COLUMN_MOVIE_SORT,
    };

    public static final String[] CHILD_MOVIE_UI = new String[]{
            MovieInfos.COLUMN_MOVIE_ID,
            MovieInfos.COLUMN_MOVIE_BACK_IMAGE,
            MovieInfos.COLUMN_MOVIE_DATE,
            MovieInfos.COLUMN_MOVIE_NAME,
            MovieInfos.COLUMN_MOVIE_VOTE,
            MovieInfos.COLUMN_MOVIE_OVER_VIEW,
            MovieInfos.COLUMN_MOVIE_TRAILER,
            MovieInfos.COLUMN_MOVIE_REVIEW
    };

    /**
     * Build content Uri for movie detail with movie id
     *
     * @param base
     * @return
     */
    public static Uri buildContentForMovieDetail(String base) {
        return CONTENT_URI.buildUpon().appendPath(base).build();
    }

    /**
     * Get the selection
     *
     * @param select select value
     * @return select string
     */
    @NonNull
    public static String getSelect(int select) {
        switch (select) {
            case BaseDataInfo.POPULAR_MODE:{
                return MovieInfos.COLUMN_MOVIE_SORT + "=" + BaseDataInfo.FAVORITE_POPULAR_MODE
                        + SQLBaseInfo.OR
                        + MovieInfos.COLUMN_MOVIE_SORT + "=" + BaseDataInfo.UN_FAVORITE_POPULAR;
            }
            case BaseDataInfo.RATE_DATE_MODE: {
                return MovieInfos.COLUMN_MOVIE_SORT + "=" + BaseDataInfo.FAVORITE_RATE_DATE_MODE
                        + SQLBaseInfo.OR
                        + MovieInfos.COLUMN_MOVIE_SORT + "=" + BaseDataInfo.UN_FAVORITE_RATE_DATE_MODE;
            }
            case BaseDataInfo.FAVORITE_MODE: {
                return MovieInfos.COLUMN_MOVIE_SORT + "=" + BaseDataInfo.FAVORITE_RATE_DATE_MODE
                        + SQLBaseInfo.OR
                        + MovieInfos.COLUMN_MOVIE_SORT + "=" + BaseDataInfo.FAVORITE_POPULAR_MODE;
            }
        }
        return null;
    }
}
