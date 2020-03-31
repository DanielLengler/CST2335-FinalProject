package guardian;

/**
 * @author Naimul Rahman
 * @class MyOpener
 * @version 3
 * This class is used for database purposes. The database allows storing and removing articles
 * to a favorites list.
 */

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

    /**
     * Creates the database and has a version number associated with it so it can either
     * call onUpgrade or onDowngrade.
     * @param ctx
     */
    public MyOpener(Context ctx){super(ctx, DATABASE_NAME, null, VERSION_NUM);}

    /**
     * This method creates a table called Favorites to allow storing articles to a favorites list or
     * removing them.
     * @param db The name of the database, FavoritesDB
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_TITLE + " text,"
                + COL_URL + " text," + COL_SECTION_NAME + " text);");
    }

    /**
     * This method drops the table if the database version is newer than the one stored on the
     * device, and calls onCreate() to make a fresh table.
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    /**
     * This method drops the table if the database version is older than the one stored on the
     * device, and calls onCreate() to make a fresh table.
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
