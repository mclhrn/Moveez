package com.project.moveez.app;
/**
 * Created by michaelhearne on 11/02/16.
 */
public class Movie {

    String desc;
    String releaseDate;
    double rating;
    String poster;
    String posterPath;
    String todaysDate;


    public Movie() {

    }

    public Movie(String desc, String releaseDate, double rating, String poster, String posterPath, String todaysDate, String title) {
        this.desc = desc;
        this.releaseDate = releaseDate;
        this.rating = rating;
        this.poster = poster;
        this.posterPath = posterPath;
        this.todaysDate = todaysDate;
        this.title = title;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    String title;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getTodaysDate() {
        return todaysDate;
    }

    public void setTodaysDate(String todaysDate) {
        this.todaysDate = todaysDate;
    }
}
