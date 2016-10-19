package com.comp3004.beacon.GUI;

import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.comp3004.beacon.LocationManagement.LocationService;
import com.comp3004.beacon.R;
import com.comp3004.beacon.User.Beacon;
import com.comp3004.beacon.User.CurrentBeaconUser;

public class ArrowActivity extends AppCompatActivity {
    private float prev,destination = 0;
    static long MIN_TIME = 1000;
    static float MIN_DIST = 1;
    static String NORTH = "North";
    static String NORTHEAST = "Northeast";
    static String NORTHWEST = "Northwest";
    static String EAST = "East";
    static String SOUTHEAST = "Southeast";
    static String WEST = "West";
    static String SOUTHWEST = "Southwest";
    static String SOUTH = "South";

    static final String beaconKey = "CURRENT_BEACON_ID";
    private Beacon followingBeacon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arrow);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String beaconId = extras.getString(beaconKey);
            followingBeacon = CurrentBeaconUser.getInstance().getBeacons().get(beaconId);
        }


        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        LocationService locationService = new LocationService() {
            @Override
            public void onLocationChanged(Location location) {
                //when the location is obtained then rotate the arrow
                findViewById(R.id.arrow_prgrs).setVisibility(View.INVISIBLE);

                final float[] results = new float[3];
                Location.distanceBetween(location.getLatitude(), location.getLongitude(), Double.parseDouble(followingBeacon.getLat()), Double.parseDouble(followingBeacon.getLon()), results);

                final int compass_bearing = (int) (results[2] + 360) % 360;

                String b;
                if (compass_bearing >= 65 && compass_bearing < 115) {
                    b = EAST;
                } else if (compass_bearing >= 115 && compass_bearing < 155) {
                    b = SOUTHEAST;
                } else if (compass_bearing >= 155 && compass_bearing < 205) {
                    b = SOUTH;
                } else if (compass_bearing >= 205 && compass_bearing < 245) {
                    b = SOUTHWEST;
                } else if (compass_bearing >= 245 && compass_bearing < 295) {
                    b = WEST;
                } else if (compass_bearing >= 295 && compass_bearing < 335) {
                    b = NORTHWEST;
                } else if (compass_bearing >= 335 || compass_bearing < 25) {
                    b = NORTH;
                } else if (compass_bearing >= 25 && compass_bearing < 65) {
                    b = NORTHEAST;
                } else {
                    b = "NA";
                }
                String s = results[0] > 1100 ? String.format("%.1f km\n%s", results[0] / 1000, b) : String.format("%.1f m\n%s", results[0], b);

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
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DIST, locationService);
        } catch (SecurityException e) {
            e.printStackTrace();
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

            Location.distanceBetween(location.getLatitude(), location.getLongitude(),  Double.parseDouble(followingBeacon.getLat()), Double.parseDouble(followingBeacon.getLon()) , results);
            final int compass_bearing = (int) (results[2] + 360) % 360;

            String b;
            if (compass_bearing >= 65 && compass_bearing < 115) {
                b = "East";
            } else if (compass_bearing >= 115 && compass_bearing < 155) {
                b = "Southeast";
            } else if (compass_bearing >= 155 && compass_bearing < 205) {
                b = "South";
            } else if (compass_bearing >= 205 && compass_bearing < 245) {
                b = "Southwest";
            } else if (compass_bearing >= 245 && compass_bearing < 295) {
                b = "West";
            } else if (compass_bearing >= 295 && compass_bearing < 335) {
                b = "Northwest";
            } else if (compass_bearing >= 335 || compass_bearing < 25) {
                b = "North";
            } else if (compass_bearing >= 25 && compass_bearing < 65) {
                b = "Northeast";
            } else {
                b = "NA";
            }
            String s = results[0] > 1100 ? String.format("%.1f km\n%s", results[0] / 1000, b) : String.format("%.0f m\n%s", results[0], b);

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
