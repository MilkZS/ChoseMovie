package com.example.android.chosemovie.utility;

import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.android.chosemovie.PicRecAdapter;
import com.example.android.chosemovie.base.MovieReviews;
import com.example.android.chosemovie.base.MovieSingleInfo;
import com.example.android.chosemovie.base.MovieTrailers;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

/**
 * Created by milkdz on 2018/1/28.
 */

public class MovieDetailSearchTask extends AsyncTask<String, Void, MovieSingleInfo> {

    private String TAG = "MovieDetailSearchTask";
    private boolean DBG = true;
    private OpenMovieInfoJson openMovieInfoJson;
    private final int ID_VIDEO = PicRecAdapter.ID_VIDEO;
    private final int ID_REVIEWS = PicRecAdapter.ID_REVIEWS;


    public MovieDetailSearchTask(OpenMovieInfoJson openMovieInfoJson) {
        this.openMovieInfoJson = openMovieInfoJson;
    }

    @Override
    protected MovieSingleInfo doInBackground(String... sid) {

        if (sid.length == 0) {
            return null;
        }
        String sId = sid[0];
        MovieReviews[] movieReviews = getMoviesReviews(sId);
        MovieTrailers movieTrailers = getMoviesTrailers(sId);
        return new MovieSingleInfo(movieReviews, movieTrailers);
    }

    /**
     * get movie reviews array
     *
     * @param sId movie id
     * @return array of MovieReviews
     */
    @Nullable
    private MovieReviews[] getMoviesReviews(String sId) {

        URL url = NetWorkUtils.buildUrlForDifSort(ID_REVIEWS, sId);
        MovieReviews[] movieReviews;
        try {
            String jsonData = NetWorkUtils.getResponseFromHttpUrl(url);
            movieReviews = openMovieInfoJson.getMovieReviews(jsonData);
            return movieReviews;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get object of MoviesTrailers
     *
     * @param sId movie id
     * @return object
     */
    @Nullable
    private MovieTrailers getMoviesTrailers(String sId) {
        URL url = NetWorkUtils.buildUrlForDifSort(ID_VIDEO, sId);
        try {
            String jsonData = NetWorkUtils.getResponseFromHttpUrl(url);
            MovieTrailers movieTrailers = openMovieInfoJson.getMovieTrailers(jsonData);
            return movieTrailers;
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}