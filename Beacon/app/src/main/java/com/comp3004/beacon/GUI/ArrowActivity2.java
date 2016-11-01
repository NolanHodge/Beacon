package com.comp3004.beacon.GUI;

import android.hardware.GeomagneticField;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.hardware.SensorManager;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorEvent;
import android.widget.Toast;

import com.comp3004.beacon.LocationManagement.LocationService;
import com.comp3004.beacon.R;
import com.comp3004.beacon.User.PrivateBeacon;
import com.comp3004.beacon.User.CurrentBeaconUser;


public class ArrowActivity2 extends AppCompatActivity implements SensorEventListener{

        private ImageView mPointer;
        private SensorManager mSensorManager;
        private Sensor mAccelerometer;
        private Sensor mMagnetometer;

        private float mCurrentDegree = 0f;
        private LocationManager locationManager;
        public static String CURRENT_BEACON_ID_KEY = "CURRENT_BEACON_ID";
        private PrivateBeacon followingBeacon;

        private GeomagneticField geoField;

        private Location location = new Location("A");
        private Location target = new Location("B");

        private float[] accelerometerValues;
        private float[] geomagneticValues;

        static long MIN_TIME = 1000;
        static float MIN_DIST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arrow2);
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            String beaconId = extras.getString(CURRENT_BEACON_ID_KEY);
            followingBeacon = CurrentBeaconUser.getInstance().getBeacons().get(beaconId);
        }

        //** Sensor Manager tings
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mPointer = (ImageView) findViewById(R.id.iv_arrow);

        //** Location Manager tings
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        LocationService locationService = new LocationService() {
         @Override
         public void onLocationChanged(Location location) {

            final float[] results = new float[3];
            Location.distanceBetween(location.getLatitude(), location.getLongitude(), Double.parseDouble(followingBeacon.getLat()), Double.parseDouble(followingBeacon.getLon()), results);
             target.setLatitude(Double.parseDouble(followingBeacon.getLat()));
             target.setLongitude(Double.parseDouble(followingBeacon.getLon()));

             location.setLatitude(45);
             location.setLongitude(-75);

            final int compass_bearing = (int) (results[2] + 360) % 360;

            String s = results[0] > 1100 ? String.format("%.1f km", results[0] / 1000) : String.format("%.1f m", results[0]);

            TextView textView = (TextView) findViewById(R.id.txt_distance);
            textView.setText(s);

         }
        };

        try {
         locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DIST, locationService);
         } catch (SecurityException e) {
          e.printStackTrace();
          Toast toast = Toast.makeText(getApplicationContext(), e.getMessage().length(), Toast.LENGTH_SHORT);
          toast.show();
      }
    }

    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_GAME);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this, mAccelerometer);
        mSensorManager.unregisterListener(this, mMagnetometer);
    }
    
    @Override
    public void onSensorChanged(SensorEvent event) {

        switch(event.sensor.getType()){
            case Sensor.TYPE_ACCELEROMETER:
                accelerometerValues = event.values.clone();
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                geomagneticValues  = event.values.clone();
                break;
        }

        geoField = new GeomagneticField(
                Double.valueOf(location.getLatitude()).floatValue(),
                Double.valueOf(location.getLongitude()).floatValue(),
                Double.valueOf(location.getAltitude()).floatValue(),
                System.currentTimeMillis()
        );

        // get the angle around the z-axis rotated
        float degree = Math.round(event.values[0]);
        degree += geoField.getDeclination();

        float bearing = location.bearingTo(target);
        degree = (bearing - degree) * -1;
        degree = normalizeDegree(degree);

        TextView degrees = (TextView) findViewById(R.id.txt_degrees);
        degrees.setText("   " + Float.toString(degree) + "%    ");

        RotateAnimation ra = new RotateAnimation(
                mCurrentDegree,
                -degree,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);

        ra.setDuration(700);
        ra.setFillAfter(true);

        mPointer.startAnimation(ra);
        mCurrentDegree = -degree;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub
    }

    private float normalizeDegree(float value)
    {
        if (value >= 0.0f && value <= 180.0f)
        {
            return value;
        }
        else
        {
            return 180 + (180 + value);
        }
    }
}
