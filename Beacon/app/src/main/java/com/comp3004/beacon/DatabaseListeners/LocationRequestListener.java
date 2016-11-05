package com.comp3004.beacon.DatabaseListeners;

import com.comp3004.beacon.Networking.CurrentLocationRequestHandler;
import com.comp3004.beacon.Networking.LocationRequestMessage;
import com.comp3004.beacon.User.CurrentBeaconUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

/**
 * Created by julianclayton on 2016-11-05.
 */
public class LocationRequestListener implements ChildEventListener {
    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        LocationRequestMessage locationRequestMessage = dataSnapshot.getValue(LocationRequestMessage.class);
        CurrentLocationRequestHandler.getInstance().setLocationRequestMessage(locationRequestMessage);
        CurrentLocationRequestHandler.getInstance().setCurrentLocationRequestExists(true);
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
