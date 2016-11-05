package com.comp3004.beacon.DatabaseListeners;

import android.database.CursorIndexOutOfBoundsException;

import com.comp3004.beacon.Networking.PublicBeaconHandler;
import com.comp3004.beacon.User.Beacon;
import com.comp3004.beacon.User.BeaconUser;
import com.comp3004.beacon.User.CurrentUserPublicBeaconHandler;
import com.comp3004.beacon.User.PrivateBeacon;
import com.comp3004.beacon.User.CurrentBeaconUser;
import com.comp3004.beacon.User.PublicBeacon;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.Arrays;
import java.util.List;

/**
 * Created by julianclayton on 16-10-19.
 */
public class PublicBeaconListener implements ChildEventListener {
    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        PublicBeacon publicBeacon = dataSnapshot.getValue(PublicBeacon.class);

        if (getUserId(dataSnapshot.getKey()).equals(CurrentBeaconUser.getInstance().getUserId())) {
            CurrentUserPublicBeaconHandler currentUserPublicBeaconHandler = CurrentUserPublicBeaconHandler.getInstance();
            currentUserPublicBeaconHandler.setHasPublicBeacon(true);
            currentUserPublicBeaconHandler.setPublicBeacon(publicBeacon);
            currentUserPublicBeaconHandler.setKey(dataSnapshot.getKey());
        }
        publicBeacon.setBeaconId(dataSnapshot.getKey());
        if (!publicBeacon.getFromUserId().equals(CurrentBeaconUser.getInstance().getUserId())) {
            PublicBeaconHandler.getInstance().addBeacon(dataSnapshot.getKey(), publicBeacon);
        }
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        PublicBeacon publicBeacon = dataSnapshot.getValue(PublicBeacon.class);
        if (!publicBeacon.getFromUserId().equals(CurrentBeaconUser.getInstance().getUserId())) {
            System.out.println("Loc: " + publicBeacon.getLon() + publicBeacon.getLat());
            PublicBeaconHandler.getInstance().addBeacon(dataSnapshot.getKey(), publicBeacon);
            for (Beacon beacon : CurrentBeaconUser.getInstance().getBeacons().values()) {
                if (beacon.getFromUserId().equals(publicBeacon.getFromUserId())) {
                    beacon.setLon(publicBeacon.getLon());
                    beacon.setLat(publicBeacon.getLat());
                }
            }
        }
    }


    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        PublicBeacon publicBeacon = dataSnapshot.getValue(PublicBeacon.class);
        PublicBeaconHandler.getInstance().removeBeacon(dataSnapshot.getKey());
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
