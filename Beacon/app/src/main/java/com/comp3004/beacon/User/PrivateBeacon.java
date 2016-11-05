package com.comp3004.beacon.User;

import com.comp3004.beacon.Networking.BeaconInvitationMessage;
import com.comp3004.beacon.Networking.CurrentBeaconInvitationHandler;

/**
 * Created by julianclayton on 16-10-10.
 */
public class PrivateBeacon extends Beacon {
    private String toUserId;
    public PrivateBeacon() {}

    public PrivateBeacon(CurrentBeaconInvitationHandler currentBeaconInvitationHandler) {
        fromUserId = currentBeaconInvitationHandler.getFromUserId();
        toUserId = currentBeaconInvitationHandler.getToUserId();
        lat = currentBeaconInvitationHandler.getLat();
        lon = currentBeaconInvitationHandler.getLon();
        beaconId = currentBeaconInvitationHandler.getBeaconId();
        publicBeacon = false;
    }

    public PrivateBeacon(PublicBeacon publicBeacon) {

        fromUserId = publicBeacon.fromUserId;
        toUserId = CurrentBeaconUser.getInstance().getUserId();
        lat = publicBeacon.getLat();
        lon = publicBeacon.getLon();
        beaconId = publicBeacon.getBeaconId();
        this.publicBeacon = false;
    }

    public PrivateBeacon(String followingUid, String lat, String lon) {
        this.fromUserId = followingUid;
        this.lat = lat;
        this.lon = lon;
        publicBeacon = false;
    }
    public PrivateBeacon(String fromUserId, String lat, String lon, boolean publicBeacon) {
        this.fromUserId = fromUserId;
        this.lat = lat;
        this.lon = lon;
        this.publicBeacon = publicBeacon;
    }

    public PrivateBeacon(String lat, String lon)
    {
        this.lat = lat;
        this.lon = lon;
    }


    public String getToUserId() {
        return toUserId;
    }
    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

}
