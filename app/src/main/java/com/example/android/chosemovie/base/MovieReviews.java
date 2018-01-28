package com.example.android.chosemovie.base;

/**
 * Created by milkdz on 2018/1/28.
 */

public class MovieReviews {
    private String author;
    private String content;

    public MovieReviews(String author, String content) {
        this.author = author;
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

}
