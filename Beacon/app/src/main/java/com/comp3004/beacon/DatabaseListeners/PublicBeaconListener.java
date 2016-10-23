package com.comp3004.beacon.DatabaseListeners;

import com.comp3004.beacon.Networking.PublicBeaconHandler;
import com.comp3004.beacon.User.PrivateBeacon;
import com.comp3004.beacon.User.CurrentBeaconUser;
import com.comp3004.beacon.User.PublicBeacon;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

/**
 * Created by julianclayton on 16-10-19.
 */
public class PublicBeaconListener implements ChildEventListener {
    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        PublicBeacon publicBeacon = dataSnapshot.getValue(PublicBeacon.class);
        PublicBeaconHandler.getInstance().addBeacon(dataSnapshot.getKey(), publicBeacon);
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
