package com.example.android.chosemovie.utility;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.android.chosemovie.base.MovieInfo;
import com.example.android.chosemovie.base.MovieReviews;
import com.example.android.chosemovie.base.MovieTrailers;
import com.example.android.chosemovie.data.BaseDataInfo;
import com.example.android.chosemovie.db.MovieInfoContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by milkdz on 2018/1/7.
 */

public class OpenMovieInfoJson {

    private String TAG = "OpenMovieInfoJson";
    private boolean DBG = true;

    private String QUERY = "results";
    private String POSTER_PATH = "poster_path";
    private String ID = "id";
    private String TITLE = "title";
    private String BACK_PATH = "backdrop_path";
    private String OVER_VIEW = "overview";
    private String VOTE_AVERAGE = "vote_average";
    private String RELEASE_DATE = "release_date";

    private String REVIEWS_AUTHOR = "author";
    private String REVIEWS_CONTENT = "content";

    private String VIDEO_TRAILERS_KEY = "key";
    private SharedPreferences sharedPreferences;
    public static OpenMovieInfoJson openMovieInfoJson;

    private OpenMovieInfoJson() {
    }

    public static OpenMovieInfoJson getInstance() {

        if (openMovieInfoJson == null) {
            return new OpenMovieInfoJson();
        }
        return openMovieInfoJson;
    }

    public ContentValues[] getDataFromMovieJson(String jsonString, int mode, Context context) throws JSONException {
        if (DBG) Log.d(TAG, "jsonString read from themoviedb.org is : " + jsonString);
        if (DBG) Log.d(TAG, "jsonString read from themoviedb.org == mode : " + mode);

        sharedPreferences = context.getSharedPreferences(BaseDataInfo.MOVIE_PREFERENCE,Context.MODE_PRIVATE);
        JSONObject movieJson = new JSONObject(jsonString);
        JSONArray movieArray = movieJson.getJSONArray(QUERY);
        ContentValues[] contentValues = new ContentValues[movieArray.length()];
        JSONObject jsonObject;
        String sPath;
        String sPath_back;
        for (int i = 0; i < movieArray.length(); i++) {
            jsonObject = movieArray.getJSONObject(i);
            String MovieID = jsonObject.getString(ID);
            int favorite = sharedPreferences.getInt(MovieID,BaseDataInfo.UN_FAVORITE);
            int favoriteInt = favorite + mode;
            String sReview = getMoviesReviews(MovieID);
            String sTrailers = getMoviesTrailers(MovieID);
            sPath = BaseDataInfo.baseUrl + jsonObject.getString(POSTER_PATH);
            sPath_back = BaseDataInfo.baseUrl + jsonObject.getString(BACK_PATH);

            ContentValues contentValueSingle = new ContentValues();
            contentValueSingle.put(MovieInfoContract.MovieInfos.COLUMN_MOVIE_ID,MovieID);
            contentValueSingle.put(MovieInfoContract.MovieInfos.COLUMN_MOVIE_DATE,jsonObject.getString(RELEASE_DATE));
            contentValueSingle.put(MovieInfoContract.MovieInfos.COLUMN_MOVIE_NAME,jsonObject.getString(TITLE));
            contentValueSingle.put(MovieInfoContract.MovieInfos.COLUMN_MOVIE_VOTE,jsonObject.getString(VOTE_AVERAGE));
            contentValueSingle.put(MovieInfoContract.MovieInfos.COLUMN_MOVIE_POSTER_IMAGE,sPath);
            contentValueSingle.put(MovieInfoContract.MovieInfos.COLUMN_MOVIE_BACK_IMAGE,sPath_back);
            contentValueSingle.put(MovieInfoContract.MovieInfos.COLUMN_MOVIE_SORT,favoriteInt);
            contentValueSingle.put(MovieInfoContract.MovieInfos.COLUMN_MOVIE_OVER_VIEW,jsonObject.getString(OVER_VIEW));
            contentValueSingle.put(MovieInfoContract.MovieInfos.COLUMN_MOVIE_REVIEW,sReview);
            contentValueSingle.put(MovieInfoContract.MovieInfos.COLUMN_MOVIE_TRAILER,sTrailers);
            contentValues[i] = contentValueSingle;
            Log.d("========mode ",favoriteInt+"");
            Log.d("========sReview",sReview + "");
            Log.d("========sTrailers",sTrailers + "");
        }
        return contentValues;
    }

    /**
     * get movie reviews array
     *
     * @param sId movie id
     * @return array of MovieReviews
     */
    @Nullable
    private String getMoviesReviews(String sId) {

        URL url = NetWorkUtils.buildUrlForDifSort(BaseDataInfo.ID_REVIEWS, sId);
        Log.d(TAG,"getMoviesReviews = URL = " + url);
        try {
            String jsonData = NetWorkUtils.getResponseFromHttpUrl(url);
            Log.d(TAG,"getMoviesReviews = jsonData = " + jsonData);
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray jsonArray = jsonObject.getJSONArray(QUERY);
            JSONObject jsonSingleObject;
            String reviewsArr = "";
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonSingleObject = jsonArray.getJSONObject(i);
                String author = jsonSingleObject.getString(REVIEWS_AUTHOR);
                String content = jsonSingleObject.getString(REVIEWS_CONTENT);
                reviewsArr = reviewsArr + author + "," + content + ";";
            }
            return reviewsArr;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get Movie Trailers
     *
     * @param jsonString json string of array message
     * @return MovieTrailers object
     * @throws JSONException
     */
    private String getMovieTrailerss(String jsonString) throws JSONException {

        if (DBG) Log.d(TAG,"Movie Trailers is " + jsonString);
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONArray jsonArray = jsonObject.getJSONArray(QUERY);
        int length = jsonArray.length();
        JSONObject jsonSingleObject;
        String trailes = "";
        for (int i = 0; i < length; i++) {
            jsonSingleObject = jsonArray.getJSONObject(i);
            String v = jsonSingleObject.getString(VIDEO_TRAILERS_KEY);
            Uri uri = NetWorkUtils.buildUrlForYoutube(v);
            trailes = trailes + ";" + uri;
        }
        return trailes;
    }



    /**
     * Get object of MoviesTrailers
     *
     * @param movieId movie id
     * @return object
     */
    @Nullable
    private String getMoviesTrailers(String movieId) {

        URL url = NetWorkUtils.buildUrlForDifSort(BaseDataInfo.ID_MOVIE, movieId);
        try {
            String jsonData = NetWorkUtils.getResponseFromHttpUrl(url);
            String trailersArr = getMovieTrailerss(jsonData);
            return trailersArr;
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**

     * Get detail messages about Reviews

     *

     * @param jsonString json string of detail message

     * @return object MovieInfo

     * @throws JSONException

     */

    public MovieReviews[] getMovieReviews(String jsonString) throws JSONException {



        if (DBG) Log.d(TAG, "++++++jsonData++++++++ === > " + jsonString);
        MovieReviews movieReviews;
        MovieReviews[] movieReviewsArr;

        JSONObject jsonObject = new JSONObject(jsonString);

        JSONArray jsonArray = jsonObject.getJSONArray(QUERY);

        movieReviewsArr = new MovieReviews[jsonArray.length()];

        JSONObject jsonSingleObject;

        for (int i = 0; i < jsonArray.length(); i++) {

            jsonSingleObject = jsonArray.getJSONObject(i);

            String author = jsonSingleObject.getString(REVIEWS_AUTHOR);

            String content = jsonSingleObject.getString(REVIEWS_CONTENT);

            movieReviews = new MovieReviews(author, content);

            movieReviewsArr[i] = movieReviews;

        }

        return movieReviewsArr;

    }



    /**

     * Get Movie Trailers

     *

     * @param jsonString json string of array message

     * @return MovieTrailers object

     * @throws JSONException

     */

    public MovieTrailers getMovieTrailers(String jsonString) throws JSONException {



        if (DBG) Log.d(TAG,"Movie Trailers is " + jsonString);

        JSONObject jsonObject = new JSONObject(jsonString);

        JSONArray jsonArray = jsonObject.getJSONArray(QUERY);

        int length = jsonArray.length();

        ArrayList<Uri> arrayList = new ArrayList<>();

        JSONObject jsonSingleObject;

        for (int i = 0; i < length; i++) {

            jsonSingleObject = jsonArray.getJSONObject(i);

            String v = jsonSingleObject.getString(VIDEO_TRAILERS_KEY);

            Uri uri = NetWorkUtils.buildUrlForYoutube(v);

            arrayList.add(i, uri);

        }

        return new MovieTrailers(arrayList);

    }
}
