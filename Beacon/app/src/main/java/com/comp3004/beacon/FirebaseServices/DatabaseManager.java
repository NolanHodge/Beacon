package com.comp3004.beacon.FirebaseServices;

import android.provider.ContactsContract;

import com.comp3004.beacon.Networking.MessageTypes;
import com.comp3004.beacon.User.BeaconUser;
import com.comp3004.beacon.User.CurrentBeaconUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by julianclayton on 16-10-02.
 */
public class DatabaseManager {

    static DatabaseManager databaseManager;
    DatabaseReference databaseReference;

    public DatabaseManager() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public static DatabaseManager getInstance() {
        if (databaseManager == null) {
            databaseManager = new DatabaseManager();
        }
        return databaseManager;
    }

    public boolean isCurrentUserRegistered() {

        final BeaconUser[] beaconUser = new BeaconUser[1];
        final boolean[] found = new boolean[1];
        found[0] = false;
        Query query = databaseReference.child("/"+MessageTypes.REGISTER_USER_MESSAGE+"/"+ CurrentBeaconUser.getInstance().getUserId());

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    found[0] = true; //This is stupid but I can't find a way around it for now
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return found[0];
    }
}
