package com.asis.finalproject.bbc;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Class helps to create, update a database
 */
public class BbcFavDB extends SQLiteOpenHelper {
    /**
     * database version
     */
    private static final int DATABASE_VERSION = 1;
    /**
     * database name
     */
    private static final String DATABASE_NAME = "BbcNewsArticlesDB";
    /**
     * table name
     */
    private static final String TABLE_NAME = "BbcNewsArticles";

    public static final String COL_ID = "id";
    /**
     * column name in a table
     */
    public static final String COL_TITLE = "title";
    /**
     * column name in a table
     */
    public static final String COL_PUBDATE = "pubDate";
    /**
     * column name in a table
     */
    public static final String COL_DESCRIPTION = "description";
    /**
     * column name in a table
     */
    public static final String COL_URL = "weblink";


    /**
     * Constructor
     * @param context content of the objects in DB
     */

    BbcFavDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This method gets called if no database file exists.
     * @param db represents database
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_BBC_ARTICLE_TABLE = "CREATE TABLE " + TABLE_NAME + "( "
                + COL_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_TITLE + " TEXT, " + COL_PUBDATE + " TEXT, "
                + COL_DESCRIPTION + " TEXT, " + COL_URL  + " TEXT)";
        db.execSQL(CREATE_BBC_ARTICLE_TABLE);
    }

    /**
     * This method gets called if the database version in the  device is lower than DATABASE_VERSION
     * @param db represents database
     * @param oldVersion represents old version of database
     * @param newVersion represents new version of database
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    /**
     * This method creates an ArrayList of favorite articles
     * @return the list of articles
     */
    ArrayList<BbcFavItem> listFavItems() {
        String sql = "select * from " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<BbcFavItem> storeFavArticles = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                int id = Integer.parseInt(cursor.getString(0));
                String title = cursor.getString(1);
                String pubDate = cursor.getString(2);
                String description = cursor.getString(3);
                String webUrl = cursor.getString(4);
                storeFavArticles.add(new BbcFavItem(title, pubDate, description, webUrl));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return storeFavArticles;
    }

    /**
     * This method adds a new favorite article to the favorite list
     * @param bbcFavItem represents a favorite item that is added to favorite list
     */
    void addArticles(BbcFavItem bbcFavItem) {
        ContentValues values = new ContentValues();
        values.put(COL_TITLE, bbcFavItem.getFav_title());
        values.put(COL_PUBDATE, bbcFavItem.getFav_pubDate());
        values.put(COL_DESCRIPTION, bbcFavItem.getFav_description());
        values.put(COL_URL, bbcFavItem.getFav_webUrl());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_NAME, null, values);

    }

    /**
     * This method gets called when the database is changed
     * @param bbcFavItem represents a favorite item from favorite list
     */
    void updateArticles(BbcFavItem bbcFavItem) {
        ContentValues values = new ContentValues();
        values.put(COL_TITLE, bbcFavItem.getFav_title());
        values.put(COL_PUBDATE, bbcFavItem.getFav_pubDate());
        values.put(COL_DESCRIPTION, bbcFavItem.getFav_description());
        values.put(COL_URL, bbcFavItem.getFav_webUrl());
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(TABLE_NAME, values, COL_ID + " = ?", new String[]{String.valueOf(bbcFavItem.getId())});
    }

    /**
     * This method is called when a favorite article is deleted from the list
     * @param id represents an id of an article that is deleted
     */
    void deleteArticle(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COL_ID + " = ?", new String[]{String.valueOf(id)});
    }

    /**
     * This method is called when the favorite articles are selected
     * @return list of favorite articles
     */
    public Cursor select_all_favorite_list() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM "+TABLE_NAME;
        return db.rawQuery(sql,null);
    }


}
