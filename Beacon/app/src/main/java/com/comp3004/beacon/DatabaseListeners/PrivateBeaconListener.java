package com.comp3004.beacon.DatabaseListeners;

import com.comp3004.beacon.User.CurrentBeaconUser;
import com.comp3004.beacon.User.PrivateBeacon;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.Arrays;
import java.util.List;

/**
 * Created by julianclayton on 2016-11-07.
 */
public class PrivateBeaconListener implements ChildEventListener {
    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        PrivateBeacon privateBeacon = dataSnapshot.getValue(PrivateBeacon.class);
        if (getUserId(dataSnapshot.getKey()).equals(CurrentBeaconUser.getInstance().getUserId())) {
            CurrentBeaconUser.getInstance().addMyBeaon(dataSnapshot.getKey(), privateBeacon);
        }
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        if (getUserId(dataSnapshot.getKey()).equals(CurrentBeaconUser.getInstance().getUserId())) {
            CurrentBeaconUser.getInstance().removeMyBeacon(dataSnapshot.getKey());
        }
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    public String getUserId(String id) {

        List<String> ids = Arrays.asList(id.split("_"));
        String id1 = ids.get(0);
        return id1;
    }
}
