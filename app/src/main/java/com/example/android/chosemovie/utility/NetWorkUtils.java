package com.example.android.chosemovie.utility;

import android.net.Uri;
import android.util.Log;

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

    final static String TAG = "MetWorkUtils";

    /**
     * build the URL for themoviedb.org
     *
     * @param chose number to chose the sort method
     * @return the URL of chose
     */
    public static URL buildUrlForPopular(int chose,String sID){

        String sBaseSearchQuery = null;
        switch (chose){
            case 0:{
                sBaseSearchQuery = BaseDataInfo.searchQueryPopularFromDB;
            }break;
            case 1:{
                sBaseSearchQuery = BaseDataInfo.searchQueryTopRateFromDB;
            }break;
            case 2:{
                sBaseSearchQuery = sID + BaseDataInfo.VIDEO;
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
    public static URL buildUrlForPopular(int chose){
        return buildUrlForPopular(chose,null);
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
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
