package com.comp3004.beacon.Messages;

import com.comp3004.beacon.User.BeaconUser;

import java.util.HashMap;

/**
 * Message will be user to register a user when they first login to create an entry for them in the database
 * At this moment this entry has been created tpo be used to store the friends of a given user.
 */
public class RegisterUserMessage {

    static final String MESSAGE_HEADER = "users";
    private String userId;
    private String displayName;
    private String userAuthToken;

    public RegisterUserMessage(BeaconUser user) {
        this.userId = user.getUserId();
        this.displayName = user.getDisplayName();
        this.userAuthToken = user.getUserAuthToken();

    }

    public String getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getUserAuthToken() {
        return userAuthToken;
    }
}
