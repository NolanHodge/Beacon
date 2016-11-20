package com.comp3004.beacon.User;

import com.comp3004.beacon.NotificationHandlers.CurrentBeaconInvitationHandler;

import java.util.Arrays;
import java.util.List;

/**
 * Created by julianclayton on 16-10-10.
 */
public class PrivateBeacon extends Beacon {
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

    private boolean isPrivate() {
        List<String> ids = Arrays.asList(beaconId.split("_"));
        if (ids.size() == 3 && ids.get(2).equals("private")) {
            return true;
        }
        else return false;

    }
}
