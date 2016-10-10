package com.comp3004.beacon.Networking;

/**
 * Created by julianclayton on 16-10-10.
 */
public class CurrentBeaconInvitationHandler extends BeaconInvitationMessage {

    private static CurrentBeaconInvitationHandler currentBeaconInvititationHandler;

    private boolean currentInvitationExists;

    public CurrentBeaconInvitationHandler() {
        currentBeaconInvititationHandler = this;
        currentInvitationExists = false;
    }

    public static CurrentBeaconInvitationHandler getInstance() {
        if (currentBeaconInvititationHandler == null) {
            currentBeaconInvititationHandler = new CurrentBeaconInvitationHandler();
        }
        return currentBeaconInvititationHandler;
    }

    public void setCurrentInvitationExists(boolean bi) {
        currentInvitationExists = bi;
    }
    public boolean currentInvitationExists() {
        return currentInvitationExists;
    }
}
