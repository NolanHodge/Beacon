package com.comp3004.beacon.NotificationHandlers;

import com.comp3004.beacon.Networking.LocationRequestMessage;

/**
 * Created by julianclayton on 2016-11-05.
 */
public class CurrentLocationRequestHandler {


    private static CurrentLocationRequestHandler currentLocationRequestHandler;
    LocationRequestMessage locationRequestMessage;

    private boolean currentLocationRequestExists;

    public static CurrentLocationRequestHandler getInstance() {
        if (currentLocationRequestHandler == null) {
            currentLocationRequestHandler = new CurrentLocationRequestHandler();
        }
        return  currentLocationRequestHandler;
    }

    public LocationRequestMessage getLocationRequestMessage() {
        return locationRequestMessage;
    }

    public void setLocationRequestMessage(LocationRequestMessage locationRequestMessage) {
        this.locationRequestMessage = locationRequestMessage;
    }

    public void setCurrentLocationRequestExists(boolean cl) {
        currentLocationRequestExists = cl;
    }
    public boolean isCurrentLocationRequestExists() {
        return currentLocationRequestExists;
    }


}
