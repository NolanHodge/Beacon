package com.comp3004.beacon.Networking;

import com.comp3004.beacon.User.PublicBeacon;

import java.util.HashMap;

/**
 * Created by julianclayton on 16-10-23.
 */
public class PublicBeaconHandler {

    private HashMap<String, PublicBeacon> beacons;
    public static PublicBeaconHandler publicBeaconHandler;

    public PublicBeaconHandler() {
        publicBeaconHandler = this;
        beacons = new HashMap<String, PublicBeacon>();
    }

    public static PublicBeaconHandler getInstance() {
        if (publicBeaconHandler == null) {
            publicBeaconHandler = new PublicBeaconHandler();
        }
        return publicBeaconHandler;
    }

    public void addBeacon(String beaconId, PublicBeacon beacon) {
        beacons.put(beaconId, beacon);
    }

    public PublicBeacon getBeacon(String beaconId) {
        return beacons.get(beaconId);
    }

    public HashMap getBeacons() {
        return beacons;
    }

    public void removeBeacon(String beaconId) {
        beacons.remove(beaconId);
    }
}
