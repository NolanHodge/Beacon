package com.comp3004.beacon.User;

import com.comp3004.beacon.FirebaseServices.DatabaseManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

/**
 * The Current User is just a BeaconUser singleton so that the information remains static and can
 * be edited in any activity
 */
public class CurrentBeaconUser extends BeaconUser {

    static CurrentBeaconUser currentBeaconUser = null;
    private boolean registered;

    public CurrentBeaconUser(FirebaseUser user, FirebaseInstanceId id) {
        super(user, id);
        //getFriends().put("as20830912743982798325", new BeaconUser("1233456", "John Doe", "as20830912743982798325", "www.test.com"));
        currentBeaconUser = this;
        registered = false;
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

}
