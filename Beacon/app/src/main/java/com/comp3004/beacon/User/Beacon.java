package com.comp3004.beacon.User;

import com.comp3004.beacon.Networking.CurrentBeaconInvitationHandler;

/**
 * Created by julianclayton on 16-10-10.
 */
public class Beacon {

    private String beaconId;
    private String fromUserId;
    private String toUserId;
    private String lat;
    private String lon;


    public Beacon() {}

    public Beacon(CurrentBeaconInvitationHandler currentBeaconInvitationHandler) {
        fromUserId = currentBeaconInvitationHandler.getFromUserId();
        toUserId = currentBeaconInvitationHandler.getToUserId();
        lat = currentBeaconInvitationHandler.getLat();
        lon = currentBeaconInvitationHandler.getLon();
        beaconId = currentBeaconInvitationHandler.getBeaconId();

    }
    public Beacon(String followingUid, String lat, String lon) {
        this.fromUserId = followingUid;
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
    public String getFromUserId() {
        return fromUserId;
    }

    public String getToUserId() {
        return toUserId;
    }
    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }
    public String getBeaconId() {
        return beaconId;
    }
    public void setBeaconId(String beaconId) {
        this.beaconId = beaconId;
    }
}
