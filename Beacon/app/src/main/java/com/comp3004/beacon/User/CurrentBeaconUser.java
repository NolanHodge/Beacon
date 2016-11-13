
package com.comp3004.beacon.User;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;

/**
 * The Current User is just a BeaconUser singleton so that the information remains static and can
 * be edited in any activity
 */
public class CurrentBeaconUser extends BeaconUser {

    static CurrentBeaconUser currentBeaconUser = null;
    private boolean registered;
    private HashMap<String, Beacon> myBeacons;

    public CurrentBeaconUser(FirebaseUser user, FirebaseInstanceId id) {
        super(user, id);
        friends.put(user.getUid(), new BeaconUser(user.getUid(), user.getDisplayName(), "tok", user.getPhotoUrl().toString()));
        currentBeaconUser = this;
        registered = false;
        myBeacons = new HashMap<String, Beacon>();
    }

    public static CurrentBeaconUser getInstance() {
        if (currentBeaconUser == null) {
            currentBeaconUser = new CurrentBeaconUser(FirebaseAuth.getInstance().getCurrentUser(), FirebaseInstanceId.getInstance());
        }
        return currentBeaconUser;
    }

    public void setIsRegistered(boolean reg) {
        registered = reg;
    }

    public boolean isRegistered() {
        return registered;
    }

    public void setCurrentBeaconUser(BeaconUser beaconUser) {
        currentBeaconUser.setDisplayName(beaconUser.getDisplayName());
        currentBeaconUser.setPhotoUrl(beaconUser.getPhotoUrl());
        currentBeaconUser.setUserAuthToken(beaconUser.getUserAuthToken());
        currentBeaconUser.setUserId(beaconUser.getUserId());
        currentBeaconUser.setFriends(beaconUser.getFriends());
    }

    public void addMyBeaon(String beaconId, Beacon beacon) {
        beacon.setBeaconId(beaconId);
        myBeacons.put(beaconId, beacon);
        beacon.setBeaconId(beaconId);
    }

    public void removeMyBeacon(String beaconId) {
        myBeacons.remove(beaconId);
    }

    public HashMap getMyBeacons() {
        return myBeacons;
    }
    public Beacon getMyBeacon(String id) {
        return myBeacons.get(id);
    }

}
