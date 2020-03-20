package com.asis.finalproject.nasaearthimage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

/**
 * Helper class for managing the database.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "nasa_earth_image_db";
    private static final int VERSION_NUM = 1;

    private static final String TABLE_NAME = "Favourites";
    static final String COL_ID = "ID";
    static final String COL_IMAGE_PATH = "PATH";
    static final String COL_LATITUDE = "LATITUDE";
    static final String COL_LONGITUDE = "LONGITUDE";
    static final String COL_DATE = "DATE";

    DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+TABLE_NAME+" (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_IMAGE_PATH + " TEXT," +
                COL_DATE +" TEXT,"+
                COL_LATITUDE +" TEXT,"+
                COL_LONGITUDE + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * Added a new row to the database
     * @param nasaEarthImage - The object to insert into the database
     * @return the id of the inserted row
     */
    long insertImage(NasaEarthImage nasaEarthImage) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_IMAGE_PATH, nasaEarthImage.getPath());
        contentValues.put(COL_DATE, nasaEarthImage.getDate().getTimeInMillis());
        contentValues.put(COL_LATITUDE, nasaEarthImage.getLatitude());
        contentValues.put(COL_LONGITUDE, nasaEarthImage.getLongitude());
        return getWritableDatabase().insert(TABLE_NAME, null, contentValues);
    }

    /**
     * Returns all rows in the database
     * @return a cursor with all the rows from TABLE_NAME.
     */
    Cursor getAll() {
        return getReadableDatabase().rawQuery("SELECT * FROM "+TABLE_NAME, null);
    }

    /**
     * Deletes a row from the database
     * @param nasaEarthImage - the object containing the id of the row to delete
     * @return the number of rows effected
     */
    long deleteImage(NasaEarthImage nasaEarthImage) {
        return getWritableDatabase().delete(TABLE_NAME,COL_ID+"=?",new String[]{String.valueOf(nasaEarthImage.getId())});
    }

    /**
     * Updates a row in the database based on the argument
     * @param nasaEarthImage - the object containing the new data
     */
    void update(NasaEarthImage nasaEarthImage) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_IMAGE_PATH, nasaEarthImage.getPath());
        contentValues.put(COL_DATE, nasaEarthImage.getDate().getTimeInMillis());
        contentValues.put(COL_LATITUDE, nasaEarthImage.getLatitude());
        contentValues.put(COL_LONGITUDE, nasaEarthImage.getLongitude());
        getWritableDatabase().update(TABLE_NAME, contentValues, COL_ID+"="+nasaEarthImage.getId(), null);
    }

}
