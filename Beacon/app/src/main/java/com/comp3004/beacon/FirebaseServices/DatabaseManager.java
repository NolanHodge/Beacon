package com.comp3004.beacon.FirebaseServices;

import android.provider.ContactsContract;

import com.comp3004.beacon.Networking.MessageTypes;
import com.comp3004.beacon.User.BeaconUser;
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

    public String getCurrentUserEntry() {
        BeaconUser beaconUser;
        Query query = databaseReference.child("/"+MessageTypes.REGISTER_USER_MESSAGE);


        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                BeaconUser beaconUser = dataSnapshot.getValue(BeaconUser.class);
                System.out.println("Beacon User: " + beaconUser.getUserId());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return "";
    }
}
