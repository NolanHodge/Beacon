package com.comp3004.beacon;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

//for Background notifications
public class ListenerService extends Service {
    public static boolean RUNNING;

    public ListenerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        RUNNING = true;
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        RUNNING = false;
        super.onDestroy();
    }
}
