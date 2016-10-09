package com.comp3004.beacon.Networking;

/**
 * Created by julianclayton on 16-10-02.
 *
 * Message is for sending an invitation t
 */
public class BeaconInvitationMessage {

    String message;
    String senderId;

    public BeaconInvitationMessage(String senderId, String message) {
        this.message = message;
        this.senderId = senderId;
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
}
