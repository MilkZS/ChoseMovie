package com.example.android.chosemovie.data;

/**
 * Created by milkdz on 2018/2/4.
 */

public interface SQLBaseInfo {

    String CREATE_TABLE = "CREATE TABLE ";

    String INT_PRIMARY_KEY = " INTEGER PRIMARY KEY AUTOINCREMENT, ";

    String TEXT_NO_NULL = " TEXT NOT NULL, ";

    String DROP_TABLE = " DROP TABLE IF EXISTS ";
}
