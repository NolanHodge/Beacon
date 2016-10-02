package com.comp3004.beacon.Networking;

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
public class MessageSenderHandler {

    static MessageSenderHandler messageHandler;


    public static MessageSenderHandler getInstance() {
        if (messageHandler == null) {
            messageHandler = new MessageSenderHandler();
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

    /**
     * This message is sent when the user logs in for the very first time to make an entry for them in the database
     *
     */
    public void sendRegisterUserMessage() {
        FirebaseDatabase.getInstance().getReference().child(MessageTypes.REGISTER_USER_MESSAGE).push().setValue(
                new RegisterUserMessage()
        );
    }
}
