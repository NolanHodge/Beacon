package com.comp3004.beacon.User;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;

/**
 * The Current User is just a BeaconUser singleton so that the information remains static and can
 * be edited in any activity
 */
public class CurrentBeaconUser extends BeaconUser {

    static CurrentBeaconUser currentBeaconUser;

    public CurrentBeaconUser(FirebaseUser user, FirebaseInstanceId id) {
        super(user, id);
    }

    public static CurrentBeaconUser getInstance() {
        if (currentBeaconUser == null) {
            currentBeaconUser = new CurrentBeaconUser(FirebaseAuth.getInstance().getCurrentUser(), FirebaseInstanceId.getInstance());
        }
        return currentBeaconUser;
    }
}
