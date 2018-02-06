package com.example.android.chosemovie.utility;

import android.content.ContentValues;
import android.net.Uri;
import android.util.Log;

import com.example.android.chosemovie.base.MovieInfo;
import com.example.android.chosemovie.base.MovieReviews;
import com.example.android.chosemovie.base.MovieTrailers;
import com.example.android.chosemovie.data.BaseDataInfo;
import com.example.android.chosemovie.db.MovieInfoContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URI;
import java.util.ArrayList;

/**
 * Created by milkdz on 2018/1/7.
 */

public class OpenMovieInfoJson {

    private String TAG = "OpenMovieInfoJson";
    private boolean DBG = true;

    private String FAVORITE_TEXT_DEFAULT = "收藏此电影";

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

    public static OpenMovieInfoJson openMovieInfoJson;

    private MovieInfo movieInfo;
    private MovieReviews movieReviews;

    private OpenMovieInfoJson() {
    }

    public static OpenMovieInfoJson getInstance() {

        if (openMovieInfoJson == null) {
            return new OpenMovieInfoJson();
        }
        return openMovieInfoJson;
    }

    public ContentValues[] getDataFromMovieJson(String jsonString) throws JSONException {
        if (DBG) Log.d(TAG, "jsonString read from themoviedb.org is : " + jsonString);
        JSONObject movieJson = new JSONObject(jsonString);
        JSONArray movieArray = movieJson.getJSONArray(QUERY);
        ContentValues[] contentValues = new ContentValues[movieArray.length()];
        JSONObject jsonObject;
        String sPath;
        String sPath_back;

        String overView;
        for (int i = 0; i < movieArray.length(); i++) {
            jsonObject = movieArray.getJSONObject(i);

            String sReview = MovieInfoContract.MovieReviews.TABLE_NAME + i;
            String sTrailers = MovieInfoContract.MovieTrailers.TABLE_NAME + i;
            sPath = BaseDataInfo.baseUrl + jsonObject.getString(POSTER_PATH);
           // sId = BaseDataInfo.originUrl + jsonObject.getString(ID);
            sPath_back = BaseDataInfo.baseUrl + jsonObject.getString(BACK_PATH);
            overView =  jsonObject.getString(OVER_VIEW);
            ContentValues contentValueSingle = new ContentValues();
            contentValueSingle.put(MovieInfoContract.MovieInfos.COLUMN_MOVIE_ID,jsonObject.getString(ID));
            contentValueSingle.put(MovieInfoContract.MovieInfos.COLUMN_MOVIE_DATE,jsonObject.getString(RELEASE_DATE));
            contentValueSingle.put(MovieInfoContract.MovieInfos.COLUMN_MOVIE_NAME,jsonObject.getString(TITLE));
            contentValueSingle.put(MovieInfoContract.MovieInfos.COLUMN_MOVIE_VOTE,jsonObject.getString(VOTE_AVERAGE));
            contentValueSingle.put(MovieInfoContract.MovieInfos.COLUMN_MOVIE_POSTER_IMAGE,sPath);
            contentValueSingle.put(MovieInfoContract.MovieInfos.COLUMN_MOVIE_BACK_IMAGE,sPath_back);
            //contentValueSingle.put(MovieInfoContract.MovieInfos.COLUMN_MOVIE_REVIEW,sReview);
            //contentValueSingle.put(MovieInfoContract.MovieInfos.COLUMN_MOVIE_TRAILER,sTrailers);
            contentValues[i] = contentValueSingle;
        }
        return contentValues;
    }

    /**
     * Analysis the jsonData from NetWorkUtils.getResponseFromHttpUrl
     *
     * @param jsonString json read from themoviedb.org
     * @return the object of MovieInfo
     * @throws JSONException
     * @see NetWorkUtils
     */
    public MovieInfo[] getDataFromMovieJson1(String jsonString) throws JSONException {

        MovieInfo[] movieData;
        if (DBG) Log.d(TAG, "jsonString read from themoviedb.org is : " + jsonString);
        JSONObject movieJson = new JSONObject(jsonString);
        JSONArray movieArray = movieJson.getJSONArray(QUERY);
        JSONObject jsonObject;
        movieData = new MovieInfo[movieArray.length()];
        String sPath;
        String sId;
        String sPath_back;
        String title;
        String voteAver;
        String pubDate;
        String overView;
        for (int i = 0; i < movieArray.length(); i++) {
            jsonObject = movieArray.getJSONObject(i);
            sPath = BaseDataInfo.baseUrl + jsonObject.getString(POSTER_PATH);
            sId = BaseDataInfo.originUrl + jsonObject.getString(ID);
            title = jsonObject.getString(TITLE);
            sPath_back = BaseDataInfo.baseUrl + jsonObject.getString(BACK_PATH);
            overView = jsonObject.getString(OVER_VIEW);
            voteAver = jsonObject.getString(VOTE_AVERAGE);
            pubDate = jsonObject.getString(RELEASE_DATE);
            movieInfo = new MovieInfo(sId, sPath, title, sPath_back, overView, voteAver, pubDate,
                    0,FAVORITE_TEXT_DEFAULT);
            movieData[i] = movieInfo;
        }
        return movieData;
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
