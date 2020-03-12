package com.asis.finalproject.nasaearthimage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "nasa_earth_image_db";
    private static final int VERSION_NUM = 1;

    private static final String TABLE_NAME = "Favourites";
    static final String COL_ID = "ID";
    static final String COL_IMAGE_PATH = "PATH";
    static final String COL_LATITUDE = "LATITUDE";
    static final String COL_LONGITUDE = "LONGITUDE";

    DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+TABLE_NAME+" (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_IMAGE_PATH + " TEXT," +
                COL_LATITUDE +" TEXT,"+
                COL_LONGITUDE + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    long insertImage(NasaEarthImage nasaEarthImage) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_IMAGE_PATH, nasaEarthImage.getPath());
        contentValues.put(COL_LATITUDE, nasaEarthImage.getLatitude());
        contentValues.put(COL_LONGITUDE, nasaEarthImage.getLongitude());
        return getWritableDatabase().insert(TABLE_NAME, null, contentValues);
    }

    Cursor getAll() {
        return getReadableDatabase().rawQuery("SELECT * FROM "+TABLE_NAME, null);
    }

    long deleteImage(NasaEarthImage nasaEarthImage) {
        return getWritableDatabase().delete(TABLE_NAME,COL_ID+"=?",new String[]{String.valueOf(nasaEarthImage.getId())});
    }

}
