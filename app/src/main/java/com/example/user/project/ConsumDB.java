package com.example.user.project;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ConsumDB extends SQLiteOpenHelper {

    public ConsumDB(Context context) {
        super(context, "consum.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table consum(_id integer primary key autoincrement,  fcal text, date text);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
