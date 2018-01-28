package com.example.android.chosemovie.data;

/**
 * Created by milkdz on 2018/1/8.
 */

public class BaseDataInfo {

    public  static String baseUrl = "http://image.tmdb.org/t/p/w185/";
    public static String originUrl = "http://api.themoviedb.org/3/movie/";

    //the url of themoviedb search
    public  static String searchQueryPopularFromDB = originUrl + "popular";
    public  static String searchQueryTopRateFromDB = originUrl + "top_rated";
    public  static String apiKey = "api_key";
    public  static String REVIEWS = "/reviews";
    public static String CLASS_PASS = "MovieInfo";
    public static String VIDEO = "/videos";

    /**  the private api_key of themoviedb.org   */
    public  static String myPrivateKey = "b2cf1b074a9c5fd6be1d9eca9d05ddd2";

}
