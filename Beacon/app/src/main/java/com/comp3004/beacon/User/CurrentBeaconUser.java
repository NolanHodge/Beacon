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

    static CurrentBeaconUser currentBeaconUser;

    public CurrentBeaconUser(FirebaseUser user, FirebaseInstanceId id) {
        super(user, id);
        //getFriends().put("as20830912743982798325", new BeaconUser("1233456", "John Doe", "as20830912743982798325", "www.test.com"));
        currentBeaconUser = this;
        //(String userId, String displayName, String userAuthToken, Uri photoUrl)
    }

    public static CurrentBeaconUser getInstance() {
        if (currentBeaconUser == null) {
            currentBeaconUser = new CurrentBeaconUser(FirebaseAuth.getInstance().getCurrentUser(), FirebaseInstanceId.getInstance());
        }
        return currentBeaconUser;
    }
}
