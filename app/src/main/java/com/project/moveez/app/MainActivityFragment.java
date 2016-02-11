package com.project.moveez.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.*;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import com.project.moveez.app.data.MovieContract;
import org.json.JSONArray;

/**
 * A placeholder fragment containing a simple view.
 * <p/>
 * Guide - http://developer.android.com/guide/components/fragments.html
 * Reference - http://developer.android.com/reference/android/app/Fragment.html
 */
public class MainActivityFragment extends Fragment
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

    /**
     * Create a list of global variables
     * - TAG for log messages
     * - customAdapter for link to the CustomAdapter class
     * - jsonArray for the JSONArray object to be used in the onPostExecute of the AsyncTask
     * - gridView so the GridView can be linked ot the xml in the onCreateView method and also sent the setAdapter method
     */
    private final String TAG = MainActivityFragment.class.getSimpleName();
    private CustomAdapter customAdapter;
    private JSONArray jsonArray;
    GridView gridView;

    public MainActivityFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * setHasOptionsMenu(true) enables fragments to
         * have options menu enabled and visible.
         * This has to be set in the onCreate method
         */
        setHasOptionsMenu(true);

        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_mainfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                /**
                 * Once the refresh button is pressed, the update() method is started.
                 * Always return true if you want an option to do something
                 */
                update();
                return true;
            default:
                return false;
        }
    }

    /**
     * Reference to the AsyncTask inner class is created.
     * The SharedPreferences are got. This is what the user has set their values to in the options menu.
     * It is used as a key, value pair.
     * The result is saved to a String and sent as an argument to the AsyncTask when executed
     */
    public void update() {
        FetchMovieTask movieTask = new FetchMovieTask(getContext());

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String userChoice = sharedPreferences.getString(getString(R.string.pref_sort_key), getString(R.string.pref_sort_value_mostpop));

        movieTask.execute(userChoice);
    }

    /**
     * onStart() method is called when the activity is started.
     * It calls the update() method so the most up to date date is collected and displayed
     */
    @Override
    public void onStart() {
        super.onStart();
        update();
    }

    /**
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return onCreateView() method is created automatically when the Fragment class is created
     * View object is created to link it to the xml UI and returned at the end of the method
     * Here any references to the UI elements are created and referenced to their java objects
     * <p/>
     * A setOnItemClickListener() is linked to the gridView object.
     * In this, when the gridView object is pressed, a new intent is started.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        customAdapter = new CustomAdapter(getActivity(), null, 0);

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        gridView = (GridView) rootView.findViewById(R.id.gridView_movies);
        gridView.setAdapter(customAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Log.i(TAG, "onItemClick: " + customAdapter.getFilmDetails(position));
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                Toast toast = Toast.makeText(getActivity(), cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE)), Toast.LENGTH_SHORT);
                toast.show();

                String title = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE));
                String desc = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_DESCRIPTION));

                Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
                intent.putExtra(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE, title);
                intent.putExtra(MovieContract.MovieEntry.COLUMN_DESCRIPTION, desc);

                /**
                 * The intent starts the MovieDetailActivity class
                 * Extra information is passed in the intent as EXTRA_TEXT
                 * The EXTRA_TEXT is the film details got from the API
                 *
                 * Reference - http://developer.android.com/reference/android/content/Intent.html
                 * Guide - http://developer.android.com/guide/components/intents-filters.html
                 */
//                Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
//                        //.putExtra(Intent.EXTRA_TEXT, customAdapter.getFilmDetails(position));
                startActivity(intent);


//                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
//                if(cursor != null){
//                    Intent intent = new Intent(getActivity(), MovieDetailActivity.class)
//                        .setData(MovieContract.MovieEntry.CONTENT_URI);
//                    startActivity(intent);
//                }
            }
        });

        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                getActivity(),
                MovieContract.MovieEntry.CONTENT_URI,
                MOVIE_COLUMNS,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        customAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        customAdapter.swapCursor(null);
    }
}
