package com.example.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "BinaryTreeDB";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "binary_tree";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_VALUE = "value";
    private static final String COLUMN_LEFT = "left";
    private static final String COLUMN_RIGHT = "right";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database upgrade if needed
    }
}