package com.comp3004.beacon.GUI;

import android.content.Intent;

import android.location.Location;
import android.location.LocationManager;

import android.os.AsyncTask;
import android.os.Bundle;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.comp3004.beacon.FirebaseServices.DatabaseManager;
import com.comp3004.beacon.LocationManagement.LocationService;
import com.comp3004.beacon.R;
import com.comp3004.beacon.User.PrivateBeacon;
import com.comp3004.beacon.User.CurrentBeaconUser;

import java.util.ArrayList;

public class ArrowActivity extends AppCompatActivity {
    private float prev, destination = 0;
    static long MIN_TIME = 5000;
    static float MIN_DIST = 1;
    public static String CURRENT_BEACON_ID_KEY = "CURRENT_BEACON_ID";
    private PrivateBeacon followingBeacon;

    private FloatingActionButton imageViewButton;
    private Location lastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arrow);

        imageViewButton = (FloatingActionButton) findViewById(R.id.view_image_button);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String beaconId = extras.getString(CURRENT_BEACON_ID_KEY);
            followingBeacon = CurrentBeaconUser.getInstance().getBeacons().get(beaconId);
        }

        //Load image associated with this beacon in background
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                DatabaseManager.getInstance().loadPhotos(followingBeacon.getFromUserId());
            }
        });
        t.start();


        final LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);


        imageViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //DatabaseManager.getInstance().loadPhotos(followingBeacon.getFromUserId());
                Intent intent = new Intent(ArrowActivity.this, ImageViewActivity.class);
                intent.putExtra(ImageViewActivity.IMAGE_USER_ID, followingBeacon.getFromUserId());
                startActivity(intent);

                startActivity(intent);
            }
        });
        final LocationService locationService = new LocationService() {
            @Override
            public void onLocationChanged(Location location) {
                //when the location is obtained then rotate the arrow
                findViewById(R.id.arrow_prgrs).setVisibility(View.INVISIBLE);

                final float[] results = new float[3];

                Location.distanceBetween(location.getLatitude(), location.getLongitude(), Double.parseDouble(followingBeacon.getLat()), Double.parseDouble(followingBeacon.getLon()), results);
                String s = results[0] > 1100 ? String.format("%.1f km", results[0] / 1000) : String.format("%.1f m", results[0]);

                TextView textView = (TextView) findViewById(R.id.txt_distance);
                textView.setText(s);

                final ImageView imageView = (ImageView) findViewById(R.id.iv_arrow);


                if (Math.abs(prev - results[1]) <= 180) {
                    destination = results[1];
                } else {
                    if (prev < 0) {
                        destination = (results[1] - 360) % 360;
                    } else {
                        destination = (results[1] + 360) % 360;
                    }
                    if (Math.abs(destination - prev) > 180)
                        System.out.println(Math.abs(destination - prev) + " " + destination + " " + prev);

                }

                RotateAnimation anim = new RotateAnimation(prev, destination, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                anim.setInterpolator(new LinearInterpolator());
                anim.setDuration(700);
                anim.setFillAfter(true);
                anim.setFillEnabled(true);
                anim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        if (destination > 180) prev = destination - 360;
                        if (destination < -180) prev = destination + 360;
                        else prev = destination;
                        System.out.println(prev);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                imageView.startAnimation(anim);
            }
        };

        try {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, 0, locationService);
        } catch (SecurityException e) {
            e.printStackTrace();
            Toast toast = Toast.makeText(getApplicationContext(), e.getMessage().length(), Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private class test extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            findViewById(R.id.arrow_prgrs).setVisibility(View.INVISIBLE);

            final float[] results = new float[3];
            Location location = new Location("");
            location.setLatitude(45);
            location.setLongitude(-75);
            double rand1 = (Math.random() / 10 - .05);
            double rand2 = (Math.random() / 10 - .05);

            Location.distanceBetween(location.getLatitude(), location.getLongitude(), Double.parseDouble(followingBeacon.getLat()), Double.parseDouble(followingBeacon.getLon()), results);

            final ImageView imageView = (ImageView) findViewById(R.id.iv_arrow);

            if (Math.abs(prev - results[1]) <= 180) {
                destination = results[1];
            } else {
                if (prev < 0) {
                    destination = (results[1] - 360) % 360;
                } else {
                    destination = (results[1] + 360) % 360;
                }
                if (Math.abs(destination - prev) > 180)
                    System.out.println(Math.abs(destination - prev) + " " + destination + " " + prev);

            }

            RotateAnimation anim = new RotateAnimation(prev, destination, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            anim.setInterpolator(new LinearInterpolator());
            anim.setDuration(1000);
            anim.setFillAfter(true);
            anim.setFillEnabled(true);
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (destination > 180) prev = destination - 360;
                    if (destination < -180) prev = destination + 360;
                    else prev = destination;
                    System.out.println(prev);
                    new test().execute();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            imageView.startAnimation(anim);
        }
    }
}
