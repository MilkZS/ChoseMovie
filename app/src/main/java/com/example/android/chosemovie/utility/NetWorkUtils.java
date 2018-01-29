package com.example.android.chosemovie.utility;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.android.chosemovie.PicRecAdapter;
import com.example.android.chosemovie.data.BaseDataInfo;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by milkdz on 2018/1/7.
 */

public class NetWorkUtils {

    private final static String TAG = "MetWorkUtils";
    private final static int POPULAR_MODE = PicRecAdapter.POPULAR_MODE;
    private final static int RATE_DATA_MODE = PicRecAdapter.RATE_DATE_MODE;
    private final static int ID_VIDEO = PicRecAdapter.ID_VIDEO;
    private final static int ID_REVIEWS = PicRecAdapter.ID_REVIEWS;

    /**
     * build the URL for themoviedb.org
     *
     * @param chose number to chose the sort method
     * @return the URL of chose
     */
    public static URL buildUrlForDifSort(int chose,String sID){

        String sBaseSearchQuery = null;
        switch (chose){
            case POPULAR_MODE:{
                sBaseSearchQuery = BaseDataInfo.searchQueryPopularFromDB;
            }break;
            case RATE_DATA_MODE:{
                sBaseSearchQuery = BaseDataInfo.searchQueryTopRateFromDB;
            }break;
            case ID_VIDEO:{
                sBaseSearchQuery = sID + BaseDataInfo.VIDEO;
            }break;
            case ID_REVIEWS:{
                sBaseSearchQuery = sID + BaseDataInfo.REVIEWS;
            }break;
        }

        Uri builtUri = Uri.parse(sBaseSearchQuery).buildUpon()
                .appendQueryParameter(BaseDataInfo.apiKey, BaseDataInfo.myPrivateKey)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "Built URI : " + url);
        return url;
    }
    public static URL buildUrlForDifSort(int chose){
        return buildUrlForDifSort(chose,null);
    }

    /**
     * build uri for youtube search
     *
     * @param sKey video key
     * @return youtube uri
     */
    public static Uri buildUrlForYoutube(String sKey){

        Uri builtUri = Uri.parse(BaseDataInfo.youtubeBaseUrl).buildUpon()
                .appendQueryParameter(BaseDataInfo.youtubeVideo,sKey)
                .build();

        return builtUri;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    @Nullable
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setConnectTimeout(10000);
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
