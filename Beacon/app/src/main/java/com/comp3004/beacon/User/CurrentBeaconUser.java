package com.comp3004.beacon.User;

import android.net.Uri;

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

    public boolean isUserRegistered() {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        DatabaseManager.getInstance().getCurrentUserEntry();
        return true;
    }


}
