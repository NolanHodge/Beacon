package com.comp3004.beacon.FirebaseServices;

import com.comp3004.beacon.User.BeaconUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by julianclayton on 16-10-04.
 */
public class IsUserRegisteredDataListener implements ValueEventListener {

    static boolean exists;

    private static IsUserRegisteredDataListener isUserRegisteredDataListener;
    public IsUserRegisteredDataListener() {
        isUserRegisteredDataListener = this;
    }
    public static IsUserRegisteredDataListener getInstance() {
        if (isUserRegisteredDataListener == null) {
            isUserRegisteredDataListener = new IsUserRegisteredDataListener();
        }
        return isUserRegisteredDataListener;
    }
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

            this.exists = dataSnapshot.exists(); //This is stupid but I can't find a way around it for now
            BeaconUser beaconUser = dataSnapshot.getValue(BeaconUser.class);
            System.out.println("EXISTS" + exists);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    public boolean isUserRegisterd() {
        return exists;
    }
}
