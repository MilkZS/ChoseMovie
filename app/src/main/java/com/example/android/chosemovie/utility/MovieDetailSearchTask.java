package com.example.android.chosemovie.utility;

import android.os.AsyncTask;
import android.util.Log;

import com.example.android.chosemovie.PicRecAdapter;
import com.example.android.chosemovie.base.MovieReviews;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

/**
 * Created by milkdz on 2018/1/28.
 */

public class MovieDetailSearchTask extends AsyncTask<Integer, Void, Object[]> {

    private String TAG = "MovieDetailSearchTask";
    private boolean DBG = true;
    private OpenMovieInfoJson openMovieInfoJson;
    private final int ID_VIDEO = PicRecAdapter.ID_VIDEO;
    private final int ID_REVIEWS = PicRecAdapter.ID_REVIEWS;
    private String SID;


    public MovieDetailSearchTask(String id, OpenMovieInfoJson openMovieInfoJson) {
        this.SID = id;
        this.openMovieInfoJson = openMovieInfoJson;
    }

    @Override
    protected Object[] doInBackground(Integer... integers) {

        if (integers.length == 0) {
            return null;
        }

        int choseMood = integers[0];
        String jsonData;
        switch (choseMood) {
            case ID_VIDEO: {
                URL url = NetWorkUtils.buildUrlForDifSort(choseMood, SID);
                try {
                    jsonData = NetWorkUtils.getResponseFromHttpUrl(url);
                    if (DBG)  Log.d(TAG,"------json_video----- "+jsonData);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            break;
            case ID_REVIEWS: {
                URL url = NetWorkUtils.buildUrlForDifSort(choseMood, SID);
                try {
                    jsonData = NetWorkUtils.getResponseFromHttpUrl(url);
                    MovieReviews[] movieReviewsArr = openMovieInfoJson.getMovieReviews(jsonData);
                    return movieReviewsArr;
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            break;
        }
        return null;
    }
}
