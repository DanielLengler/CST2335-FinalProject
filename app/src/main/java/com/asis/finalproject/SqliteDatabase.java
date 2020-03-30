package com.asis.finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class SqliteDatabase extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 5;
    private static final String DATABASE_NAME = "BbcNewsArticlesDB";
    private static final int VERSION_NUM = 1;
    private static final String TABLE_NAME = "BbcNewsArticles";
    private static final String COL_ID = "_id";
    private static final String COL_TITLE = "title";
    private static final String COL_PUBDATE = "pubDate";
    private static final String COL_DESCRIPTION = "description";
    private static final String COL_URL = "weblink";

    SqliteDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_NAME + "( "
                + COL_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_TITLE + " TEXT, " + COL_PUBDATE + " TEXT, "
                + COL_DESCRIPTION + " TEXT, " + COL_URL + " TEXT)";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    ArrayList<BbcArticles> listArticles() {
        String sql = "select * from " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<BbcArticles> storeArticles = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                int id = Integer.parseInt(cursor.getString(0));
                String title = cursor.getString(1);
                String pubDate = cursor.getString(2);
                String description = cursor.getString(3);
                String webUrl = cursor.getString(4);
                storeArticles.add(new BbcArticles(id, title, pubDate, description, webUrl));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return storeArticles;
    }
    void addArticles(BbcArticles articles) {
        ContentValues values = new ContentValues();
        values.put(COL_TITLE, articles.getTitle());
        values.put(COL_PUBDATE, articles.getPubDate());
        values.put(COL_DESCRIPTION, articles.getDescription());
        values.put(COL_URL, articles.getWebUrl());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_NAME, null, values);

    }
    void updateArticles(BbcArticles articles) {
        ContentValues values = new ContentValues();
        values.put(COL_TITLE, articles.getTitle());
        values.put(COL_PUBDATE, articles.getPubDate());
        values.put(COL_DESCRIPTION, articles.getDescription());
        values.put(COL_URL, articles.getWebUrl());
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(TABLE_NAME, values, COL_ID + " = ?", new String[]{String.valueOf(articles.getId())});
    }
    void deleteArticle(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COL_ID + " = ?", new String[]{String.valueOf(id)});
    }

}
