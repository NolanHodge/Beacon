package com.comp3004.beacon.GUI;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.comp3004.beacon.R;


// THIS IS THE PAGE THAT FIRES UP WHEN YOU CLICK ON THE BEACON ICON FROM MAINACTIVITY
// THIS IS NOT HE MAIN PAGE

public class BeaconHomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon_home_page);
    }
}
