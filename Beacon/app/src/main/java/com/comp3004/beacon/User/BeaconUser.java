package com.comp3004.beacon.User;

import android.net.Uri;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * An object to hold info and methods pertaining to the current user.
 */
public class BeaconUser {

    private String userId;
    private String displayName;
    private String userAuthToken;
    private Uri photoUrl;
    private HashMap<String, BeaconUser> friends;

    public BeaconUser(FirebaseUser user, FirebaseInstanceId id) {
        userId = user.getUid();
        displayName = user.getDisplayName();
        userAuthToken = id.getToken();
        photoUrl = user.getPhotoUrl();
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserAuthToken() {
        return userAuthToken;
    }
    public BeaconUser getFriend(String userId) {
        return friends.get(userId);
    }
    public Uri getPhotoUrl() {
        return photoUrl;
    }

}
