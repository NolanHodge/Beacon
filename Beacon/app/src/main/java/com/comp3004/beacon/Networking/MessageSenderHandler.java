package com.comp3004.beacon.Networking;

import android.location.Location;

import com.comp3004.beacon.FirebaseServices.DatabaseManager;
import com.comp3004.beacon.User.CurrentBeaconUser;
import com.comp3004.beacon.User.PrivateBeacon;
import com.comp3004.beacon.User.PublicBeacon;
import com.google.android.gms.maps.model.LatLng;
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


    public MessageSenderHandler() {
        messageHandler = this;
    }

    public static MessageSenderHandler getInstance() {
        if (messageHandler == null) {
            messageHandler = new MessageSenderHandler();
        }
        return messageHandler;
    }

    public void sendBeaconRequest(String senderId, Location current) {
        CurrentBeaconUser currentBeaconUser = CurrentBeaconUser.getInstance();
        String beaconMessage = currentBeaconUser.getDisplayName() + " wants you to follow their Beacon!";
        Map notification = new HashMap<>();
        notification.put("toUserId", senderId);
        notification.put("message", beaconMessage);
        notification.put("from", currentBeaconUser.getUserId());

        new LatLng(current.getLatitude(), current.getLongitude());

        FirebaseDatabase.getInstance().getReference().child(MessageTypes.BEACON_REQUEST_MESSAGE).push().setValue(notification);

        BeaconInvitationMessage beaconInvitationMessage = new BeaconInvitationMessage(senderId, CurrentBeaconUser.getInstance().getUserId(), beaconMessage, Double.toString(current.getLatitude()), Double.toString(current.getLongitude()), false);

        FirebaseDatabase.getInstance().getReference().child("/" + senderId + "_beaconRequests").push().setValue(beaconInvitationMessage);
    }

    //Method allows users to follow and track public beacons
    public void followBeacon(PublicBeacon publicBeacon) {

        PrivateBeacon privateBeacon = new PrivateBeacon(publicBeacon);
        FirebaseDatabase.getInstance().getReference().child("/" + CurrentBeaconUser.getInstance().getUserId() + "_beaconRequests").push().setValue(privateBeacon);

    }

    public void sendPublicBeacon(LatLng latLng) {
        CurrentBeaconUser currentBeaconUser = CurrentBeaconUser.getInstance();


        PublicBeacon publicBeacon = new PublicBeacon(currentBeaconUser.getUserId(), currentBeaconUser.getDisplayName(), Double.toString(latLng.latitude), Double.toString(latLng.longitude));
        FirebaseDatabase.getInstance().getReference().child(MessageTypes.PUBLIC_BEACON).push().setValue(publicBeacon);



    }

    public void sendMessage(String toUserId, String message) {
        CurrentBeaconUser currentBeaconUser = CurrentBeaconUser.getInstance();
        String threadIdOne = toUserId + "_" + currentBeaconUser.getUserId();
        String threadIdTwo = currentBeaconUser.getUserId() + "_" + toUserId;
        Map notification = new HashMap<>();
        notification.put("toUserId", toUserId);
        notification.put("message", message);
        notification.put("from", currentBeaconUser.getUserId());

        FirebaseDatabase.getInstance().getReference().child(MessageTypes.MESSAGE_REQUEST).push().setValue(notification);
        ChatMessage chatMessage = new ChatMessage(toUserId, currentBeaconUser.getUserId(), message, threadIdOne);

        FirebaseDatabase.getInstance().getReference().child("/" + toUserId + "_messageRequests/" + threadIdOne).push().setValue(chatMessage);
        FirebaseDatabase.getInstance().getReference().child("/" + currentBeaconUser.getUserId()+ "_messageRequests/" + threadIdTwo).push().setValue(chatMessage);

    }
    /**
     * This message is sent when the user logs in for the very first time to make an entry for them in the database
     *
     */
    public void sendRegisterUserMessage() {
        DatabaseManager.getInstance().isCurrentUserRegistered();
    }
}
