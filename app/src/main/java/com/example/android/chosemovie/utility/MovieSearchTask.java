package com.example.android.chosemovie.utility;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import com.example.android.chosemovie.adapter.PicRecAdapter;
import com.example.android.chosemovie.base.MovieInfo;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

/**
 * Created by milkdz on 2018/1/11.
 */

public class MovieSearchTask extends AsyncTask<Integer, Void, MovieInfo[]> {

    private ProgressBar progressBar;
    private PicRecAdapter imageAdapter;
    private OpenMovieInfoJson openMovieInfoJson;

    public MovieSearchTask(ProgressBar progressBar
            ,PicRecAdapter imageAdapter,OpenMovieInfoJson openMovieInfoJson){
        this.progressBar = progressBar;
        this.imageAdapter = imageAdapter;
        this.openMovieInfoJson = openMovieInfoJson;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        showProgressBar();
    }

    @Override
    protected MovieInfo[] doInBackground(Integer... choseMode) {

        if (choseMode.length == 0) {
            return null;
        }

        URL url = NetWorkUtils.buildUrlForDifSort(choseMode[0]);
        String sJsonData;
        MovieInfo[] movieData = null;
        try {
            sJsonData = NetWorkUtils.getResponseFromHttpUrl(url);
            movieData = openMovieInfoJson.getDataFromMovieJson(sJsonData);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return movieData;
    }

    @Override
    protected void onPostExecute(MovieInfo[] movieData) {
        if (movieData != null) {
            hideProgressBar();
            imageAdapter.setData(movieData);
        }
    }

    private void showProgressBar(){
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar(){
        progressBar.setVisibility(View.INVISIBLE);
    }
}