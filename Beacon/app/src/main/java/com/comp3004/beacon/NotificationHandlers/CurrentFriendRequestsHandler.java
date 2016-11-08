package com.comp3004.beacon.NotificationHandlers;

import com.comp3004.beacon.Networking.FriendRequestMessage;
import com.comp3004.beacon.User.BeaconUser;

/**
 * Created by julianclayton on 2016-11-06.
 */
public class CurrentFriendRequestsHandler {


    private static CurrentFriendRequestsHandler currentFriendRequestsHandler = null;
    boolean currentFriendRequestExist;
    FriendRequestMessage friendRequestMessage;
    BeaconUser pendingAprovalUser;
    String displayName;

    public static CurrentFriendRequestsHandler getInstance() {
        if (currentFriendRequestsHandler == null) {
            currentFriendRequestsHandler = new CurrentFriendRequestsHandler();
        }
        return  currentFriendRequestsHandler;
    }
    public void setCurrentFriendRequestExist(boolean currentLocationRequestExist) {
        this.currentFriendRequestExist = currentLocationRequestExist;
    }

    public boolean doesCurrentFriendRequestExist() {
        return currentFriendRequestExist;
    }
    public FriendRequestMessage getFriendRequestMessage() {
        return friendRequestMessage;
    }

    public void setFriendRequestMessage(FriendRequestMessage friendRequestMessage) {
        this.friendRequestMessage = friendRequestMessage;
        displayName = friendRequestMessage.getDisplayName();
    }
    public String getDisplayName() {
        return displayName;
    }
    public BeaconUser getPendingAprovalUser() {
        return pendingAprovalUser;
    }

    public void setPendingAprovalUser(BeaconUser pendingAprovalUser) {
        this.pendingAprovalUser = pendingAprovalUser;
    }

}
