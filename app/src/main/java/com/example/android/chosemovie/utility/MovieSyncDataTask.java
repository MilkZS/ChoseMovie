package com.example.android.chosemovie.utility;

import android.content.Context;
import android.os.AsyncTask;

import com.example.android.chosemovie.sync.MovieSyncTask;

/**
 * Created by milkdz on 2018/2/11.
 */

public class MovieSyncDataTask extends AsyncTask<Void,Void,Void> {

    private OpenMovieInfoJson openMovieInfoJson;
    private Context context;

    public MovieSyncDataTask(OpenMovieInfoJson openMovieInfoJson, Context context){
        this.openMovieInfoJson = openMovieInfoJson;
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        MovieSyncTask.syncMovie(openMovieInfoJson,context);
        return null;
    }
}
