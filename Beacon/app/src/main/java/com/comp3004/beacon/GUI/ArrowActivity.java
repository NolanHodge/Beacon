package com.comp3004.beacon.GUI;

import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.comp3004.beacon.R;

public class ArrowActivity extends AppCompatActivity {
    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arrow);

        float[] results = {0};
        double[] location = {45.393614, -75.687776};
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        Location.distanceBetween(location[0], location[1], lastLocation.getLatitude(), lastLocation.getLongitude(), results);

        TextView distance  = (TextView) findViewById(R.id.txt_distance);
        distance.setText(String.format("%.2f m",results[0]));
    }
}
