package com.comp3004.beacon.Networking;

import com.comp3004.beacon.User.BeaconUser;

/**
 * Created by julianclayton on 2016-11-06.
 */
public class CurrentFriendRequestsHandler {


    private static CurrentFriendRequestsHandler currentFriendRequestsHandler;
    boolean currentFriendRequestExist;
    FriendRequestMessage friendRequestMessage;
    BeaconUser pendingAprovalUser;

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
    }

    public BeaconUser getPendingAprovalUser() {
        return pendingAprovalUser;
    }

    public void setPendingAprovalUser(BeaconUser pendingAprovalUser) {
        this.pendingAprovalUser = pendingAprovalUser;
    }

}
