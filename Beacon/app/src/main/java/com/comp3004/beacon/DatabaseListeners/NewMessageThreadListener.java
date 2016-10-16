package com.comp3004.beacon.DatabaseListeners;

import android.database.CursorIndexOutOfBoundsException;
import android.provider.ContactsContract;

import com.comp3004.beacon.FirebaseServices.DatabaseManager;
import com.comp3004.beacon.Networking.SubscriptionHandler;
import com.comp3004.beacon.User.CurrentBeaconUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;

/**
 * Created by julianclayton on 16-10-16.
 */
public class NewMessageThreadListener implements ChildEventListener {


    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        CurrentBeaconUser currentBeaconUser = CurrentBeaconUser.getInstance();

        String dbPath = "/" + currentBeaconUser.getUserId() + "_messageRequests/" + dataSnapshot.getKey();
        System.out.println("Subscribed to " + dbPath);
        DatabaseManager.getInstance().createMessageThreadListener(dbPath);
        SubscriptionHandler.getInstance().subscribeToTopic(dataSnapshot.getKey());
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
