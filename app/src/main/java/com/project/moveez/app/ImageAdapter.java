package com.project.moveez.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ImageAdapter extends ArrayAdapter<ArrayList> {

    ArrayList list;
    Context context;

    public ImageAdapter(Context context, ArrayList list) {
        super(context, android.R.id.content);
        this.list = list;
        this.context = context;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w500/";

        try {
            JSONObject jsonObject = (JSONObject) list.get(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.list_image_movie, parent, false);
            }

            ImageView iv = (ImageView) convertView.findViewById(R.id.list_movie_pics_imageview);

            Picasso.with(context)
                    .load(IMAGE_BASE_URL + jsonObject.get("poster_path"))
                    .into(iv);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return convertView;
    }

    public String getTitle(int position){
        try{
            JSONObject jsonObject = (JSONObject) list.get(position);

            return jsonObject.getString("original_title");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getFilmDetails(int position){
        try{
            JSONObject jsonObject = (JSONObject) list.get(position);

            String title = jsonObject.getString("original_title");
            String overview = jsonObject.getString("overview");
            double rating = jsonObject.getDouble("vote_average");
            String release = jsonObject.getString("release_date");

            String details = "The movie title is " + title +
                    "\n" +
                    "\nOverview : " + overview +
                    "\n" +
                    "\nThe rating of the film is " + Double.toString(rating) +
                    "\n" +
                    "\nThe release date of the film is " + release;

            return details;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}