package com.comp3004.beacon.DatabaseListeners;

import com.comp3004.beacon.Networking.BeaconInvitationMessage;
import com.comp3004.beacon.NotificationHandlers.CurrentBeaconInvitationHandler;
import com.comp3004.beacon.User.PrivateBeacon;
import com.comp3004.beacon.User.CurrentBeaconUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.Arrays;
import java.util.List;

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
        currentBeaconInvititationHandler.setToUserId(beaconInvitationMessage.getToUserId());
        currentBeaconInvititationHandler.setTimestamp(beaconInvitationMessage.getTimestamp());
        currentBeaconInvititationHandler.setFromUserId(beaconInvitationMessage.getFromUserId());
        currentBeaconInvititationHandler.setBeaconId(dataSnapshot.getKey());

        PrivateBeacon privateBeacon = new PrivateBeacon(currentBeaconInvititationHandler);
        privateBeacon.setBeaconId(dataSnapshot.getKey());
        CurrentBeaconUser.getInstance().getBeacons().put(privateBeacon.getFromUserId(), privateBeacon);

        if (getUserId(dataSnapshot.getKey()).equals(CurrentBeaconUser.getInstance().getUserId())) {
            CurrentBeaconUser.getInstance().addMyBeaon(dataSnapshot.getKey(), privateBeacon);
        }



    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {



    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        PrivateBeacon privateBeacon = dataSnapshot.getValue(PrivateBeacon.class);

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
