package com.project.moveez.app.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.project.moveez.app.Movie;
import com.project.moveez.app.MovieDetailActivityFragment;

/**
 * Content providers are one of the primary building blocks of Android applications, providing content to applications.
 * They encapsulate data and provide it to applications through the single ContentResolver interface.
 *
 * Reference - http://developer.android.com/reference/android/content/ContentProvider.html
 * Guide - http://developer.android.com/guide/topics/providers/content-providers.html
 */
public class MovieProvider extends ContentProvider {

    private static final String LOG_TAG = MovieProvider.class.getSimpleName();

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    public static MovieDBHelper mOpenHelper;

    private static final int MOVIE = 100;

    @Override
    public boolean onCreate() {
        mOpenHelper = new MovieDBHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor = mOpenHelper.getReadableDatabase().query(
                MovieContract.MovieEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return MovieContract.MovieEntry.CONTENT_TYPE;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        Uri returnUri;

        normalizeDate(values);
        long _id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, values);
        if(_id > 0){
            returnUri = MovieContract.MovieEntry.buildMovieUri(_id);
        } else {
            try {
                //return returnUri = MovieContract.MovieEntry.CONTENT_URI;
                throw new android.database.SQLException("Failed to insert row into " + uri);
            } catch(android.database.SQLException e) {
                Log.i(LOG_TAG, "Did not insert row to DB it was already there");
            } finally {
                return returnUri = MovieContract.MovieEntry.CONTENT_URI;
            }
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int rowDeleted;

        if(selection == null){
            selection = "1";
        }

        rowDeleted = db.delete(MovieContract.MovieEntry.TABLE_NAME, selection, selectionArgs);

        if(rowDeleted != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int rowUpdated;

        normalizeDate(values);
        rowUpdated = db.update(MovieContract.MovieEntry.TABLE_NAME, values, selection, selectionArgs);

        if(rowUpdated != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowUpdated;
    }

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, MovieContract.PATH_MOVIE, MOVIE);
        return matcher;
    }

    private void normalizeDate(ContentValues values){
        if(values.containsKey(MovieContract.MovieEntry.COLUMN_DATE)){
            long dateValue = values.getAsLong(MovieContract.MovieEntry.COLUMN_DATE);
            values.put(MovieContract.MovieEntry.COLUMN_DATE, MovieContract.normalizeDate(dateValue));
        }
    }






    public static Movie getMovie(int name) {
        Movie movie = new Movie();
        String where = MovieContract.MovieEntry._ID + " = '" + name + "'";
        Cursor cursor = mOpenHelper.getReadableDatabase().query(MovieContract.MovieEntry.TABLE_NAME, MovieDetailActivityFragment.MOVIE_COLUMNS, where, null, null, null, null, null);

        if (cursor.getColumnCount() > 0) {
            cursor.moveToNext();
            movie.setTitle(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE)));
            movie.setDesc(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_DESCRIPTION)));
            movie.setReleaseDate(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE_DATE)));
            movie.setRating(cursor.getDouble(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_RATING)));
            movie.setPoster(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER)));
            movie.setTodaysDate(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_DATE)));
        }
        cursor.close();
        return movie;
    }

    public static boolean rowExists(String name) {
        String where = MovieContract.MovieEntry.COLUMN_MOVIE_TITLE + " = '" + name + "'";
        Cursor cursor = mOpenHelper.getReadableDatabase().query(MovieContract.MovieEntry.TABLE_NAME, MovieDetailActivityFragment.MOVIE_COLUMNS, where, null, null, null, null, null);
        return cursor.getCount() != 0;
    }

}
