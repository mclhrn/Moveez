package com.project.moveez.app;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.moveez.app.data.MovieContract;
import com.project.moveez.app.data.MovieProvider;

/**
 * Created by dandold on 21/01/2016.
 */
public class MovieDetailActivityFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String[] MOVIE_COLUMNS = {
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
    int _id;

    public MovieDetailActivityFragment() {
    }

    public static MovieDetailActivityFragment newInstance(int id) {
        MovieDetailActivityFragment f = new MovieDetailActivityFragment();
        Bundle args = new Bundle();
        args.putInt("_id", id);
        f.setArguments(args);
        return f;
    }


    /**
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return onCreateView() is the standard method called for a fragment class
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        _id = getArguments().getInt("_id");
        Movie movie = MovieProvider.getMovie(_id);

        Log.i(TAG, "onCreateView: ");

        tv = (TextView) rootView.findViewById(R.id.movie_details);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Intent intent = getActivity().getIntent();
        if (intent == null) {
            return null;
        }

        return new CursorLoader(
                getActivity(),
                intent.getData(),
                MOVIE_COLUMNS,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if (data != null && data.moveToFirst()) {
            String movieName = data.getString(COL_MOVIE_TITLE);
            //tv = (TextView) getActivity().findViewById(R.id.movie_details);
            tv.setText(movieName);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

}
