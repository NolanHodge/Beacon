package com.comp3004.beacon.Networking;

import com.google.firebase.messaging.FirebaseMessaging;

/**
 * Created by julianclayton on 16-10-07.
 *
 * This will handle all the subscriptions the current user is subscribed to. It will subscribe them to the
 * default subscriptions
 */
public class SubscriptionHandler {

    String global = "global";

    private static SubscriptionHandler subscriptionHandler;

    private FirebaseMessaging firebaseMessaging;

    public SubscriptionHandler(FirebaseMessaging firebaseMessaging) {
        subscriptionHandler = this;
        this.firebaseMessaging = firebaseMessaging;

        //Default subscriptions here
        FirebaseMessaging.getInstance().subscribeToTopic("user_" + global);

    }

    public static SubscriptionHandler getInstance() {
        if (subscriptionHandler == null) {
            subscriptionHandler = new SubscriptionHandler(FirebaseMessaging.getInstance());
        }
        return subscriptionHandler;
    }




}
