package com.comp3004.beacon.DatabaseListeners;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.CursorIndexOutOfBoundsException;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.NotificationCompat;

import com.comp3004.beacon.FirebaseServices.DatabaseManager;
import com.comp3004.beacon.GUI.FriendListActivity;
import com.comp3004.beacon.GUI.LoginActivity;
import com.comp3004.beacon.Networking.MailBox;
import com.comp3004.beacon.Networking.SubscriptionHandler;
import com.comp3004.beacon.R;
import com.comp3004.beacon.User.CurrentBeaconUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by julianclayton on 16-10-16.
 */
public class NewMessageThreadListener implements ChildEventListener {
    Context context;

    public NewMessageThreadListener(Context c) {
        context = c;
    }

    public NewMessageThreadListener() {
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        CurrentBeaconUser currentBeaconUser = CurrentBeaconUser.getInstance();

        String dbPath = "/" + currentBeaconUser.getUserId() + "_messageRequests/" + dataSnapshot.getKey();
        DatabaseManager.getInstance().createMessageThreadListener(dbPath);
        SubscriptionHandler.getInstance().subscribeToTopic(dataSnapshot.getKey());
        MailBox.getInstance().createNewMessageThread(dataSnapshot.getKey());
        /*if (context != null) {
            Notification n = new NotificationCompat.Builder(context)
                    .setContentIntent(PendingIntent.getActivity(context, 0, new Intent(context, FriendListActivity.class), 0))
                    .setContentTitle("Beacon")
                    .setTicker("New Message")
                    .setContentText("New Message")
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
