package com.comp3004.beacon.User;

/**
 * Created by julianclayton on 2016-11-05.
 */
public class CurrentUserPublicBeaconHandler {

    private static CurrentUserPublicBeaconHandler currentUserPublicBeaconHandler;
    private  boolean hasPublicBeacon;
    private PublicBeacon publicBeacon;

    private String key;

    public CurrentUserPublicBeaconHandler() {
        publicBeacon = null;
    }

    public static CurrentUserPublicBeaconHandler getInstance() {
        if (currentUserPublicBeaconHandler == null) {
            currentUserPublicBeaconHandler = new CurrentUserPublicBeaconHandler();
        }
        return currentUserPublicBeaconHandler;
    }



    public boolean isHasPublicBeacon() {
        return hasPublicBeacon;
    }

    public void setHasPublicBeacon(boolean hasPublicBeacon) {
        this.hasPublicBeacon = hasPublicBeacon;
    }

    public  PublicBeacon getPublicBeacon() {
        return publicBeacon;
    }

    public void setPublicBeacon(PublicBeacon publicBeacon) {
        this.publicBeacon = publicBeacon;
    }
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }




}
