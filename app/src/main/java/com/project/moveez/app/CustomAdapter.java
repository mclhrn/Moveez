package com.project.moveez.app;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import com.project.moveez.app.data.MovieContract;

/**
 * ArrayAdapter<ArrayList> is an ArrayAdapter class of type ArrayList
 * A concrete BaseAdapter that is backed by an array of arbitrary objects.
 *
 * Reference - http://developer.android.com/reference/android/widget/ArrayAdapter.html
 */
public class CustomAdapter extends CursorAdapter { //ArrayAdapter<ArrayList> {

    public final String LOG_TAG = CustomAdapter.class.getSimpleName();

    public static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w500/";
    public static final String IMAGE_PATH = "poster_path";
    public static final String MOVIE_TITLE = "original_title";
    public static final String MOVIE_DESCRIPTION = "overview";
    public static final String MOVIE_RATING = "vote_average";
    public static final String MOVIE_RELEASE_DATE = "release_date";


    public CustomAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
        //Log.i(LOG_TAG, "In constructor");
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        //Log.i(LOG_TAG, "In the newView() method");
        View view = LayoutInflater.from(context).inflate(R.layout.list_image_movie, parent, false);

        return view;
    }


    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        String title =cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE));
        Log.i(LOG_TAG, "Movie Title - " + title);

        ImageView iv = (ImageView) view.findViewById(R.id.list_movie_pics_imageview);

        int columIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER);
        Bitmap image = Utility.getImage(cursor.getBlob(columIndex));

        iv.setImageBitmap(image);
    }
}
