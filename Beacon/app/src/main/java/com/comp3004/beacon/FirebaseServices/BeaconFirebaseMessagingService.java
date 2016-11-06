package com.comp3004.beacon.FirebaseServices;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.location.LocationManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.comp3004.beacon.GUI.ChatActivity;
import com.comp3004.beacon.GUI.MapsActivity;
import com.comp3004.beacon.Networking.ChatMessage;
import com.comp3004.beacon.Networking.CurrentBeaconInvitationHandler;
import com.comp3004.beacon.User.CurrentBeaconUser;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;

/**
 * Created by julianclayton on 16-09-26.
 * This deals with INCOMING messages, MessageSenderHandler deals with outgoing.
 */
public class BeaconFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFMService";
    public MapsActivity activity;




    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getFrom());
        }

        //Handle messages
        if (remoteMessage.getFrom().equals("/topics/beaconRequests_" + CurrentBeaconUser.getInstance().getUserId())) {
            CurrentBeaconInvitationHandler.getInstance().setCurrentInvitationExists(true);
            Intent dialogIntent = new Intent(this, MapsActivity.class);
            dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(dialogIntent);
        }

        if (remoteMessage.getFrom().equals("/topics/messages_" + CurrentBeaconUser.getInstance().getUserId())) {
            DatabaseManager.getInstance().subscribeToMessageThread();
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction(ChatActivity.UPDATE_MESSAGE_THREAD);

            sendBroadcast(broadcastIntent);

        }

        if (remoteMessage.getFrom().equals("/topics/locationRequests_" + CurrentBeaconUser.getInstance().getUserId())) {
            DatabaseManager.getInstance().registerLocationRequestListener();

            Intent dialogIntent = new Intent(this, MapsActivity.class);
            dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(dialogIntent);
        }

        if (remoteMessage.getFrom().equals("/topics/friendRequests_" + CurrentBeaconUser.getInstance().getUserId())) {
            System.out.println("Bitch please");
        }


    }
    public void updatePhotoBroadast() {

    }

}
