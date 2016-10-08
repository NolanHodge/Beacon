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
    private String photoUrl;
    private HashMap<String, BeaconUser> friends;
    FirebaseUser firebaseUser;

    public BeaconUser(FirebaseUser user, FirebaseInstanceId id) {
        userId = user.getUid();
        displayName = user.getDisplayName();
        userAuthToken = id.getToken();
        photoUrl = user.getPhotoUrl().toString();
        friends = new HashMap<String, BeaconUser>();
    }

    public BeaconUser() {}
    public BeaconUser(String userId, String displayName, String userAuthToken, String photoUrl) {
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
    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setUserAuthToken(String userAuthToken) {
        this.userAuthToken = userAuthToken;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public void setFriends(HashMap<String, BeaconUser> friends) {
        this.friends = friends;
    }

}
