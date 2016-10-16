package com.comp3004.beacon.Networking;

/**
 * Created by julianclayton on 16-10-16.
 */
public class ChatMessage {

    String fromUserId;
    String toUserId;
    String message;
    long timeStamp;

    public ChatMessage() {}

    public ChatMessage(String toUserId, String fromUserId, String message) {
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.message = message;
        this.timeStamp = System.currentTimeMillis();
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

}
