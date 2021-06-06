package com.waleed.worldclock;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String SELECTED = "SELECTED";
    public static final String TZ_MAP = "TZMAPS";

    public static final String _ID = "_id";
    public static final String CITY = "city";
    public static final String TMZN = "timezone";

    static final String DB_NAME = "CLOCK";

    static final int DB_VERSION = 1;

    private static final String CREATE_TABLE_1 = "create table " + SELECTED + "(" + _ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + CITY + " TEXT NOT NULL);";

    private static final String CREATE_TABLE_2 = "create table " + TZ_MAP + "(" + _ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +  TMZN + " TEXT NOT NULL );";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_1);
        db.execSQL(CREATE_TABLE_2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + SELECTED);
        db.execSQL("DROP TABLE IF EXISTS " + TZ_MAP);
        onCreate(db);
    }
}

