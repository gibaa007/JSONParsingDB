package com.gibaa007.httpurljson;

/**
 * Created by gibaa007 on 11/3/16.
 */

public class Movie {
    private int id;
    private boolean fav = false;
    private String title, thumbnailUrl;
    private int year;
    private double rating;

    public Movie() {
    }

    public Movie(int id, String name, String thumbnailUrl, int year, double rating, Boolean fav) {
        this.id = id;
        this.title = name;
        this.thumbnailUrl = thumbnailUrl;
        this.year = year;
        this.rating = rating;
        this.fav = fav;
    }

    public Movie(String name, String thumbnailUrl, int year, double rating, Boolean fav) {
        this.title = name;
        this.thumbnailUrl = thumbnailUrl;
        this.year = year;
        this.rating = rating;
        this.fav = fav;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isFav() {
        return fav;
    }

    public void setFav(boolean fav) {
        this.fav = fav;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

}