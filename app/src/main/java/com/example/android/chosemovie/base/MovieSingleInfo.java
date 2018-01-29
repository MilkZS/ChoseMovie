package com.example.android.chosemovie.base;

/**
 * Created by milkdz on 2018/1/29.
 */

public class MovieSingleInfo {
    private MovieReviews[] movieReviews;
    private MovieTrailers movieTrailers;

    public MovieSingleInfo(MovieReviews[] movieReviews, MovieTrailers movieTrailers) {
        this.movieReviews = movieReviews;
        this.movieTrailers = movieTrailers;
    }

    public MovieReviews[] getMovieReviews() {
        return movieReviews;
    }

    public MovieTrailers getMovieTrailers() {
        return movieTrailers;
    }
}
