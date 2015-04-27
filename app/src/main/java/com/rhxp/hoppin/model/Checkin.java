package com.rhxp.hoppin.model;

/**
 * Created by rickyh on 4/23/15.
 */
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class Checkin {
    private long id;
    private String service = "twitter";
    private String created_at;
    private User user;
    private Geo geo;
    private String text;
    private int retweet_count;
    private int favorite_count;
    public final double FIFTH_OF_A_MILE = 0.00028;
    public Checkin() {}

    public Checkin(long id, String service, Geo geo, User user, String text, int retweet_count, int favorite_count,  String created_at) {
        this.id = id;
        this.service = service;
        this.created_at = created_at;
        this.geo = geo;
        this.user = user;
        this.text = text;
        this.retweet_count = retweet_count;
        this.favorite_count = favorite_count;
    }

    public long getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getService() {
        return this.service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public double getLat() {
        return geo.getLat();
    }

    public double getLon() {
        return geo.getLon();
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCreated_at(){
        return this.created_at;
    }

    public String getFormattedCreated_at() {
        SimpleDateFormat readTwitterFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy");
        SimpleDateFormat writeFormat = new SimpleDateFormat("EEE MMM dd, yyyy - hh:mm a");
        readTwitterFormat.setTimeZone(TimeZone.getTimeZone("EST"));
        writeFormat.setTimeZone(TimeZone.getTimeZone("EST"));
        Date date = null;
        try {
            date = readTwitterFormat.parse(this.created_at);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return (date != null)?writeFormat.format(date):"";
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public Geo getGeo() {
        return this.geo;
    }
    public void setGeo(Geo geo) {
        this.geo = geo;
    }

    public int getRetweet_count() {
        return this.retweet_count;
    }

    public void setRetweet_count(int retweet_count) {
        this.retweet_count = retweet_count;
    }

    public int getFavorite_count() {
        return this.favorite_count;
    }

    public void setFavorite_count(int favorite_count) {
        this.favorite_count = favorite_count;
    }

    public String getContentLink() {
        return "https://twitter.com/" + user.getScreen_name() + "/status/" + this.getId();
    }


    public String toString(){
        return "[id="+ this.id + ", service=" + this.service + ", lat="
                + this.getLat() + ", lon=" + this.getLon() + ", user=" + user.getScreen_name() + ", text=" + this.getText() +"]";
    }

    public String getColor(ArrayList<Checkin> list){
        int neighbors = 0;
        for(Checkin place: list){
            if(this.isNeighbor(place)){
                neighbors ++;
            }
        }
        return "";
    }

    public boolean isNeighbor(Checkin c){
        double x;
        double y;
        y = Math.abs (c.getLat()- this.getLat());
        x = Math.abs (c.getLon() - this.getLon());
        if(x < FIFTH_OF_A_MILE ){
            return true;
        }
        else if(y < FIFTH_OF_A_MILE){
            return true;
        }
        return false;
    }

}
