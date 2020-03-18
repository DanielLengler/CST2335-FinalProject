package com.asis.finalproject.guardian;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyOpener extends SQLiteOpenHelper {

    protected static final String DATABASE_NAME = "FavoritesDB";
    protected static final int VERSION_NUM = 1;
    public static final String TABLE_NAME = "Favorites";
    public static final String COL_ID = "_id";
    public static final String COL_TITLE = "Title";
    public static final String COL_URL = "Url";
    public static final String COL_SECTION_NAME = "Section_Name";

    public MyOpener(Context ctx){super(ctx, DATABASE_NAME, null, VERSION_NUM);}

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_TITLE + " text,"
                + COL_URL + " text," + COL_SECTION_NAME + " text);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
