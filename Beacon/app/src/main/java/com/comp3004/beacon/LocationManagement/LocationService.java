package com.comp3004.beacon.LocationManagement;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.TextView;

import com.comp3004.beacon.R;

/**
 * Created by julianclayton on 16-09-26.
 */
public abstract class LocationService implements LocationListener{


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
