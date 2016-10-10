package com.comp3004.beacon.Networking;

/**
 * Created by julianclayton on 16-10-02.
 *
 * Message is for sending an invitation t
 */
public class BeaconInvitationMessage {

    String message;
    String senderId;
    long timestamp;
    String lat;
    String lon;


    public BeaconInvitationMessage() {}
    public BeaconInvitationMessage(String senderId, String message) {
        this.message = message;
        this.senderId = senderId;
        this.timestamp = System.currentTimeMillis();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
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
}
