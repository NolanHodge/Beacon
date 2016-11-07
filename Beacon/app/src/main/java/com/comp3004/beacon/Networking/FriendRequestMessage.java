package com.comp3004.beacon.Networking;

import com.comp3004.beacon.User.BeaconUser;

/**
 * Created by julianclayton on 2016-11-06.
 */
public class FriendRequestMessage {

    String toUserId;
    String fromUserId;
    String displayName;
    BeaconUser beaconUser;

    public FriendRequestMessage(){};

    public FriendRequestMessage(String toUserId, String fromUserId, BeaconUser fromBeaconUser) {
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        beaconUser = fromBeaconUser;

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

    public BeaconUser getBeaconUser() {
        return beaconUser;
    }

    public void setBeaconUser(BeaconUser beaconUser) {
        this.beaconUser = beaconUser;
    }

}
