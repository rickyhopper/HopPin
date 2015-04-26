package com.rhxp.hoppin.model;

/**
 * Created by xavierprimus on 4/26/15.
 */
public class User {
    private String screen_name;
    private String name;
    private int followers_count;
    private String profile_image_url;

    public User(String name, String screen_name, String profile_image_url, int followers_count){
        this.screen_name = screen_name;
        this.name = name;
        this.followers_count = followers_count;
        this.profile_image_url = profile_image_url;
    }

    public String getScreen_name() {
        return screen_name;
    }

    public void setScreen_name(String screen_name) {
        this.screen_name = screen_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFollowers_count() {
        return followers_count;
    }

    public void setFollowers_count(int followers_count) {
        this.followers_count = followers_count;
    }

    public String getProfile_image_url() {
        return profile_image_url;
    }

    public void setProfile_image_url(String profile_image_url) {
        this.profile_image_url = profile_image_url;
    }

    @Override
    public String toString() {
        return "User [screen_name=" + this.screen_name + ", name="
                + this.name + ", followers_count=" + followers_count + ",profile_image_url=" + this.profile_image_url+ "]";
    }
}
