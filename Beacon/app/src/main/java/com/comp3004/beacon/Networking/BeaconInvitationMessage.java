package com.comp3004.beacon.Networking;

/**
 * Created by julianclayton on 16-10-02.
 *
 * Message is for sending an invitation t
 */
public class BeaconInvitationMessage {

    String beaconId;
    String message;
    String toUserId;
    String fromUserId;
    long timestamp;
    String lat;
    String lon;


    public BeaconInvitationMessage() {}
    public BeaconInvitationMessage(String toUserId, String fromUserId, String message, String lat, String lon) {
        this.message = message;
        this.toUserId = toUserId;
        this.timestamp = System.currentTimeMillis();
        this.lon = lon;
        this.lat = lat;
        this.fromUserId = fromUserId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getBeaconId() {
        return beaconId;
    }

    public void setBeaconId(String beaconId) {
        this.beaconId = beaconId;
    }

}
