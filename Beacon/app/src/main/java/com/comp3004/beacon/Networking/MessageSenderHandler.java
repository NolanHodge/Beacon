package com.comp3004.beacon.Networking;

import android.location.Location;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.comp3004.beacon.FirebaseServices.DatabaseManager;
import com.comp3004.beacon.User.CurrentBeaconUser;
import com.comp3004.beacon.User.CurrentUserPublicBeaconHandler;
import com.comp3004.beacon.User.PrivateBeacon;
import com.comp3004.beacon.User.PublicBeacon;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
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
        String beaconId = currentBeaconUser.getUserId() + "_" + System.currentTimeMillis() + "_private";
        FirebaseDatabase.getInstance().getReference().child("/" + senderId + "_beaconRequests/" + beaconId).setValue(beaconInvitationMessage);

    }

    //Method allows users to follow and track public beacons
    public void followBeacon(PublicBeacon publicBeacon) {

        PrivateBeacon privateBeacon = new PrivateBeacon(publicBeacon);
        FirebaseDatabase.getInstance().getReference().child("/" + CurrentBeaconUser.getInstance().getUserId() + "_beaconRequests").child(publicBeacon.getBeaconId()).setValue(privateBeacon);

    }

    public void sendPublicBeacon(LatLng latLng) {
        CurrentBeaconUser currentBeaconUser = CurrentBeaconUser.getInstance();

        for (Object key : PublicBeaconHandler.getInstance().getBeacons().keySet()) {
            PublicBeacon publicBeacon = PublicBeaconHandler.getInstance().getBeacon((String) key);
            if (publicBeacon.getFromUserId().equals(CurrentBeaconUser.getInstance().getUserId())) {

            }
        }

        PublicBeacon publicBeacon = new PublicBeacon(currentBeaconUser.getUserId(), currentBeaconUser.getDisplayName(), Double.toString(latLng.latitude), Double.toString(latLng.longitude));

        if (CurrentUserPublicBeaconHandler.getInstance().isHasPublicBeacon()) {
            CurrentUserPublicBeaconHandler currentUserPublicBeaconHandler = CurrentUserPublicBeaconHandler.getInstance();
            FirebaseDatabase.getInstance().getReference().child(MessageTypes.PUBLIC_BEACON + "/" + currentUserPublicBeaconHandler.getKey()).setValue(publicBeacon);
        }
        else { //create new entry
            FirebaseDatabase.getInstance().getReference().child(MessageTypes.PUBLIC_BEACON + "/" + currentBeaconUser.getUserId() + "_" + System.currentTimeMillis()).setValue(publicBeacon);
        }
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

    public void sendLocationRequest(String toUserId) {
        CurrentBeaconUser currentBeaconUser = CurrentBeaconUser.getInstance();

        String message = currentBeaconUser.getDisplayName() + "wants to know your location";
        Map notification = new HashMap<>();
        notification.put("toUserId", toUserId);
        notification.put("message", message);
        notification.put("fromUserId", currentBeaconUser.getUserId());

        FirebaseDatabase.getInstance().getReference().child(MessageTypes.LOCATION_REQUEST).push().setValue(notification);
        FirebaseDatabase.getInstance().getReference().child(toUserId + "_locationRequests").push().setValue(new LocationRequestMessage(toUserId, currentBeaconUser.getUserId()));

    }
    /**
     * This message is sent when the user logs in for the very first time to make an entry for them in the database
     *
     */
    public void sendRegisterUserMessage() {
        DatabaseManager.getInstance().isCurrentUserRegistered();
    }


    public void sendPhotoMessage(File photoFile) {


        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();

        UploadTask uploadTask = firebaseStorage.getReference().child(CurrentBeaconUser.getInstance().getUserId() + "_photos").putFile(Uri.fromFile(photoFile));

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                System.out.println("Successful image upload!");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                System.out.println("Unsuccessful image upload!");

            }
        });
    }

}
