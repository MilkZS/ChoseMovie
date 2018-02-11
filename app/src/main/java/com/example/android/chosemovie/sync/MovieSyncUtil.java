package com.example.android.chosemovie.sync;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.example.android.chosemovie.data.BaseDataInfo;
import com.example.android.chosemovie.db.MovieInfoContract;

/**
 * Created by milkdz on 2018/2/8.
 */

public class MovieSyncUtil {

    private static String TAG = "MovieSyncUtil";
    public static int MODE = BaseDataInfo.POPULAR_MODE;

    public static void startIntentService(final Context context) {
        Intent intent = new Intent(context, MovieSyncIntentService.class);
        context.startService(intent);
    }

    synchronized public static void initialize(final Context context, final int mode) {

        Log.d(TAG, "initialize" + mode);
        if (mode == MODE) {
            return;
        }
        MODE = mode;

        Log.d(TAG, "initialize" + mode);
        Thread syncForFirst = new Thread(new Runnable() {
            @Override
            public void run() {
                Uri uri = MovieInfoContract.MovieInfos.CONTENT_URI;
                String[] projectionColu = null;
                String select = null;
                projectionColu = new String[]{MovieInfoContract.MovieInfos.COLUMN_MOVIE_ID};
                select = MovieInfoContract.getSelect(mode);
                Log.d(TAG, "initialize --- " + select);
                Log.d(TAG, "initialize --- " + uri);
                Cursor cursor = context.getContentResolver().query(uri, projectionColu, select, null, null);

                if (null == cursor || cursor.getCount() == 0) {
                    Log.d(TAG, "cursor.getCount() : " + cursor.getCount());
                    startIntentService(context);
                }
                if (cursor != null) {
                    cursor.close();
                }
            }
        });
        syncForFirst.start();
    }
}