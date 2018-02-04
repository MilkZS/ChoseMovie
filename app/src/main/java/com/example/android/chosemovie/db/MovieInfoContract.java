package com.example.android.chosemovie.db;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by milkdz on 2018/2/4.
 */

public class MovieInfoContract {

    public static final String CONTENT_AUTHORITY = "com.example.android.chosemovie";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+CONTENT_AUTHORITY);
    public static final String PATH_MOVIE = "movie";

    public static final class MovieInfos implements BaseColumns{

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE)
                .build();

        public static final String TABLE_NAME = "movieInfo";
        public static final String COLUMN_MOVIE_NAME = "title";
        public static final String COLUMN_MOVIE_DATE = "pub_date";
        public static final String COLUMN_MOVIE_VOTE = "vote";
        public static final String COLUMN_MOVIE_REVIEW = "review";
        public static final String COLUMN_MOVIE_TRAILER = "trailers";
    }

    public static final class MovieReviews implements BaseColumns{
        public static final String TABLE_NAME = "movieReviews_";
        public static final String COLUMN_MOVIE_REVIEWS_AUTHOR = "author";
        public static final String COLUMN_MOVIE_REVIEW_CONTENT = "content";
    }

    public static final class MovieTrailers implements BaseColumns{
        public static final String TABLE_NAME = "movieTrailers_";
        public static final String COLUMN_MOVIE_TRAILER_URI = "trailers_uri";
    }

    /**
     * Build content Uri for movie detail
     *
     * @param base
     * @return
     */
    public static Uri buildContentForMovieDetail(String base){
        return BASE_CONTENT_URI.buildUpon().appendPath(base).build();
    }
}
