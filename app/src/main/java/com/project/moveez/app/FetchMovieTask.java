package com.project.moveez.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.format.Time;
import android.util.Log;
import com.project.moveez.app.data.MovieContract;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

/**
 * FetchMovieTask is an AsyncTask that fetches the data from the API
 * The significance of the Async Task is that it runs on its own thread in the background
 * Reference - http://developer.android.com/reference/android/os/AsyncTask.html
 */
public class FetchMovieTask extends AsyncTask<String, Void, Void> {

    private final String LOG_TAG = FetchMovieTask.class.getSimpleName();

    private final Context mContext;

    public FetchMovieTask(Context context){
        this.mContext = context;
    }

    /**
     * @param params
     * @return doInBackground() method is used to perform the background computation that can take a long time
     * In this case it pulls the data from the MovieDB API
     */
    @Override
    protected Void doInBackground(String... params) {
        /**
         * An URLConnection for HTTP is used to send & receive data over the web
         *
         * Reference - http://developer.android.com/reference/java/net/HttpURLConnection.html
         */
        HttpURLConnection urlConnection = null;

        /**
         * Wraps an existing Reader and buffers the input.
         * Expensive interaction with the underlying reader is minimized, since most (smaller)
         * requests can be satisfied by accessing the buffer alone.
         * The drawback is that some extra space is required to hold the buffer and that copying
         * takes place when filling that buffer, but this is usually outweighed by the performance benefits.
         *
         * Reference - http://developer.android.com/reference/java/io/BufferedReader.html
         */
        BufferedReader reader = null;

        String movieJsonStr = null;

        try {
            /**
             * Build URI
             * Reference to URI - http://developer.android.com/reference/android/net/Uri.html
             * Reference to URI.Builder - http://developer.android.com/reference/android/net/Uri.Builder.html
             */

            final String MOVIE_BASE_URL = "https://api.themoviedb.org/3/discover/movie?";
            final String SORT_PARAM = "sort_by";
            final String APIKEY_PARAM = "api_key";

            Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                    .appendQueryParameter(SORT_PARAM, params[0])
                    .appendQueryParameter(APIKEY_PARAM, BuildConfig.OPEN_MOVIE_MAP_API_KEY)
                    .build();

            // URL Reference - http://developer.android.com/reference/java/net/URL.html
            URL url = new URL(builtUri.toString());

            Log.v(LOG_TAG, "Built URI :" + builtUri.toString());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            /**
             * InputStream is a readable source of Bytes
             * Reference - http://developer.android.com/reference/java/io/InputStream.html
             */
            InputStream inputStream = urlConnection.getInputStream();

            /**
             * StringBuffer is a modifiable sequence of characters for use in creating strings,
             * where all accesses are synchronized
             * Reference - http://developer.android.com/reference/java/lang/StringBuffer.html
             */
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }

            /**
             * InputStreamReader is a class for turning a byte stream into a character stream
             *
             * Reference - http://developer.android.com/reference/java/io/InputStreamReader.html
             */
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return null;
            }

            movieJsonStr = buffer.toString();
            insertToDatabase(new JSONObject(movieJsonStr));
            checkDBValues(mContext);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
            //return null;
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            /**
             * if the HTTPUrlConnection is not already closed when it reaches the
             * finally block - close it
             */
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            /**
             * If the BufferedReader is not already closed when
             * it reaches the finally block - close it
             */
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        return null;
    }

    public void insertToDatabase(JSONObject movieData) {
        try {
            JSONArray movieArray = movieData.getJSONArray("results");

            Time dayTime = new Time();
            dayTime.setToNow();
            int julianStartDay = Time.getJulianDay(System.currentTimeMillis(), dayTime.gmtoff);

            Vector<ContentValues> cVVector = new Vector<ContentValues>(movieArray.length());
            if (movieArray != null) {
                for (int i = 0; i < movieArray.length(); i++) {
                    JSONObject movies = movieArray.getJSONObject(i);

                    long dateTime = dayTime.setJulianDay(julianStartDay + i);
                    Bitmap moviePoster = Utility.getBitmapFromURL(movies.getString(CustomAdapter.IMAGE_PATH));
                    String movieTitle = movies.getString(CustomAdapter.MOVIE_TITLE);
                    String movieDescription = movies.getString(CustomAdapter.MOVIE_DESCRIPTION);
                    double movieRating = movies.getDouble(CustomAdapter.MOVIE_RATING);
                    String movieReleaseDate = movies.getString(CustomAdapter.MOVIE_RELEASE_DATE);

                    ContentValues movieValues = new ContentValues();
                    movieValues.put(MovieContract.MovieEntry.COLUMN_POSTER, Utility.getBytes(moviePoster));
                    movieValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, movieReleaseDate);
                    movieValues.put(MovieContract.MovieEntry.COLUMN_DESCRIPTION, movieDescription);
                    movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_RATING, movieRating);
                    movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE, movieTitle);
                    movieValues.put(MovieContract.MovieEntry.COLUMN_DATE, dateTime);

                    cVVector.add(movieValues);

                    //Log.i(LOG_TAG, "Movie Title - " + movieTitle);
                }

                int inserted = 0;
                if (cVVector.size() > 0) {
                    ContentValues[] array = new ContentValues[cVVector.size()];
                    cVVector.toArray(array);
                    inserted = mContext.getContentResolver().bulkInsert(MovieContract.MovieEntry.CONTENT_URI, array);
                }

                Log.i(LOG_TAG, "Inserted " + inserted + " items to the database");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void checkDBValues(Context context){
        Cursor cursor = context.getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null
                );

        int count = 0;
        while(cursor.moveToNext()){
//            Log.i(LOG_TAG, "Movie Title - "
//                    + cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE))
//                    + " - got from DB");
            count++;
        }
        Log.i(LOG_TAG, "Got " + count + " values from the DB");
    }

}
