package com.comp3004.beacon.User;

/**
 * Created by julianclayton on 16-10-23.
 */
public class PublicBeacon extends Beacon {

    String displayName;

    public PublicBeacon() {}

    public boolean isPublicBeacon() {
        return true;
    }
    public PublicBeacon(String fromUserId, String displayName, String lat, String lon) {
        this.fromUserId = fromUserId;
        this.displayName = displayName;
        this.lat = lat;
        this.lon = lon;
        publicBeacon = true;
    }
    public String getDisplayName() {
        return displayName;
    }
}
