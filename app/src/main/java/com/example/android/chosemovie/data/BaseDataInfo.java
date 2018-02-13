package com.example.android.chosemovie.data;

/**
 * Created by milkdz on 2018/1/8.
 */

public interface BaseDataInfo {

    String baseUrl = "http://image.tmdb.org/t/p/w185/";
    String originUrl = "http://api.themoviedb.org/3/movie/";

    String POPULAR = "popular";
    String TOP_RATE = "top_rated";

    //the url of themoviedb search
    String searchQueryPopularFromDB = originUrl + POPULAR;
    String searchQueryTopRateFromDB = originUrl + TOP_RATE;
    String apiKey = "api_key";
    String REVIEWS = "/reviews";
    String VIDEO = "/videos";

    /** youtube */
    String youtubeBaseUrl = "https://www.youtube.com/watch";
    String youtubeVideo = "v";

    int POPULAR_MODE = 1;
    int RATE_DATE_MODE = 2;
    int FAVORITE_MODE = 3;
    int UN_FAVORITE = 0;

    int FAVORITE_POPULAR_MODE = POPULAR_MODE + FAVORITE_MODE;//4
    int FAVORITE_RATE_DATE_MODE = RATE_DATE_MODE + FAVORITE_MODE;//5
    int UN_FAVORITE_POPULAR = POPULAR_MODE + UN_FAVORITE;//1
    int UN_FAVORITE_RATE_DATE_MODE = RATE_DATE_MODE + UN_FAVORITE;//2

    int ID_MOVIE = 10;
    int ID_REVIEWS = 11;

    String MOVIE_PREFERENCE = "Text2";
    String MOVIE_PREFERENCE_MAIN = "Text1";
    String MOVIE_PREFERENCE_CHILD = "childText";

    /**  the private api_key of themoviedb.org   */
    String myPrivateKey = "";
}
