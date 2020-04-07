package com.asis.finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Class helps to create, update a database
 */
public class BbcFavDB extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "BbcNewsArticlesDB";
    private static final String TABLE_NAME = "BbcNewsArticles";
    public static final String COL_ID = "id";
    public static final String COL_TITLE = "title";
    public static final String COL_PUBDATE = "pubDate";
    public static final String COL_DESCRIPTION = "description";
    public static final String COL_URL = "weblink";
    public static String FAVORITE_STATUS = "fStatus";

    BbcFavDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_NAME + "( "
                + COL_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_TITLE + " TEXT, " + COL_PUBDATE + " TEXT, "
                + COL_DESCRIPTION + " TEXT, " + COL_URL + " TEXT, " + FAVORITE_STATUS + " TEXT)";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
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
    void addArticles(BbcFavItem bbcFavItem) {
        ContentValues values = new ContentValues();
        values.put(COL_TITLE, bbcFavItem.getFav_title());
        values.put(COL_PUBDATE, bbcFavItem.getFav_pubDate());
        values.put(COL_DESCRIPTION, bbcFavItem.getFav_description());
        values.put(COL_URL, bbcFavItem.getFav_webUrl());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_NAME, null, values);

    }
    void updateArticles(BbcFavItem bbcFavItem) {
        ContentValues values = new ContentValues();
        values.put(COL_TITLE, bbcFavItem.getFav_title());
        values.put(COL_PUBDATE, bbcFavItem.getFav_pubDate());
        values.put(COL_DESCRIPTION, bbcFavItem.getFav_description());
        values.put(COL_URL, bbcFavItem.getFav_webUrl());
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(TABLE_NAME, values, COL_ID + " = ?", new String[]{String.valueOf(bbcFavItem.getId())});
    }
    void deleteArticle(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COL_ID + " = ?", new String[]{String.valueOf(id)});
    }

//    // read all data
//    public Cursor read_all_data(String id) {
//        SQLiteDatabase db = this.getReadableDatabase();
//        String sql = "select * from " + TABLE_NAME + " where " + KEY_ID+"='"+id+"'" ;
//        return db.rawQuery(sql,null,null);
//    }
//    // remove line from database
//    public void remove_fav(String id) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        String sql = "UPDATE " + TABLE_NAME + " SET  "+ FAVORITE_STATUS+" ='0' WHERE "+KEY_ID+"='"+id+"'";
//        db.execSQL(sql);
//        Log.d("remove", id.toString());
//    }
    // select all favorite list
    public Cursor select_all_favorite_list() {
        SQLiteDatabase db = this.getReadableDatabase();
//        String sql = "SELECT * FROM "+TABLE_NAME+" WHERE "+FAVORITE_STATUS+" ='1'";
        String sql = "SELECT * FROM "+TABLE_NAME;
        return db.rawQuery(sql,null);
    }


}
