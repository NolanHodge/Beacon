package com.comp3004.beacon.Messages;

import android.app.Notification;

import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by julianclayton on 16-10-01.
 *
 * This class will be used to create and send all messages to the server. Some are intended to be sent
 * to other clients, some are intended to just be stored in the DB.
 *
 * Singleton
 */
public class MessageHandler {

    static MessageHandler  messageHandler;


    public static MessageHandler getInstance() {
        if (messageHandler == null) {
            messageHandler = new MessageHandler();
        }
        return messageHandler;
    }

    public void sendBeaconRequest(String senderId) {

        //TODO make this a class of BeaconRequest Message not a HashMap
        Map notification = new HashMap<>();
        notification.put("username", senderId);
        notification.put("message", "Hey");

        FirebaseDatabase.getInstance().getReference().child(MessageTypes.BEACON_REQUEST_MESSAGE).push().setValue(notification);

    }
}
