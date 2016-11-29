package com.comp3004.beacon.FirebaseServices;

import android.content.Context;
import android.database.CursorIndexOutOfBoundsException;
import android.support.annotation.NonNull;

import com.comp3004.beacon.DatabaseListeners.BeaconRequestDataListener;
import com.comp3004.beacon.DatabaseListeners.CurrentUsersFriendsDataListener;
import com.comp3004.beacon.DatabaseListeners.FriendRequestDataListener;
import com.comp3004.beacon.DatabaseListeners.IsUserRegisteredDataListener;
import com.comp3004.beacon.DatabaseListeners.LocationRequestListener;
import com.comp3004.beacon.DatabaseListeners.MessageThreadListener;
import com.comp3004.beacon.DatabaseListeners.NewMessageThreadListener;
import com.comp3004.beacon.DatabaseListeners.PrivateBeaconListener;
import com.comp3004.beacon.DatabaseListeners.PublicBeaconListener;
import com.comp3004.beacon.Networking.FriendRequestMessage;
import com.comp3004.beacon.Networking.LocationRequestMessage;
import com.comp3004.beacon.Networking.MessageTypes;
import com.comp3004.beacon.Networking.PhotoSenderHandler;
import com.comp3004.beacon.User.Beacon;
import com.comp3004.beacon.User.BeaconUser;
import com.comp3004.beacon.User.PrivateBeacon;
import com.comp3004.beacon.User.CurrentBeaconUser;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.HashMap;

/**
 * Created by julianclayton on 16-10-02.
 */
public class DatabaseManager {

    static DatabaseManager databaseManager;
    public DatabaseReference databaseReference;
    Query query;

    HashMap<String, PrivateBeacon> publicBeacons;

    public DatabaseManager() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        query = databaseReference.child("/" + CurrentBeaconUser.getInstance().getUserId() + "_beaconRequests");
        BeaconRequestDataListener beaconRequestDataListener = new BeaconRequestDataListener();
        query.addChildEventListener(beaconRequestDataListener);
        publicBeacons = new HashMap<>();

        registerPrivateBeaconListener();

    }

    public static DatabaseManager getInstance() {
        if (databaseManager == null) {
            databaseManager = new DatabaseManager();
        }
        return databaseManager;
    }

    public void isCurrentUserRegistered() {
        Query query = databaseReference.child("/" + MessageTypes.REGISTER_USER_MESSAGE + "/" + CurrentBeaconUser.getInstance().getUserId());
        IsUserRegisteredDataListener isUserRegisteredDataListener = new IsUserRegisteredDataListener();
        query.addValueEventListener(isUserRegisteredDataListener);
    }

    public void loadCurrentUser() {
        Query query = databaseReference.child("/" + MessageTypes.REGISTER_USER_MESSAGE + "/" + CurrentBeaconUser.getInstance().getUserId());
        CurrentUsersFriendsDataListener currentUsersFriendsDataListener = new CurrentUsersFriendsDataListener();
        query.addValueEventListener(currentUsersFriendsDataListener);
        loadPublicBeacons();
    }

    public void removeBeaconFromDb(String beaconId) {
        DatabaseReference dbNode = FirebaseDatabase.getInstance().getReference().getRoot().child("/" + CurrentBeaconUser.getInstance().getUserId() + "_beaconRequests/" + beaconId);
        dbNode.removeValue();

        dbNode = FirebaseDatabase.getInstance().getReference().getRoot().child("beaconRequest/" + beaconId);
        dbNode.removeValue();
    }

    public void removeYourBeaconFromDb(Beacon beacon) {
        DatabaseReference ref;
        ref = FirebaseDatabase.getInstance().getReference()
                .getRoot()
                .child("/" + beacon.getToUserId() + "_beaconRequests/" + beacon.getBeaconId());
        ref.removeValue();

        ref = FirebaseDatabase.getInstance().getReference()
                .getRoot()
                .child("/beaconRequest/" + beacon.getBeaconId());
        ref.removeValue();

        if (beacon.isPublicBeacon()) {
            ref = FirebaseDatabase.getInstance().getReference()
                    .getRoot()
                    .child("/publicBeacons/" + beacon.getBeaconId());
            ref.removeValue();
        }
    }

    public void subscribeToMessageThread() {
        CurrentBeaconUser currentBeaconUser = CurrentBeaconUser.getInstance();
        Query query = databaseReference.child("/" + currentBeaconUser.getUserId() + "_messageRequests");
        NewMessageThreadListener newMessageThreadListener = new NewMessageThreadListener();
        query.addChildEventListener(newMessageThreadListener);
    }

    public void subscribeToMessageThread(Context c) {
        CurrentBeaconUser currentBeaconUser = CurrentBeaconUser.getInstance();
        Query query = databaseReference.child("/" + currentBeaconUser.getUserId() + "_messageRequests");
        NewMessageThreadListener newMessageThreadListener = new NewMessageThreadListener(c);
        query.addChildEventListener(newMessageThreadListener);
    }

    public void createMessageThreadListener(String threadId) {
        Query query = databaseReference.child(threadId);
        MessageThreadListener messageThreadListener = new MessageThreadListener();
        query.addChildEventListener(messageThreadListener);
    }

    public void getMessagesFromDb() {
        query = databaseReference.child("/" + CurrentBeaconUser.getInstance().getUserId() + "_messageRequests");
        BeaconRequestDataListener beaconRequestDataListener = new BeaconRequestDataListener();
        query.addChildEventListener(beaconRequestDataListener);

    }

    public void loadPublicBeacons() {
        query = databaseReference.child("/publicBeacons");
        PublicBeaconListener publicBeaconListener = new PublicBeaconListener();
        query.addChildEventListener(publicBeaconListener);

    }

    public void registerLocationRequestListener() {
        Query query = databaseReference.child(CurrentBeaconUser.getInstance().getUserId() + "_locationRequests");
        LocationRequestListener locationRequestListener = new LocationRequestListener();
        query.addChildEventListener(locationRequestListener);
    }

    public void addFriend(BeaconUser beaconUser) {
        databaseReference.child("beaconUsers/" + CurrentBeaconUser.getInstance().getUserId() + "/friends").child(beaconUser.getUserId()).setValue(beaconUser);
    }

    public void createPublicBeacon(PrivateBeacon privateBeacon) {
        databaseReference.child("/publicBeacons").push().setValue(privateBeacon);
    }

    public void registerPrivateBeaconListener() {
        Query query = databaseReference.child("beaconRequest/");
        PrivateBeaconListener privateBeaconListener = new PrivateBeaconListener();
        query.addChildEventListener(privateBeaconListener);
    }

    public void registerPrivateBeaconListener(Context c) {
        Query query = databaseReference.child("beaconRequest/");
        PrivateBeaconListener privateBeaconListener = new PrivateBeaconListener(c);
        query.addChildEventListener(privateBeaconListener);
    }

    public void registerFriendRequestListener() {
        Query query = databaseReference.child(CurrentBeaconUser.getInstance().getUserId() + "_friendRequests");
        FriendRequestDataListener friendRequestDataListener = new FriendRequestDataListener();
        query.addChildEventListener(friendRequestDataListener);

    }

    public void registerFriendRequestListener(Context c) {
        Query query = databaseReference.child(CurrentBeaconUser.getInstance().getUserId() + "_friendRequests");
        FriendRequestDataListener friendRequestDataListener = new FriendRequestDataListener(c);
        query.addChildEventListener(friendRequestDataListener);

    }

    public File loadPhotos(final String userId) {

        File localFile;
        StorageReference storageReference = FirebaseStorage.getInstance().getReference(userId + "_photos");
        localFile = new File("sdcard/camera_app/camera_img1.jpg");

        final File finalLocalFile = localFile;
        storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                PhotoSenderHandler.getInstance().addFile(userId, finalLocalFile);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                exception.printStackTrace();
            }
        });

        return localFile;
    }

}
