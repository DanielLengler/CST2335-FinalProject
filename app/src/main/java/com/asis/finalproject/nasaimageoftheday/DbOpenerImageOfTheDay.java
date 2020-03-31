package com.asis.finalproject.nasaimageoftheday;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Helper class for managing the database.
 */
public class DbOpenerImageOfTheDay extends SQLiteOpenHelper {

    private final static String DATABASE_NAME = "NasaImagesDB";
    private final static int VERSION_NUM = 1;
    protected final static String TABLE_NAME = "NasaImagesList";
    protected final static String COL_DATE = "DATE";
    protected final static String COL_EXPLANATION = "EXPLANATION";
    protected final static String COL_URL = "URL";
    protected final static String COL_TITLE = "TITLE";
    protected final static String COL_PATH = "PATH";
    protected final static String COL_ID = "_id";

    protected DbOpenerImageOfTheDay(Context ctx)
    {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }


    //This function gets called if no database file exists.
    //Look on your device in the /data/data/package-name/database directory.
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_DATE + " text,"
                + COL_EXPLANATION + " text,"
                + COL_URL + " text,"
                + COL_TITLE + " text,"
                + COL_PATH  + " text);");  // add or remove columns
    }


    //this function gets called if the database version on your device is lower than VERSION_NUM
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {   //Drop the old table:
        db.execSQL( "DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create the new table:
        onCreate(db);
    }

    //this function gets called if the database version on your device is higher than VERSION_NUM
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {   //Drop the old table:
        db.execSQL( "DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create the new table:
        onCreate(db);
    }
}