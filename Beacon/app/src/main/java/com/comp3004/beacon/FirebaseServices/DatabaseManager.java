package com.comp3004.beacon.FirebaseServices;

import android.database.CursorIndexOutOfBoundsException;

import com.comp3004.beacon.DatabaseListeners.BeaconRequestDataListener;
import com.comp3004.beacon.DatabaseListeners.CurrentUsersFriendsDataListener;
import com.comp3004.beacon.DatabaseListeners.IsUserRegisteredDataListener;
import com.comp3004.beacon.DatabaseListeners.MessageThreadListener;
import com.comp3004.beacon.DatabaseListeners.NewMessageThreadListener;
import com.comp3004.beacon.Networking.MessageTypes;
import com.comp3004.beacon.User.CurrentBeaconUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;

/**
 * Created by julianclayton on 16-10-02.
 */
public class DatabaseManager {

    static DatabaseManager databaseManager;
    DatabaseReference databaseReference;
    Query query;

    public DatabaseManager() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        query = databaseReference.child("/" + CurrentBeaconUser.getInstance().getUserId() + "_beaconRequests");
        BeaconRequestDataListener beaconRequestDataListener = new BeaconRequestDataListener();
        query.addChildEventListener(beaconRequestDataListener);

    }

    public static DatabaseManager getInstance() {
        if (databaseManager == null) {
            databaseManager = new DatabaseManager();
        }
        return databaseManager;
    }

    public void isCurrentUserRegistered() {
        Query query = databaseReference.child("/" + MessageTypes.REGISTER_USER_MESSAGE + "/" + CurrentBeaconUser.getInstance().getUserId());
        IsUserRegisteredDataListener isUserRegisteredDataListener = new IsUserRegisteredDataListener();
        query.addValueEventListener(isUserRegisteredDataListener);
    }

    public void loadCurrentUser() {
        Query query = databaseReference.child("/" + MessageTypes.REGISTER_USER_MESSAGE + "/" + CurrentBeaconUser.getInstance().getUserId());
        CurrentUsersFriendsDataListener currentUsersFriendsDataListener = new CurrentUsersFriendsDataListener();
        query.addValueEventListener(currentUsersFriendsDataListener);
    }

    public void removeBeaconFromDb(String beaconId) {
        DatabaseReference dbNode = FirebaseDatabase.getInstance().getReference().getRoot().child("/" + CurrentBeaconUser.getInstance().getUserId() + "_beaconRequests/" + beaconId);
        dbNode.removeValue();
    }

    public void subscribeToMessageThread() {
        CurrentBeaconUser currentBeaconUser = CurrentBeaconUser.getInstance();
        Query query = databaseReference.child("/" + currentBeaconUser.getUserId() + "_messageRequests");
        NewMessageThreadListener newMessageThreadListener = new NewMessageThreadListener();
        query.addChildEventListener(newMessageThreadListener);
    }

    public void createMessageThreadListener(String threadId) {
        Query query = databaseReference.child(threadId);
        MessageThreadListener messageThreadListener = new MessageThreadListener();
        query.addChildEventListener(messageThreadListener);
    }

    public void getMessagesFromDb() {
        query = databaseReference.child("/" + CurrentBeaconUser.getInstance().getUserId() + "_messageRequests");
        BeaconRequestDataListener beaconRequestDataListener = new BeaconRequestDataListener();
        query.addChildEventListener(beaconRequestDataListener);

    }

}
