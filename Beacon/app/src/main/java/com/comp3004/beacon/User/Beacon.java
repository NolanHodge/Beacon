package com.comp3004.beacon.User;

import java.util.Arrays;
import java.util.List;

/**
 * Created by julianclayton on 16-10-23.
 */
public class Beacon {
    protected String beaconId;
    protected String lat;
    protected String lon;
    protected String fromUserId;
    protected boolean publicBeacon;
    protected String toUserId;


    public String getLat() {
        return lat;
    }
    public void setLat(String lat) {
        this.lat = lat;
    }
    public String getLon() {
        return lon;
    }
    public void setLon(String lon) {
        this.lon = lon;
    }
    public String getFromUserId() {
        return fromUserId;
    }
    public String getBeaconId() {
        return beaconId;
    }
    public void setBeaconId(String beaconId) {
        this.beaconId = beaconId;
    }

    public boolean isPublicBeacon() {
        List<String> ids = Arrays.asList(beaconId.split("_"));
        if (ids.size() == 3 && ids.get(2).equals("private")) {
            return false;
        }
        else return true;

    }
    public void setPublicBeacon(boolean publicBeacon) {
        this.publicBeacon = publicBeacon;
    }


    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }
}
