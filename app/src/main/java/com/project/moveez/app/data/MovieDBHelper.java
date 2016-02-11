package com.project.moveez.app.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.project.moveez.app.data.MovieContract.MovieEntry;

/**
 * SQLiteOpenHelper is a helper class to manage database creation and version management.
 * Reference - http://developer.android.com/reference/android/database/sqlite/SQLiteOpenHelper.html
 */
public class MovieDBHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "movies.db";

    public MovieDBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        /**
         * Create a table to hold all the movies
         */
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +
                MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MovieEntry.COLUMN_DATE + " INTEGER NOT NULL, " +
                MovieEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_MOVIE_RATING + " REAL NOT NULL, " +
                MovieEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_POSTER + " BLOB, " +
                MovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                "UNIQUE (" + MovieEntry.COLUMN_DATE + ", "
                            + MovieEntry.COLUMN_MOVIE_TITLE + ", "
                            + MovieEntry.COLUMN_DESCRIPTION + ", "
                            + MovieEntry.COLUMN_MOVIE_RATING + ", "
                            + MovieEntry.COLUMN_POSTER + ", "
                            + MovieEntry.COLUMN_RELEASE_DATE +
                ") ON CONFLICT REPLACE" +
                ");";

        db.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /**
         * This database is only a cache for online data, so its upgrade policy is
         * to simply to discard the data and start over
         * Note that this only fires if you change the version number for your database.
         * It does NOT depend on the version number for your application.
         * If you want to update the schema without wiping data, commenting out the next 2 lines
         * should be your top priority before modifying this method.
         */
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
