package com.example.android.chosemovie.utility;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.android.chosemovie.PicRecAdapter;
import com.example.android.chosemovie.R;
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

public class MovieDetailSearchTask extends AsyncTask<String, Void, MovieTrailers> implements View.OnClickListener{

    private String TAG = "MovieDetailSearchTask";
    private boolean DBG = true;
    private OpenMovieInfoJson openMovieInfoJson;
    private final int ID_VIDEO = PicRecAdapter.ID_VIDEO;
    private final int ID_REVIEWS = PicRecAdapter.ID_REVIEWS;
    private LinearLayout[] linearLayouts;
    private Context context;
    private ArrayList<Uri> uriArrayList;

    public MovieDetailSearchTask(OpenMovieInfoJson openMovieInfoJson, LinearLayout[] linearLayouts,
                                 Context context) {
        this.openMovieInfoJson = openMovieInfoJson;
        this.linearLayouts = linearLayouts;
        this.context = context;
    }

    @Override
    protected MovieTrailers doInBackground(String... sid) {

        if (sid.length == 0) {
            return null;
        }
        String sId = sid[0];
        MovieReviews[] movieReviews = getMoviesReviews(sId);
        MovieTrailers movieTrailers = getMoviesTrailers(sId);
        uriArrayList = movieTrailers.getTrailersUrl();
        int num = uriArrayList.size();
        if (DBG) Log.d(TAG, "====> num " + num);
        return movieTrailers;
    }

    @Override
    protected void onPostExecute(MovieTrailers movieTrailers) {
        if (movieTrailers == null) {
            return;
        }

        ArrayList<Uri> uriArrayList = movieTrailers.getTrailersUrl();
        int num = uriArrayList.size();
        for (int i = 0; i < num; i++) {
            for (int j=0;j<3;j++){
                if(i < num){
                    Button button = new Button(context);
                    button.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
                    String show = "Trailer " + (i + 1);
                    Log.d(TAG, show);
                    button.setText(show);
                    button.setTag(i);
                    button.setOnClickListener(this);
                    i++;
                    linearLayouts[j].addView(button);
                }else {
                    break;
                }
            }
            i --;
        }
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

    /**
     * Open the web page
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        int iTag = (int) v.getTag();
        if(uriArrayList == null){
            return;
        }
        Uri uri = uriArrayList.get(iTag);
        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
        /*
         * This is a check we perform with every implicit Intent that we launch. In some cases,
         * the device where this code is running might not have an Activity to perform the action
         * with the data we've specified. Without this check, in those cases your app would crash.
         */
        if(intent.resolveActivity(context.getPackageManager()) != null){
            context.startActivity(intent);
        }
    }
}
