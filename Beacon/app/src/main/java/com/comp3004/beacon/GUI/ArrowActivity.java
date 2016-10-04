package com.comp3004.beacon.GUI;

import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.comp3004.beacon.LocationManagement.LocationService;
import com.comp3004.beacon.R;

public class ArrowActivity extends AppCompatActivity {
    int prev = 0;
    static long MIN_TIME = 10000;
    static float MIN_DIST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arrow);


        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        new test().execute();
        LocationService locationService = new LocationService() {
            @Override
            public void onLocationChanged(Location location) {
                findViewById(R.id.arrow_prgrs).setVisibility(View.INVISIBLE);
                System.out.println("Update Location");

                final float[] results = new float[3];

                double rand1 = (Math.random() / 10 - .05);
                double rand2 = (Math.random() / 10 - .05);

                Location.distanceBetween(location.getLatitude(), location.getLongitude(), location.getLatitude() + rand1, location.getLongitude() + rand2, results);
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
                String s = results[0] > 1100 ? String.format("%.1f km\n%s", results[0] / 1000, b) : String.format("%.1f m\n%s", results[0], b);

                TextView textView = (TextView) findViewById(R.id.txt_distance);
                textView.setText(s);

                final ImageView imageView = (ImageView) findViewById(R.id.iv_arrow);


                final RotateAnimation anim = new RotateAnimation(0, results[1], Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                anim.setInterpolator(new LinearInterpolator());
                anim.setDuration(1400);
                anim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        imageView.setAnimation(null);
                        imageView.setRotation(results[1]);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                imageView.startAnimation(anim);
            }
        };
        try {
            //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DIST, locationService);
        } catch (SecurityException e) {
            e.printStackTrace();
        }


    }

    private class test extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            findViewById(R.id.arrow_prgrs).setVisibility(View.INVISIBLE);
            System.out.println("Update Location");

            final float[] results = new float[3];
            Location location = new Location("");
            location.setLatitude(45);
            location.setLongitude(-75);
            double rand1 = (Math.random() / 10 - .05);
            double rand2 = (Math.random() / 10 - .05);

            Location.distanceBetween(location.getLatitude(), location.getLongitude(), location.getLatitude() + rand1, location.getLongitude() + rand2, results);
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
            String s = results[0] > 1100 ? String.format("%.1f km\n%s", results[0] / 1000, b) : String.format("%.1f m\n%s", results[0], b);

            TextView textView = (TextView) findViewById(R.id.txt_distance);
            textView.setText(s);

            final ImageView imageView = (ImageView) findViewById(R.id.iv_arrow);
            System.out.println(imageView.getRotation());
            RotateAnimation anim = new RotateAnimation(prev, compass_bearing, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

            anim.setInterpolator(new LinearInterpolator());
            anim.setDuration(1000);
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    prev = compass_bearing;
                    imageView.setAnimation(null);
                    imageView.setRotation(compass_bearing);
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
