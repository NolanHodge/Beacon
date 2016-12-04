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
import com.comp3004.beacon.Networking.BeaconInvitationMessage;
import com.comp3004.beacon.NotificationHandlers.CurrentBeaconInvitationHandler;
import com.comp3004.beacon.R;
import com.comp3004.beacon.User.PrivateBeacon;
import com.comp3004.beacon.User.CurrentBeaconUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.Arrays;
import java.util.List;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by julianclayton on 16-10-10.
 */
public class BeaconRequestDataListener implements ChildEventListener {
    Context context;

    public BeaconRequestDataListener() {
    }

    public BeaconRequestDataListener(Context c) {
        context = c;
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        BeaconInvitationMessage beaconInvitationMessage = (BeaconInvitationMessage) dataSnapshot.getValue(BeaconInvitationMessage.class);

        CurrentBeaconInvitationHandler currentBeaconInvititationHandler = CurrentBeaconInvitationHandler.getInstance();

        currentBeaconInvititationHandler.setLat(beaconInvitationMessage.getLat());
        currentBeaconInvititationHandler.setLon(beaconInvitationMessage.getLon());
        currentBeaconInvititationHandler.setMessage(beaconInvitationMessage.getMessage());
        currentBeaconInvititationHandler.setToUserId(beaconInvitationMessage.getToUserId());
        currentBeaconInvititationHandler.setTimestamp(beaconInvitationMessage.getTimestamp());
        currentBeaconInvititationHandler.setFromUserId(beaconInvitationMessage.getFromUserId());
        currentBeaconInvititationHandler.setBeaconId(dataSnapshot.getKey());

        PrivateBeacon privateBeacon = new PrivateBeacon(currentBeaconInvititationHandler);
        privateBeacon.setBeaconId(dataSnapshot.getKey());
        CurrentBeaconUser.getInstance().getBeacons().put(dataSnapshot.getKey(), privateBeacon);

        if (getUserId(dataSnapshot.getKey()).equals(CurrentBeaconUser.getInstance().getUserId())) {
            privateBeacon.setBeaconId(dataSnapshot.getKey());
            CurrentBeaconUser.getInstance().addMyBeaon(dataSnapshot.getKey(), privateBeacon);
        }
       /* if (context != null) {
            Intent dialogIntent = new Intent(context, MapsActivity.class);
            dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            dialogIntent.putExtra(MapsActivity.BEACON_REQUEST, true);


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
                    .setTicker("Beacon Request")
                    .setContentText("Someone requested a beacon")
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
        PrivateBeacon privateBeacon = dataSnapshot.getValue(PrivateBeacon.class);

        if (getUserId(dataSnapshot.getKey()).equals(CurrentBeaconUser.getInstance().getUserId())) {
            CurrentBeaconUser.getInstance().removeMyBeacon(dataSnapshot.getKey());
        }
        CurrentBeaconUser.getInstance().getBeacons().remove(dataSnapshot.getKey());


    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    public String getUserId(String id) {

        List<String> ids = Arrays.asList(id.split("_"));
        String id1 = ids.get(0);
        return id1;
    }
}
