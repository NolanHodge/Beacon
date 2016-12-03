package com.comp3004.beacon.DatabaseListeners;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;

import com.comp3004.beacon.GUI.LoginActivity;
import com.comp3004.beacon.GUI.MapsActivity;
import com.comp3004.beacon.Networking.FriendRequestMessage;
import com.comp3004.beacon.NotificationHandlers.CurrentFriendRequestsHandler;
import com.comp3004.beacon.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by julianclayton on 2016-11-06.
 */
public class FriendRequestDataListener implements ChildEventListener {
    Context context;

    public FriendRequestDataListener(Context context) {
        this.context = context;
    }

    public FriendRequestDataListener() {
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        FriendRequestMessage friendRequestMessage = dataSnapshot.getValue(FriendRequestMessage.class);
        CurrentFriendRequestsHandler.getInstance().setCurrentFriendRequestExist(true);
        CurrentFriendRequestsHandler.getInstance().setFriendRequestMessage(friendRequestMessage);

        /*if (context != null) {
            Intent dialogIntent = new Intent(context, MapsActivity.class);
            dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            dialogIntent.putExtra(MapsActivity.FRIEND_REQUEST, true);

            PendingIntent resultPendingIntent =
                    PendingIntent.getActivity(
                            context,
                            0,
                            dialogIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );
            Notification n = new NotificationCompat.Builder(context)
                    .setContentIntent(resultPendingIntent)
                    .setContentTitle("Beacon")
                    .setTicker("Fried Request")
                    .setContentText("Friend request from " + friendRequestMessage.getDisplayName())
                    .setSmallIcon(R.mipmap.ic_launcher_2)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher_2))
                    .setAutoCancel(true)
                    .setSound(Uri.parse("android.resource://" + context.getPackageName() + "/raw/snd1"))
                    .setVibrate(new long[]{0, 300, 100, 300}).build();
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(0, n);
        }*/

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

