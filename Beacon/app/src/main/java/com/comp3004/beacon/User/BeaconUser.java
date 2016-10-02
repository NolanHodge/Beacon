package com.comp3004.beacon.User;

import android.net.Uri;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;

import java.net.URI;
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
        friends = new HashMap<String, BeaconUser>();
    }

    public BeaconUser() {}
    public BeaconUser(String userId, String displayName, String userAuthToken, Uri photoUrl) {
        this.userId = userId;
        this.displayName = displayName;
        this.userAuthToken = userAuthToken;
        this.photoUrl = photoUrl;
        friends = new HashMap<String, BeaconUser>();

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
    public HashMap getFriends() {
        return friends;
    }
    public Uri getPhotoUrl() {
        return photoUrl;
    }

}
