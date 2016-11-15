package com.comp3004.beacon.LocationManagement;

import android.location.Location;

/**
 * Created by julianclayton on 2016-11-14.
 */
public class MyLocationManager {

    private static MyLocationManager myLocationManager;


    private Location myLocation;

    public static MyLocationManager getInstance() {
        if (myLocationManager == null) {
            myLocationManager = new MyLocationManager();
        }
        return myLocationManager;
    }

    public Location getMyLocation() {
        return myLocation;
    }

    public void setMyLocation(Location myLocation) {
        this.myLocation = myLocation;
    }

}
