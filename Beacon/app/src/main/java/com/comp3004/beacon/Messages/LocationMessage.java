package com.comp3004.beacon.Messages;

import android.net.Uri;

/**
 * Created by julianclayton on 16-09-24.
 */
public class LocationMessage {

    static final String MESSAGE_HEADER = "location";
    private String lat;
    private String lon;
    private String name;
    private String photoUrl;
    private String userId;

    public LocationMessage(String userId,String name, String photoUrl, String lat, String lon) {
        this.userId = userId;
        this.lat = lat;
        this.lon = lon;
        this.name = name;
        this.photoUrl = photoUrl;
    }



    public String getLat() {
        return lat;
    }

    public String getLon() { return lon; }

    public String getUid() { return  userId;}

    public void setUid() { this.userId = userId;}

    public void setLat(String text) {
        this.lat = text;
    }

    public void setLon(String lon) { this.lon = lon; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
