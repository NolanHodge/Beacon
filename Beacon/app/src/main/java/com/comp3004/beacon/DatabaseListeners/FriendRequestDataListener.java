package com.comp3004.beacon.DatabaseListeners;


import com.comp3004.beacon.Networking.CurrentFriendRequestsHandler;
import com.comp3004.beacon.Networking.FriendRequestMessage;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

/**
 * Created by julianclayton on 2016-11-06.
 */
public class FriendRequestDataListener implements ChildEventListener {

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        FriendRequestMessage friendRequestMessage = dataSnapshot.getValue(FriendRequestMessage.class);

        CurrentFriendRequestsHandler.getInstance().setCurrentFriendRequestExist(true);
        CurrentFriendRequestsHandler.getInstance().setFriendRequestMessage(friendRequestMessage);
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

