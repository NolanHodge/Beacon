package com.comp3004.beacon.DatabaseListeners;

import android.provider.ContactsContract;
import android.provider.Telephony;

import com.comp3004.beacon.FirebaseServices.DatabaseManager;
import com.comp3004.beacon.Networking.ChatMessage;
import com.comp3004.beacon.Networking.MailBox;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.Arrays;
import java.util.List;

/**
 * Created by julianclayton on 16-10-16.
 */
public class MessageThreadListener implements ChildEventListener {
    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        ChatMessage chatMessage = dataSnapshot.getValue(ChatMessage.class);
        MailBox.getInstance().addToChatMessageMailbox(chatMessage.getThreadId(), chatMessage);
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        ChatMessage chatMessage = dataSnapshot.getValue(ChatMessage.class);
        MailBox.getInstance().removeChatMessageFromThread(chatMessage.getThreadId(), chatMessage);
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

}
