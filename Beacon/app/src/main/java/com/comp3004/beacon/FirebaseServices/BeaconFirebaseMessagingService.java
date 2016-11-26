package com.comp3004.beacon.FirebaseServices;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.comp3004.beacon.GUI.ChatActivity;
import com.comp3004.beacon.GUI.LoginActivity;
import com.comp3004.beacon.GUI.MapsActivity;
import com.comp3004.beacon.NotificationHandlers.CurrentBeaconInvitationHandler;
import com.comp3004.beacon.NotificationHandlers.CurrentFriendRequestsHandler;
import com.comp3004.beacon.NotificationHandlers.CurrentLocationRequestHandler;
import com.comp3004.beacon.R;
import com.comp3004.beacon.User.CurrentBeaconUser;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by julianclayton on 16-09-26.
 * This deals with INCOMING messages, MessageSenderHandler deals with outgoing.
 */
public class BeaconFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFMService";
    public MapsActivity activity;

    Context context;

    public void sendNotification(RemoteMessage remoteMessage) {
        String title = "", message = "";
        if (remoteMessage.getFrom().contains("beaconRequests")) {
            title = "Beacon Request";
        } else if (remoteMessage.getFrom().contains("messages")) {
            title = "New Message";
        } else if (remoteMessage.getFrom().contains("locationRequests")) {
            title = "Location Request";
        } else if (remoteMessage.getFrom().contains("acceptFriendRequests")) {
            title = "Friend Request Accepted";
        } else if (remoteMessage.getFrom().contains("friendRequests")) {
            title = "New Friend Request";
        } else {
            title = "Beacon";
            message = remoteMessage.getNotification().getBody();
        }
        Notification n = new NotificationCompat.Builder(getApplicationContext())
                .setContentIntent(PendingIntent.getActivity(getApplicationContext(), 0, new Intent(getApplicationContext(), LoginActivity.class), 0))
                .setContentTitle(title)
                .setTicker(message)
                .setContentText(message)
                .setSmallIcon(R.mipmap.ic_launcher_2)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_2))
                .setAutoCancel(true)
                .setSound(Uri.parse("android.resource://" + getPackageName() + "/raw/snd1"))
                .setVibrate(new long[]{0, 300, 100, 300}).build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, n);
    }

    public void onMessageReceived(RemoteMessage remoteMessage) {
        sendNotification(remoteMessage);
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
            dialogIntent.putExtra(MapsActivity.BEACON_REQUEST, true);


            PendingIntent resultPendingIntent =
                    PendingIntent.getActivity(
                            this,
                            0,
                            dialogIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );
            CurrentLocationRequestHandler currentLocationRequestHandler = CurrentLocationRequestHandler.getInstance();
            android.support.v4.app.NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.app_icon)
                            .setContentTitle("Beacon Request")
                            .setContentText("A friend wants you to know their location")
                            .setAutoCancel(true)
                            .setContentIntent(resultPendingIntent);

            int mNotificationId = 003;
            NotificationManager mNotifyMgr =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            mNotifyMgr.notify(mNotificationId, mBuilder.build());
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
            // startActivity(dialogIntent);
            dialogIntent.putExtra(MapsActivity.LOCATION_REQUEST, true);


            PendingIntent resultPendingIntent =
                    PendingIntent.getActivity(
                            this,
                            0,
                            dialogIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );
            CurrentLocationRequestHandler currentLocationRequestHandler = CurrentLocationRequestHandler.getInstance();
            android.support.v4.app.NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.app_icon)
                            .setContentTitle("Location Request")
                            .setContentText("A friend wants know your location")
                            .setAutoCancel(true)
                            .setContentIntent(resultPendingIntent);

            int mNotificationId = 002;
            NotificationManager mNotifyMgr =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            mNotifyMgr.notify(mNotificationId, mBuilder.build());
        }

        if (remoteMessage.getFrom().equals("/topics/friendRequests_" + CurrentBeaconUser.getInstance().getUserId())) {
            DatabaseManager.getInstance().registerFriendRequestListener();


            Intent dialogIntent = new Intent(this, MapsActivity.class);
            dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            dialogIntent.putExtra(MapsActivity.FRIEND_REQUEST, true);

            PendingIntent resultPendingIntent =
                    PendingIntent.getActivity(
                            this,
                            0,
                            dialogIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );
            CurrentFriendRequestsHandler currentFriendRequestsHandler = CurrentFriendRequestsHandler.getInstance();
            android.support.v4.app.NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.app_icon)
                            .setContentTitle("Friend Request")
                            .setContentText("A Beacon user wants to add you as a friend")
                            .setAutoCancel(true)
                            .setContentIntent(resultPendingIntent);

            int mNotificationId = 001;
            NotificationManager mNotifyMgr =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            mNotifyMgr.notify(mNotificationId, mBuilder.build());

        }

        if (remoteMessage.getFrom().equals("/topics/acceptFriendRequests_" + CurrentBeaconUser.getInstance().getUserId())) {
            DatabaseManager.getInstance().addFriend(CurrentFriendRequestsHandler.getInstance().getPendingAprovalUser());
            remoteMessage.getNotification();


        }
    }
}
