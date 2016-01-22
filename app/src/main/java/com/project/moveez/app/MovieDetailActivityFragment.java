package com.project.moveez.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by dandold on 21/01/2016.
 */
public class MovieDetailActivityFragment extends Fragment {

    public final String TAG = MovieDetailActivityFragment.class.getSimpleName();
    private String movieData;

    public MovieDetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        Intent intent = getActivity().getIntent();
        if(intent != null && intent.hasExtra(Intent.EXTRA_TEXT)){
            movieData = intent.getStringExtra(Intent.EXTRA_TEXT);
            Log.i(TAG, intent.getStringExtra(Intent.EXTRA_TEXT));
            TextView tv = (TextView) rootView.findViewById(R.id.movie_details);
            tv.setText(movieData);
        }

        return rootView;
    }

}
