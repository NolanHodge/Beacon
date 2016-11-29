package com.comp3004.beacon.FirebaseServices;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service {
    public static boolean RUNNING = true;
    private static final String TAG = "MESSAGE_RECEIVER";

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        RUNNING = true;
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate");
        DatabaseManager.getInstance().registerFriendRequestListener(getApplicationContext());
    }

    @Override
    public void onDestroy() throws SecurityException {
        Log.e(TAG, "onDestroy");
        RUNNING = false;
        super.onDestroy();

    }


}

