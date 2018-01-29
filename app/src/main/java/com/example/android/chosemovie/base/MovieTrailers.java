package com.example.android.chosemovie.base;

import android.net.Uri;

import java.util.ArrayList;

/**
 * Created by milkdz on 2018/1/29.
 */

public class MovieTrailers {
    private ArrayList<Uri> TrailersUrl;

    public MovieTrailers(ArrayList<Uri> trailersUrl) {
        TrailersUrl = trailersUrl;
    }

    public ArrayList<Uri> getTrailersUrl() {
        return TrailersUrl;
    }
}
