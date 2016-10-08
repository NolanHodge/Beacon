package com.comp3004.beacon.DatabaseListeners;

import com.comp3004.beacon.Networking.MessageSenderHandler;
import com.comp3004.beacon.Networking.MessageTypes;
import com.comp3004.beacon.Networking.RegisterUserMessage;
import com.comp3004.beacon.User.BeaconUser;
import com.comp3004.beacon.User.CurrentBeaconUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by julianclayton on 16-10-04.
 */
public class IsUserRegisteredDataListener implements ValueEventListener {

    static boolean exists;

    private static IsUserRegisteredDataListener isUserRegisteredDataListener;

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

            if (dataSnapshot.exists() == false) {
                System.out.println("User exists in database");
                FirebaseDatabase.getInstance().getReference().child(MessageTypes.REGISTER_USER_MESSAGE).child(
                        CurrentBeaconUser.getInstance().getUserId())
                        .setValue(
                                new RegisterUserMessage()
                        );
            }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    public boolean isUserRegisterd() {
        return exists;
    }
}
