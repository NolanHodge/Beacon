package com.comp3004.beacon.FirebaseServices;

import android.support.annotation.NonNull;

import com.comp3004.beacon.DatabaseListeners.BeaconRequestDataListener;
import com.comp3004.beacon.DatabaseListeners.CurrentUsersFriendsDataListener;
import com.comp3004.beacon.DatabaseListeners.IsUserRegisteredDataListener;
import com.comp3004.beacon.DatabaseListeners.MessageThreadListener;
import com.comp3004.beacon.DatabaseListeners.NewMessageThreadListener;
import com.comp3004.beacon.DatabaseListeners.PublicBeaconListener;
import com.comp3004.beacon.Networking.MessageTypes;
import com.comp3004.beacon.Networking.PhotoSenderHandler;
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
    DatabaseReference databaseReference;
    Query query;

    HashMap<String, PrivateBeacon> publicBeacons;

    public DatabaseManager() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        query = databaseReference.child("/" + CurrentBeaconUser.getInstance().getUserId() + "_beaconRequests");
        BeaconRequestDataListener beaconRequestDataListener = new BeaconRequestDataListener();
        query.addChildEventListener(beaconRequestDataListener);
        publicBeacons = new HashMap<>();
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
    }

    public void subscribeToMessageThread() {
        CurrentBeaconUser currentBeaconUser = CurrentBeaconUser.getInstance();
        Query query = databaseReference.child("/" + currentBeaconUser.getUserId() + "_messageRequests");
        NewMessageThreadListener newMessageThreadListener = new NewMessageThreadListener();
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

    public void createPublicBeacon(PrivateBeacon privateBeacon) {
        databaseReference.child("/publicBeacons").push().setValue(privateBeacon);
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
