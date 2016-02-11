package com.project.moveez.app;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.project.moveez.app.data.MovieContract;

/**
 * Created by dandold on 21/01/2016.
 */
public class MovieDetailActivityFragment extends Fragment
    implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String[] MOVIE_COLUMNS = {
            MovieContract.MovieEntry.TABLE_NAME + "." + MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_DATE,
            MovieContract.MovieEntry.COLUMN_MOVIE_TITLE,
            MovieContract.MovieEntry.COLUMN_DESCRIPTION,
            MovieContract.MovieEntry.COLUMN_POSTER,
            MovieContract.MovieEntry.COLUMN_MOVIE_RATING,
            MovieContract.MovieEntry.COLUMN_RELEASE_DATE
    };

    static final int COL_MOVIE_ID = 0;
    static final int COL_MOVIE_DATE = 1;
    static final int COL_MOVIE_TITLE = 2;
    static final int COL_MOVIE_DESC = 3;
    static final int COL_MOVIE_POSTER = 4;
    static final int COL_MOVIE_RATING = 5;
    static final int COL_MOVIE_RELEASE = 6;

    private static final int DETAIL_LOADER = 0;

    public final String TAG = MovieDetailActivityFragment.class.getSimpleName();
    private String movieData;

    private TextView tv;

    public MovieDetailActivityFragment() {
    }

    /**
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     *
     * onCreateView() is the standard method called for a fragment class
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        /**
         * The intent gets the intent and extra information
         * Extra information is passed in the intent as EXTRA_TEXT
         * The EXTRA_TEXT is the film details got from the API
         * This is got with the intent.getStringExtra method
         *
         * Reference - http://developer.android.com/reference/android/content/Intent.html
         * Guide - http://developer.android.com/guide/components/intents-filters.html
         */

        Bundle extras = getActivity().getIntent().getExtras();
        String title = extras.getString(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE);
        String desc = extras.getString(MovieContract.MovieEntry.COLUMN_DESCRIPTION);


//        Intent intent = getActivity().getIntent();
//        if(intent != null && intent.hasExtra(Intent.EXTRA_TEXT)){
//            movieData = intent.getStringExtra(Intent.EXTRA_TEXT);
//            Log.i(TAG, intent.getStringExtra(Intent.EXTRA_TEXT));
//            TextView tv = (TextView) rootView.findViewById(R.id.movie_details);
//            tv.setText(movieData);
//        }

        tv = (TextView) rootView.findViewById(R.id.movie_details);
tv.setText(desc);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
//        Intent intent = getActivity().getIntent();
//        if(intent == null){
//            return null;
//        }
//
//        return new CursorLoader(
//                getActivity(),
//                intent.getData(),
//                MOVIE_COLUMNS,
//                null,
//                null,
//                null
//        );
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

//        if(data != null && data.moveToFirst()){
//            String movieName = data.getString(COL_MOVIE_TITLE);
//            //tv = (TextView) getActivity().findViewById(R.id.movie_details);
//            tv.setText(movieName);
//        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

}
