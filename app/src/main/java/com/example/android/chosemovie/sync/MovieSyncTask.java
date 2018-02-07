package com.example.android.chosemovie.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;

import com.example.android.chosemovie.db.MovieInfoContract;
import com.example.android.chosemovie.utility.NetWorkUtils;
import com.example.android.chosemovie.utility.OpenMovieInfoJson;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

/**
 * Created by milkdz on 2018/2/7.
 */

public class MovieSyncTask {

    synchronized public static void syncMovie(OpenMovieInfoJson openMovieInfoJson, Context context) {
        try {
            URL url = NetWorkUtils.buildUrlForDifSort(0);
            String sjson = NetWorkUtils.getResponseFromHttpUrl(url);
            ContentValues[] contentValues = openMovieInfoJson.getDataFromMovieJson(sjson);
            if (contentValues != null && contentValues.length != 0){
                ContentResolver contentResolver = context.getContentResolver();
                contentResolver.delete(MovieInfoContract.MovieInfos.CONTENT_URI,null,null);
                contentResolver.bulkInsert(MovieInfoContract.MovieInfos.CONTENT_URI,contentValues);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
    }
}
