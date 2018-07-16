package com.example.user.project;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FoodDB extends SQLiteOpenHelper{

    public FoodDB(Context context) {
        super(context, "salendar.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table cal(_id integer primary key autoincrement, fname text, fcal text, fgram text);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
