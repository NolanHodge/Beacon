package com.comp3004.beacon.FirebaseServices;

import com.comp3004.beacon.DatabaseListeners.CurrentUsersFriendsDataListener;
import com.comp3004.beacon.DatabaseListeners.IsUserRegisteredDataListener;
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
    IsUserRegisteredDataListener isUserRegisteredDataListener;
    Query query;

    public DatabaseManager() {

        databaseReference = FirebaseDatabase.getInstance().getReference();
        isUserRegisteredDataListener = IsUserRegisteredDataListener.getInstance();

    }

    public static DatabaseManager getInstance() {
        if (databaseManager == null) {
            databaseManager = new DatabaseManager();
        }
        return databaseManager;
    }

    public boolean isCurrentUserRegistered() {
        Query query = databaseReference.child("/" + MessageTypes.REGISTER_USER_MESSAGE + "/" + CurrentBeaconUser.getInstance().getUserId());
        query.addValueEventListener(isUserRegisteredDataListener);
        System.out.println("REGISTERED:" + isUserRegisteredDataListener.isUserRegisterd());
        return isUserRegisteredDataListener.isUserRegisterd();
    }
    
    public HashMap getCurrentUsersFriends() {
        Query query = databaseReference.child("/" + MessageTypes.REGISTER_USER_MESSAGE + "/" + CurrentBeaconUser.getInstance().getUserId());
        CurrentUsersFriendsDataListener currentUsersFriendsDataListener = new CurrentUsersFriendsDataListener();
        query.addValueEventListener(currentUsersFriendsDataListener);
        return currentUsersFriendsDataListener.getFriends();
    }

}
