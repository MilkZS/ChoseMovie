package com.example.android.chosemovie.utility;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.android.chosemovie.adapter.MovieTrailersAdapter;
import com.example.android.chosemovie.adapter.PicRecAdapter;
import com.example.android.chosemovie.adapter.MovieReviewsAdapter;
import com.example.android.chosemovie.base.MovieReviews;
import com.example.android.chosemovie.base.MovieSingleInfo;
import com.example.android.chosemovie.base.MovieTrailers;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by milkdz on 2018/1/28.
 */

public class MovieDetailSearchTask extends AsyncTask<String, Void, MovieSingleInfo>{

    private String TAG = "MovieDetailSearchTask";
    private boolean DBG = true;
    private OpenMovieInfoJson openMovieInfoJson;
    private final int ID_VIDEO = PicRecAdapter.ID_VIDEO;
    private final int ID_REVIEWS = PicRecAdapter.ID_REVIEWS;
    private MovieReviewsAdapter movieReviewsAdapter;
    private MovieTrailersAdapter movieTrailersAdapter;

    public MovieDetailSearchTask(OpenMovieInfoJson openMovieInfoJson,
                                 MovieReviewsAdapter movieReviewsAdapter,
                                 MovieTrailersAdapter movieTrailersAdapter) {
        this.openMovieInfoJson = openMovieInfoJson;
        this.movieReviewsAdapter = movieReviewsAdapter;
        this.movieTrailersAdapter = movieTrailersAdapter;
    }

    @Override
    protected MovieSingleInfo doInBackground(String... sid) {
        if (sid.length == 0) {
            return null;
        }
        String sId = sid[0];
        MovieReviews[] movieReviews = getMoviesReviews(sId);
        MovieTrailers movieTrailers = getMoviesTrailers(sId);
        MovieSingleInfo movieSingleInfo = new MovieSingleInfo(movieReviews, movieTrailers);
        return movieSingleInfo;
    }

    @Override
    protected void onPostExecute(MovieSingleInfo movieSingleInfo) {
        if (movieSingleInfo == null) {
            return;
        }
        movieReviewsAdapter.deliverData(movieSingleInfo.getMovieReviews());
        movieTrailersAdapter.deliverTrailers(movieSingleInfo.getMovieTrailers().getTrailersUrl());
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
