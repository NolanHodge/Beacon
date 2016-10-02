package com.comp3004.beacon.Networking;

import android.net.Uri;

import com.comp3004.beacon.User.BeaconUser;
import com.comp3004.beacon.User.CurrentBeaconUser;

import java.util.HashMap;

/**
 * Created by julianclayton on 16-10-02.
 *
 * This message is intended to register a use if they are logging in for the first time, ie; add an
 * user entry in the DB.
 */
public class RegisterUserMessage {

    String userId;
    String displayName;
    String userAuthToken;
    HashMap friends;
    Uri photoUrl;

    public RegisterUserMessage() {
        BeaconUser currentUser = CurrentBeaconUser.getInstance();
        this.userId = currentUser.getUserId();
        this.displayName = currentUser.getDisplayName();
        this.userAuthToken = currentUser.getUserAuthToken();
        this.friends = currentUser.getFriends();
        this.photoUrl = currentUser.getPhotoUrl();
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
    public HashMap getFriends() {
        return friends;
    }

    public Uri getPhotoUrl() {
        return photoUrl;
    }

}
