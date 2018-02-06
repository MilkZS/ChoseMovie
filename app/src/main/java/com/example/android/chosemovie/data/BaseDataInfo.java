package com.example.android.chosemovie.data;

/**
 * Created by milkdz on 2018/1/8.
 */

public interface BaseDataInfo {

    String baseUrl = "http://image.tmdb.org/t/p/w185/";
    String originUrl = "http://api.themoviedb.org/3/movie/";

    String POPULAR = "popular";
    int POPULAR_MODE = 0;
    int RATE_DATE_MODE = 1;
    int BOTH_POPULAR_RATE_MODE = 5;
    int FAVORITE_MODE = 4;
    String TOP_RATE = "top_rated";

    //the url of themoviedb search
    String searchQueryPopularFromDB = originUrl + "popular";
    String searchQueryTopRateFromDB = originUrl + "top_rated";
    String apiKey = "api_key";
    String REVIEWS = "/reviews";
    String CLASS_PASS = "MovieInfo";
    String VIDEO = "/videos";

    /** youtube */
    String youtubeBaseUrl = "https://www.youtube.com/watch";
    String youtubeVideo = "v";

    /**  the private api_key of themoviedb.org   */
    String myPrivateKey = "b2cf1b074a9c5fd6be1d9eca9d05ddd2";
}
