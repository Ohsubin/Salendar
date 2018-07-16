package com.example.user.project;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class WalkDB extends SQLiteOpenHelper {

    public WalkDB(Context context) {
        super(context, "walk.db",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table walk(_id integer primary key autoincrement, walk text, date text);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
