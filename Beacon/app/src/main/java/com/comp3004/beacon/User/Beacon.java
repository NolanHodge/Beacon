package com.comp3004.beacon.User;

import com.comp3004.beacon.Networking.CurrentBeaconInvitationHandler;

/**
 * Created by julianclayton on 16-10-10.
 */
public class Beacon {

    String senderId;
    String lat;
    String lon;

    public Beacon() {}

    public Beacon(CurrentBeaconInvitationHandler currentBeaconInvitationHandler) {
        senderId = currentBeaconInvitationHandler.getSenderId();
        lat = currentBeaconInvitationHandler.getLat();
        lon = currentBeaconInvitationHandler.getLon();

    }
    public Beacon(String followingUid, String lat, String lon) {
        this.senderId = followingUid;
        this.lat = lat;
        this.lon = lon;

    }

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
    public String getSenderId() {
        return senderId;
    }

}
