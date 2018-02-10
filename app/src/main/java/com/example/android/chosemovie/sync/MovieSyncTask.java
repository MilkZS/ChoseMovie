package com.example.android.chosemovie.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.android.chosemovie.data.BaseDataInfo;
import com.example.android.chosemovie.db.MovieInfoContract;
import com.example.android.chosemovie.utility.NetWorkUtils;
import com.example.android.chosemovie.utility.OpenMovieInfoJson;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

/**
 * Created by milkdz on 2018/2/7.
 */

public class MovieSyncTask {

    private static String TAG = "MovieSyncTask";
    private static boolean DBG = true;

    /**
     * Get each ContentValues
     *
     * @param openMovieInfoJson Movie json
     * @param mode search movie mode
     * @return each content values
     */
    @Nullable
    synchronized private static ContentValues[] getContentValues(OpenMovieInfoJson openMovieInfoJson, int mode,Context context) {
        if (DBG) Log.d(TAG, "run here getContentValues");
        URL url = NetWorkUtils.buildUrlForDifSort(mode);
        try {
            String sJson = NetWorkUtils.getResponseFromHttpUrl(url);
            ContentValues[] contentValues = openMovieInfoJson.getDataFromMovieJson(sJson, mode,context);
            return contentValues;
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Sync movie of each modes
     *
     * @param openMovieInfoJson movie json
     * @param context content
     */
    synchronized public static void syncMovie(OpenMovieInfoJson openMovieInfoJson, Context context) {

        if (DBG) Log.d(TAG, "run here syncMovie");
        ContentValues[] contentValues1 = getContentValues(openMovieInfoJson, BaseDataInfo.POPULAR_MODE,context);
        ContentValues[] contentValues2 = getContentValues(openMovieInfoJson, BaseDataInfo.RATE_DATE_MODE,context);
        ContentValues[] contentValues;
        if (contentValues1 == null && contentValues2 == null) {
            return;
        } else if (contentValues1 == null) {
            contentValues = contentValues1;
        } else if (contentValues2 == null) {
            contentValues = contentValues2;
        } else {
            int len1 = contentValues1.length;
            int len2 = contentValues2.length;
            contentValues1 = Arrays.copyOf(contentValues1, len1 + len2);
            System.arraycopy(contentValues2, 0, contentValues1, len1, len2);
            contentValues = contentValues1;
        }

        if (contentValues != null && contentValues.length != 0) {
            ContentResolver contentResolver = context.getContentResolver();
            contentResolver.delete(MovieInfoContract.MovieInfos.CONTENT_URI, null, null);
            contentResolver.bulkInsert(MovieInfoContract.MovieInfos.CONTENT_URI, contentValues);
        }
    }
}
