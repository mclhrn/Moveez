package com.project.moveez.app.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.format.Time;

/**
 * Created by dandold on 05/02/2016.
 *
 * Here is where the table and column names for the Movie Database is defined
 */
public class MovieContract {
    /**
     * The "Content authority" is a name for the entire content provider, similar to the
     * relationship between a domain name and its website.  A convenient string to use for the
     * content authority is the package name for the app, which is guaranteed to be unique on the
     * device.
     */
    public static final String CONTENT_AUTHORITY = "com.project.moveez.app";


    /**
     * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);


    /**
     * Possible paths (appended to base content URI for possible URI's)
     * For instance, content://com.project.moveez.app/movie/ is a valid path for
     * looking at weather data. content://com.project.moveez.app/givemeroot/ will fail,
     * as the ContentProvider hasn't been given any information on what to do with "givemeroot".
     */
    public static final String PATH_MOVIE = "movie";


    /**
     * To make it easy to query for the exact date, we normalize all dates that go into
     * the database to the start of the the Julian day at UTC.
     */
    public static long normalizeDate(long startDate) {
        // normalize the start date to the beginning of the (UTC) day
        Time time = new Time();
        time.set(startDate);
        int julianDay = Time.getJulianDay(startDate, time.gmtoff);
        return time.setJulianDay(julianDay);
    }


    /**
     * Inner class that defines the table contents of the movie table
     *
     * BaseColumns Reference - http://developer.android.com/reference/android/provider/BaseColumns.html
     */
    public static final class MovieEntry implements BaseColumns {
        /**
         * This is the direct URI for the movie content provider
         */
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();


        /**
         * Define the paths to directories and items
         */
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;


        /**
         * Define the table name
         */
        public static final String TABLE_NAME = "movies";


        /**
         * Date, stored as long in millimeters since the epoch
         */
        public static final String COLUMN_DATE = "date";


        /**
         * Define the poster path
         */
        public static final String COLUMN_POSTER = "poster_path";


        /**
         * Define the movie description
         */
        public static final String COLUMN_DESCRIPTION = "overview";


        /**
         * Define the movie release date
         */
        public static final String COLUMN_RELEASE_DATE = "release_date";


        /**
         * Define the movie title
         */
        public static final String COLUMN_MOVIE_TITLE = "original_title";


        /**
         * Define the movie rating
         */
        public static final String COLUMN_MOVIE_RATING = "vote_average";

        public static Uri buildMovieUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
