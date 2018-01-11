package com.example.android.chosemovie.base;

import java.io.Serializable;

/**
 * Created by milkdz on 2018/1/7.
 */

public class MovieInfo implements Serializable {
    private String id;
    private String path;
    private String title;
    private String path_back;
    private String overView;
    private String voteAver;
    private String pubDate;

    public MovieInfo(String id,String path,String title,String path_back,String overView,String voteAver,String pubDate){
        this.id = id;
        this.path = path;
        this.title = title;
        this.path_back = path_back;
        this.overView = overView;
        this.voteAver = voteAver;
        this.pubDate = pubDate;
    }
    public void setId(String id){
        this.id = id;
    }

    public String getId(){
        return id;
    }

    public String getPath(){
        return path;
    }

    public String getTitle(){
        return title;
    }

    public String getPath_back(){
        return path_back;
    }

    public String getOverView(){
        return overView;
    }

    public String getVoteAver(){
        return voteAver;
    }

    public String getPubDate(){
        return pubDate;
    }
}
