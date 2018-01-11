package com.example.android.chosemovie.utility;

import android.util.Log;

import com.example.android.chosemovie.base.MovieInfo;
import com.example.android.chosemovie.data.BaseDataInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by milkdz on 2018/1/7.
 */

public class OpenMovieInfoJson {

    private String TAG = "OpenMovieInfoJson";

    public static OpenMovieInfoJson openMovieInfoJson;

    private MovieInfo movieInfo;
    private OpenMovieInfoJson(){
    }

    public static OpenMovieInfoJson getInstance() {

        if(openMovieInfoJson == null){
            return new OpenMovieInfoJson();
        }
        return openMovieInfoJson;
    }

    private String QUERY = "results";
    private String POSTER_PATH = "poster_path";
    private String ID = "id";
    private String TITLE = "title";
    private String BACK_PATH = "backdrop_path";
    private String OVER_VIEW = "overview";
    private String VOTE_AVERAGE = "vote_average";
    private String RELEASE_DATE = "release_date";

    /**
     * get data from json
     *
     * @param jsonString json read from themoviedb.org
     * @return the object of MovieInfo
     * @throws JSONException
     */
    public MovieInfo[] getDataFromMovieJson(String jsonString) throws JSONException {

        MovieInfo[] movieData;
        Log.d(TAG,"jsonString read from themoviedb.org is : " + jsonString);
        JSONObject movieJson = new JSONObject(jsonString);
        JSONArray movieArray = movieJson.getJSONArray(QUERY);
        JSONObject jsonObject ;
        movieData = new MovieInfo[movieArray.length()];
        String sPath;
        String sId;
        String sPath_back;
        String title;
        String voteAver;
        String pubDate;
        String overView;
        for (int i=0;i<movieArray.length();i++){
            jsonObject = movieArray.getJSONObject(i);
            sPath = BaseDataInfo.baseUrl + jsonObject.getString(POSTER_PATH);
            sId = BaseDataInfo.originUrl + jsonObject.getString(ID);
            title = jsonObject.getString(TITLE);
            sPath_back =BaseDataInfo.baseUrl + jsonObject.getString(BACK_PATH);
            overView = jsonObject.getString(OVER_VIEW);
            voteAver = jsonObject.getString(VOTE_AVERAGE);
            pubDate = jsonObject.getString(RELEASE_DATE);
            movieInfo = new MovieInfo(sId,sPath,title,sPath_back,overView,voteAver,pubDate);
            movieData[i] = movieInfo;
        }
        return movieData;
    }
}
