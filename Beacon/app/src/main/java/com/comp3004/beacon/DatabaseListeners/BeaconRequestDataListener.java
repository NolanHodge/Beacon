package com.comp3004.beacon.DatabaseListeners;

import com.comp3004.beacon.Networking.BeaconInvitationMessage;
import com.comp3004.beacon.Networking.CurrentBeaconInvitationHandler;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

/**
 * Created by julianclayton on 16-10-10.
 */
public class BeaconRequestDataListener implements ChildEventListener {


    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        BeaconInvitationMessage beaconInvitationMessage = (BeaconInvitationMessage)dataSnapshot.getValue(BeaconInvitationMessage.class);

        CurrentBeaconInvitationHandler currentBeaconInvititationHandler = CurrentBeaconInvitationHandler.getInstance();

        currentBeaconInvititationHandler.setLat(beaconInvitationMessage.getLat());
        currentBeaconInvititationHandler.setLon(beaconInvitationMessage.getLon());
        currentBeaconInvititationHandler.setMessage(beaconInvitationMessage.getMessage());
        currentBeaconInvititationHandler.setSenderId(beaconInvitationMessage.getSenderId());
        currentBeaconInvititationHandler.setTimestamp(beaconInvitationMessage.getTimestamp());




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
