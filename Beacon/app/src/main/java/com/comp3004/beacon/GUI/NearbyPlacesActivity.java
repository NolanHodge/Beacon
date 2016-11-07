package com.comp3004.beacon.GUI;

import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.comp3004.beacon.LocationManagement.LocationService;
import com.comp3004.beacon.R;

public class NearbyPlacesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_places);

        final LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        final LocationService locationService = new LocationService() {
            @Override
            public void onLocationChanged(Location location) {

                try {
                    locationManager.removeUpdates(this);
                } catch (SecurityException e) {
                }
            }
        };
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, locationService);
        } catch (SecurityException e) {
        }
    }
}
