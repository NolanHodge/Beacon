package com.comp3004.beacon;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

/**
 * Created by Jason on 01/01/2017.
 */

public class BeaconDatabase {

    public static Query getChats() {
        return FirebaseDatabase.getInstance().getReference("chats");
    }

    public static Query getUsers() {
        return FirebaseDatabase.getInstance().getReference("beaconUsers");
    }

    public static DatabaseReference getChat(String key) {
        return FirebaseDatabase.getInstance().getReference("chats").child(key);
    }

    public static DatabaseReference getUsers(String id) {
        return FirebaseDatabase.getInstance().getReference("beaconUsers").child(id);
    }
    public static Query getChatMessages(String id) {
        return FirebaseDatabase.getInstance().getReference("chats").child(id).child("messages");
    }
}
