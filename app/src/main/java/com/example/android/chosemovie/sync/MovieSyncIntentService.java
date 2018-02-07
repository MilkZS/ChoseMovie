package com.example.android.chosemovie.sync;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.example.android.chosemovie.utility.OpenMovieInfoJson;

/**
 * Created by milkdz on 2018/2/7.
 */

public class MovieSyncIntentService extends IntentService {

    OpenMovieInfoJson openMovieInfoJson = OpenMovieInfoJson.getInstance();

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public MovieSyncIntentService() {
        super("MovieSyncIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        MovieSyncTask.syncMovie(openMovieInfoJson,this);
    }
}
