package com.comp3004.beacon.GUI;

import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.comp3004.beacon.LocationManagement.LocationService;
import com.comp3004.beacon.R;

public class ArrowActivity extends AppCompatActivity {
    LocationManager locationManager;
    static long MIN_TIME = 5000;
    static float MIN_DIST = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arrow);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);


        float[] results = {0};
        Location.distanceBetween(45, -75, 44.98, -75.041, results);
        TextView textView = (TextView) findViewById(R.id.txt_distance);
        textView.setText(String.format("%.2f m", results[0]));

        LocationService locationService = new LocationService() {
            @Override
            public void onLocationChanged(Location location) {
                System.out.println("Update Location");
                float[] results = {0};
                Location.distanceBetween(45, -75, 44.98, -75.041, results);
                TextView textView = (TextView) findViewById(R.id.txt_distance);
                textView.setText(String.format("%.2f m", results[0]));
            }
        };
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DIST, locationService);
        } catch (SecurityException e) {
            e.printStackTrace();
        }


    }
}
