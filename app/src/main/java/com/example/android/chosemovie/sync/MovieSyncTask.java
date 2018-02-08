package com.example.android.chosemovie.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.example.android.chosemovie.data.BaseDataInfo;
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

    private static String TAG = "MovieSyncTask";
    private static boolean DBG = true;

    synchronized public static void syncMovie(OpenMovieInfoJson openMovieInfoJson, Context context) {

        syncMovie(openMovieInfoJson,context,BaseDataInfo.RATE_DATE_MODE);
        syncMovie(openMovieInfoJson,context,BaseDataInfo.POPULAR_MODE);
    }

    synchronized private static void syncMovie(OpenMovieInfoJson openMovieInfoJson, Context context,int mode){
        try {
            if (DBG) Log.d(TAG,"run here");
            URL url = NetWorkUtils.buildUrlForDifSort(mode);
            String sjson = NetWorkUtils.getResponseFromHttpUrl(url);
            ContentValues[] contentValues = openMovieInfoJson.getDataFromMovieJson(sjson,mode);
            if (contentValues != null && contentValues.length != 0){
                ContentResolver contentResolver = context.getContentResolver();
                contentResolver.delete(MovieInfoContract.MovieInfos.CONTENT_URI,null,null);
                contentResolver.bulkInsert(MovieInfoContract.MovieInfos.CONTENT_URI,contentValues);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
