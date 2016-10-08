package com.comp3004.beacon.DatabaseListeners;

import com.comp3004.beacon.User.BeaconUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by julianclayton on 16-10-04.
 */
public class CurrentUsersFriendsDataListener implements ValueEventListener {

    HashMap friends;

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        BeaconUser beaconUser = dataSnapshot.getValue(BeaconUser.class);
        BeaconUser friend = (BeaconUser) beaconUser.getFriends().get("as20830912743982798325");
        System.out.println("A FRIEND" + friend.getDisplayName());

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    public HashMap getFriends() {
        return friends;
    }
}
