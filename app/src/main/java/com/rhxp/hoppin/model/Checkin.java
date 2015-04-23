package com.rhxp.hoppin.model;

/**
 * Created by rickyh on 4/23/15.
 */
public class Checkin {
    private int id;
    private String service;
    private int serviceId;
    private double lat;
    private double lon;
    private int user;
    private String content;
    private String contentLink;

    public Checkin() {}

    public Checkin(int id, String service, int serviceId, double lat, double lon, int user, String content, String contentLink) {
        this.id = id;
        this.service = service;
        this.serviceId = serviceId;
        this.lat = lat;
        this.lon = lon;
        this.user = user;
        this.content = content;
        this.contentLink = contentLink;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentLink() {
        return contentLink;
    }

    public void setContentLink(String contentLink) {
        this.contentLink = contentLink;
    }
}
