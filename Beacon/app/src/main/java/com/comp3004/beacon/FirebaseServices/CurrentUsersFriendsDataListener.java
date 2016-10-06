package com.comp3004.beacon.FirebaseServices;

import com.comp3004.beacon.User.BeaconUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

/**
 * Created by julianclayton on 16-10-04.
 */
public class CurrentUsersFriendsDataListener implements ValueEventListener {

    HashMap friends;

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        System.out.println("burf" + dataSnapshot.getChildren().toString());
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    public HashMap getFriends() {
        return friends;
    }
}
