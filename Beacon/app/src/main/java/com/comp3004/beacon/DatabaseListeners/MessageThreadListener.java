package com.comp3004.beacon.DatabaseListeners;

import android.provider.Telephony;

import com.comp3004.beacon.Networking.ChatMessage;
import com.comp3004.beacon.Networking.MailBox;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

/**
 * Created by julianclayton on 16-10-16.
 */
public class MessageThreadListener implements ChildEventListener {
    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        ChatMessage chatMessage = dataSnapshot.getValue(ChatMessage.class);
        System.out.println("Chat message " + chatMessage.getMessage());
        System.out.println("thread1:" + chatMessage.getThreadId());
        MailBox.getInstance().addToChatMessageMailbox(chatMessage.getThreadId(), chatMessage);
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
