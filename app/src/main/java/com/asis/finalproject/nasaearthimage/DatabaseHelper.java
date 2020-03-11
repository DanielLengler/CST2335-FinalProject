package com.asis.finalproject.nasaearthimage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.Arrays;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "nasa_earth_image_db";
    private static final int VERSION_NUM = 1;

    private static final String TABLE_NAME = "Favourites";
    public static final String COL_ID = "ID";
    public static final String COL_TEXT = "TEXT";
    public static final String COL_TYPE = "TYPE";

    DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+TABLE_NAME+" (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_TEXT + " TEXT," +
                COL_TYPE +" TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    static void printCursor(Cursor cursor, int version) {
        Log.e("DatabaseHelper", "Version Number: "+version);
        Log.e("DatabaseHelper", "Column Amount: "+cursor.getColumnCount());
        Log.e("DatabaseHelper", "Column Names: "+ Arrays.toString(cursor.getColumnNames()));
        Log.e("DatabaseHelper", "Cursor result count: "+ cursor.getCount());

        cursor.moveToFirst();
        for(int i=0; i < cursor.getCount(); i++) {
            Log.e("DatabaseHelper", "Row "+i+" ----------------");
            Log.e("DatabaseHelper", COL_ID+": "+ cursor.getLong(cursor.getColumnIndex(COL_ID)));
            Log.e("DatabaseHelper", COL_TEXT+": "+ cursor.getString(cursor.getColumnIndex(COL_TEXT)));
            Log.e("DatabaseHelper", COL_TYPE+": "+ cursor.getString(cursor.getColumnIndex(COL_TYPE)));
            cursor.moveToNext();
        }
    }

    long insertMessage(Message message) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_TEXT, message.getMessage());
        contentValues.put(COL_TYPE, message.getMessageType());
        return getWritableDatabase().insert(TABLE_NAME, "NullColumnHack", contentValues);
    }

    Cursor getMessages() {
        return getReadableDatabase().rawQuery("SELECT * FROM "+TABLE_NAME, null);
    }

    long deleteMessage(Message message) {
        return getWritableDatabase().delete(TABLE_NAME,COL_ID+"=?",new String[]{String.valueOf(message.getId())});
    }

}
