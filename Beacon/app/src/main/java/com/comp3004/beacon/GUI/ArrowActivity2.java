package com.comp3004.beacon.GUI;

import android.hardware.GeomagneticField;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationListener;
import java.lang.ref.WeakReference;
import android.os.Bundle;
import android.graphics.Canvas;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.util.DisplayMetrics;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.TextView;
import android.hardware.SensorManager;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorEvent;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;

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

        private float[] accelerometerValues = new float[3];
        private float[] geomagneticValues = new float[3];
        private float[] mR = new float[9];
        private float[] mOrientation = new float[3];
        private boolean accelerometerValuesSet = false;
        private boolean geomagneticValuesSet = false;

        static long MIN_TIME  = 1000;
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
                accelerometerValuesSet = true;
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                geomagneticValues  = event.values.clone();
                geomagneticValuesSet = true;
                break;
        }
/*
        SensorManager.getRotationMatrix(mR, null, accelerometerValues, geomagneticValues);
        SensorManager.getOrientation(mR, mOrientation);
        float azimuthInRadians = mOrientation[0];

        float azimuth = mOrientation[0];
        azimuth = (float)Math.toDegrees(azimuth);

        geoField = new GeomagneticField(
                Double.valueOf(location.getLatitude()).floatValue(),
                Double.valueOf(location.getLongitude()).floatValue(),
                Double.valueOf(location.getAltitude()).floatValue(),
                System.currentTimeMillis()
        );

        azimuth += geoField.getDeclination(); // converts magnetic north into true north
        float bearing = location.bearingTo(target); // (it's already in degrees)

        float degree = Math.round(event.values[0]);
        degree += geoField.getDeclination();

        float direction = azimuth + location.bearingTo( target );

        TextView targetTV = (TextView) findViewById(R.id.txt_bearing);
        targetTV.setText(" Target X: " + target.getLatitude() + " Target Y: " + target.getLongitude());

        TextView degreesTextView = (TextView) findViewById(R.id.txt_degrees);
        degreesTextView.setText("   " + Float.toString(degree) + "%    ");

        RotateAnimation ra = new RotateAnimation(
                mCurrentDegree,
                -direction,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);

        ra.setDuration(250);
        ra.setFillAfter(true);

        mPointer.startAnimation(ra);
        mCurrentDegree = -direction;
  */

        SensorManager.getRotationMatrix(mR, null, accelerometerValues, geomagneticValues);
        SensorManager.getOrientation(mR, mOrientation);

        float azimuth2 = mOrientation[0];
        azimuth2 = (float)Math.toDegrees(azimuth2);
        float baseAzimuth = azimuth2;

        GeomagneticField geoField2 = new GeomagneticField(
                Double.valueOf( location.getLatitude() ).floatValue(),
                Double.valueOf( location.getLongitude() ).floatValue(),
                Double.valueOf( location.getAltitude() ).floatValue(),
                System.currentTimeMillis() );

        azimuth2 -= geoField2.getDeclination(); // converts magnetic north into true north

        // Store the bearingTo in the bearTo variable
        float bearTo = location.bearingTo( target );
        // If the bearTo is smaller than 0, add 360 to get the rotation clockwise.
        if (bearTo < 0) {
            bearTo = bearTo + 360;
        }

        //This is where we choose to point it
        float direction2 = bearTo - azimuth2;

        // If the direction is smaller than 0, add 360 to get the rotation clockwise.
        if (direction2 < 0)
        {
            direction2 = direction2 + 360;
        }

        mPointer.setRotation(direction2);

        //Set the field
        String bearingText = "N";

        if ( (360 >= baseAzimuth && baseAzimuth >= 337.5) || (0 <= baseAzimuth && baseAzimuth <= 22.5) ) bearingText = "N";
        else if (baseAzimuth > 22.5 && baseAzimuth < 67.5) bearingText = "NE";
        else if (baseAzimuth >= 67.5 && baseAzimuth <= 112.5) bearingText = "E";
        else if (baseAzimuth > 112.5 && baseAzimuth < 157.5) bearingText = "SE";
        else if (baseAzimuth >= 157.5 && baseAzimuth <= 202.5) bearingText = "S";
        else if (baseAzimuth > 202.5 && baseAzimuth < 247.5) bearingText = "SW";
        else if (baseAzimuth >= 247.5 && baseAzimuth <= 292.5) bearingText = "W";
        else if (baseAzimuth > 292.5 && baseAzimuth < 337.5) bearingText = "NW";
        else bearingText = "?";

        TextView degreesTextView = (TextView) findViewById(R.id.txt_degrees);
        degreesTextView.setText(bearingText);

        TextView targetTV = (TextView) findViewById(R.id.txt_bearing);
        targetTV.setText(" Target X: " + target.getLatitude() + " Target Y: " + target.getLongitude());

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

    private void rotateImageView( ImageView imageView, int drawable, float rotate ) {
        // Decode the drawable into a bitmap
        Bitmap bitmapOrg = BitmapFactory.decodeResource( getResources(), drawable );

        // Get the width/height of the drawable
        DisplayMetrics dm = new DisplayMetrics(); getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = bitmapOrg.getWidth(), height = bitmapOrg.getHeight();

        // Initialize a new Matrix
        Matrix matrix = new Matrix();

        // Decide on how much to rotate
        rotate = rotate % 360;

        // Actually rotate the image
        matrix.postRotate( rotate, width, height );

        // recreate the new Bitmap via a couple conditions
        Bitmap rotatedBitmap = Bitmap.createBitmap( bitmapOrg, 0, 0, width, height, matrix, true );
        BitmapDrawable bmd = new BitmapDrawable( rotatedBitmap );

        imageView.setImageBitmap( rotatedBitmap );
        imageView.setImageDrawable(new BitmapDrawable(getResources(), rotatedBitmap));
        imageView.setScaleType( ScaleType.CENTER );
    }
}
