package com.waleed.worldclock;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class DBManager {
    private DatabaseHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public DBManager(Context c) {
        context = c;
    }

    public DBManager open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void update(ArrayList<String> selected) {
        database.execSQL("DELETE FROM " + DatabaseHelper.TABLE_NAME);

        for (String s : selected) {
            ContentValues contentValue = new ContentValues();
            contentValue.put(DatabaseHelper.CITY, s);
            database.insert(DatabaseHelper.TABLE_NAME, null, contentValue);
        }
    }

    public ArrayList<String> fetch() {
        String[] columns = new String[] { DatabaseHelper._ID, DatabaseHelper.CITY };
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, columns, null, null, null, null, null);
        ArrayList<String> s = new ArrayList<String>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String name = cursor.getString(1);
                s.add(name);
                cursor.moveToNext();
            }
        }
        return s;
    }
}
