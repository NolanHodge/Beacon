package com.comp3004.beacon.Networking;

import com.comp3004.beacon.DatabaseListeners.LocationRequestListener;

/**
 * Created by julianclayton on 2016-11-05.
 */
public class LocationRequestMessage {

    String toUserId;
    String fromUserId;

    public LocationRequestMessage() {}

    public LocationRequestMessage(String toUserId, String fromUserId) {
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
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
}
