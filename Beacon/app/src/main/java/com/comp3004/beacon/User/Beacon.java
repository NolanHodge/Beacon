package com.comp3004.beacon.User;

/**
 * Created by julianclayton on 16-10-23.
 */
public class Beacon {
    protected String beaconId;
    protected String lat;
    protected String lon;
    protected String fromUserId;
    protected boolean publicBeacon;


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
        return publicBeacon;
    }
    public void setPublicBeacon(boolean publicBeacon) {
        this.publicBeacon = publicBeacon;
    }

}
