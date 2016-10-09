package com.comp3004.beacon.Networking;

import android.database.CursorIndexOutOfBoundsException;

import com.comp3004.beacon.User.CurrentBeaconUser;
import com.google.firebase.messaging.FirebaseMessaging;

/**
 * Created by julianclayton on 16-10-07.
 *
 * This will handle all the subscriptions the current user is subscribed to. It will subscribe them to the
 * default subscriptions
 */
public class SubscriptionHandler {

    String GLOBAL_MESSAGE = "global";

    private static SubscriptionHandler subscriptionHandler;

    private FirebaseMessaging firebaseMessaging;

    public SubscriptionHandler(FirebaseMessaging firebaseMessaging) {
        subscriptionHandler = this;
        this.firebaseMessaging = firebaseMessaging;

        //Default subscriptions here
        FirebaseMessaging.getInstance().subscribeToTopic("user_" + GLOBAL_MESSAGE);
        FirebaseMessaging.getInstance().subscribeToTopic("beaconRequests_" + CurrentBeaconUser.getInstance().getUserId());
        FirebaseMessaging.getInstance().subscribeToTopic("messageRequests_" + CurrentBeaconUser.getInstance().getUserId());

    }

    public static SubscriptionHandler getInstance() {
        if (subscriptionHandler == null) {
            subscriptionHandler = new SubscriptionHandler(FirebaseMessaging.getInstance());
        }
        return subscriptionHandler;
    }




}
