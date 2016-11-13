package com.comp3004.beacon.GUI;

import android.Manifest;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.hardware.GeomagneticField;

import android.location.Location;
import android.location.LocationManager;

import android.os.Bundle;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;

import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import android.util.DisplayMetrics;

import android.support.v4.app.FragmentActivity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import android.hardware.SensorManager;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorEvent;

import android.widget.Toast;
import android.widget.ImageView.ScaleType;


import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.MapStyleOptions;

import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import android.view.animation.RotateAnimation;
import android.view.animation.Animation;

import com.comp3004.beacon.FirebaseServices.DatabaseManager;
import com.comp3004.beacon.LocationManagement.LocationService;
import com.comp3004.beacon.R;
import com.comp3004.beacon.User.PrivateBeacon;
import com.comp3004.beacon.User.CurrentBeaconUser;

public class ArrowActivity2 extends FragmentActivity implements SensorEventListener, OnMapReadyCallback{

        private GoogleMap mMap;
        private Handler mHandler;

        private ImageView mPointer;
        private SensorManager mSensorManager;
        private Sensor mAccelerometer;
        private Sensor mMagnetometer;
        private LocationManager locationManager;
        private FloatingActionButton imageViewButton;
        private float distance;
        private float mCurrentDegree = 0f;
        private Location location;

        public static String CURRENT_BEACON_ID_KEY = "CURRENT_BEACON_ID";
        public static String FROM_MAP_TRACK_LAT = "FROM_MAP_TRACK_LAT";
        public static String FROM_MAP_TRACK_LON = "FROM_MAP_TRACK_LON";

        private PrivateBeacon followingBeacon;
        private GeomagneticField geoField;
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
        findViewById(R.id.arrow_prgrs).setVisibility(View.VISIBLE);
        imageViewButton = (FloatingActionButton) findViewById(R.id.view_image_button2);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map2);
        mapFragment.getMapAsync(this);
        mHandler = new Handler();

        if (extras != null) {
            if (extras.containsKey(CURRENT_BEACON_ID_KEY)) {
                String beaconId = extras.getString(CURRENT_BEACON_ID_KEY);
                followingBeacon = CurrentBeaconUser.getInstance().getBeacons().get(beaconId);
            }
            else {
                String fromMapLat = extras.getString(FROM_MAP_TRACK_LAT);
                String fromMapLon = extras.getString(FROM_MAP_TRACK_LON);
                followingBeacon = new PrivateBeacon(fromMapLat, fromMapLon);
            }
        }

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                DatabaseManager.getInstance().loadPhotos(followingBeacon.getFromUserId());
            }
        });
        t.start();

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
             distance = results[0];
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
        imageViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //DatabaseManager.getInstance().loadPhotos(followingBeacon.getFromUserId());
                Intent intent = new Intent(ArrowActivity2.this, ImageViewActivity.class);
                intent.putExtra(ImageViewActivity.IMAGE_USER_ID, followingBeacon.getFromUserId());
                startActivity(intent);

                startActivity(intent);
            }
        });
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        System.out.println("MAP READY");
        mMap = googleMap;
        DatabaseManager.getInstance().loadCurrentUser();
        final CurrentBeaconUser currentBeaconUser = CurrentBeaconUser.getInstance();
        //Requesting permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.beacon_map_style));


        } catch (Exception e) {
        }

        final LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        LocationService locationService = new LocationService() {
            @Override
            public void onLocationChanged(Location location) {
                try {
                    locationManager.removeUpdates(this);
                } catch (SecurityException e) {
                }
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(location.getLatitude(), location.getLongitude()))
                        .zoom(getZoomDistance((int)distance))
                        .build();
                mMap.animateCamera(
                        CameraUpdateFactory.newCameraPosition(cameraPosition),
                        500,
                        null);
                // comment out addMarker function to remove the little tower!
                mMap.addMarker(new MarkerOptions()
                        .title("Your Beacon")
                        .position(new LatLng(location.getLatitude(), location.getLongitude()))
                        .snippet(currentBeaconUser.getDisplayName())
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.tower_icon_small)));
            }
        };
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 10, locationService);

        placeOtherBeacon();
    }

    public void placeOtherBeacon()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                final LatLng position = new LatLng(Double.parseDouble(followingBeacon.getLat()), Double.parseDouble(followingBeacon.getLon()));

                                mMap.addMarker(new MarkerOptions()
                                        .title("")
                                        .position(position)
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.tower_icon_small)));
                            }});
                    }});
            }
         }).start();
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

        final Context context = this;

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
         location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) == null ?
                locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) :
                locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

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


        // Rotate Compass
        float azimuthInRadians = mOrientation[0];
        float azimuthInDegress = (float)(Math.toDegrees(azimuthInRadians)+360)%360;
        RotateAnimation ra = new RotateAnimation(
                mCurrentDegree,
                -azimuthInDegress,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);

        RotateAnimation ra2 = new RotateAnimation(
                mCurrentDegree,
                -direction2,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);

        ra.setDuration(250);
        ra.setFillAfter(true);
        ra2.setDuration(250);
        ra2.setFillAfter(true);

        mPointer.setRotation(direction2 % 360);

        findViewById(R.id.compass_corner).startAnimation(ra);
        mCurrentDegree = -azimuthInDegress;

        if (mMap != null)
            updateCamera(direction2);

        findViewById(R.id.arrow_prgrs).setVisibility(View.INVISIBLE);
    }

    public void updateCamera(float bearing) {
        CameraPosition currentPlace = new CameraPosition.Builder()
                .target(new LatLng(location.getLatitude(), location.getLongitude()))
                .bearing(bearing).tilt(65.5f).zoom(getZoomDistance((int)distance)).build();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(currentPlace));

    }

    /*
      Zoom distances on Google Maps
        20 : 1128.497220
        19 : 2256.994440
        18 : 4513.988880
        17 : 9027.977761
        16 : 18055.955520
        15 : 36111.911040
        14 : 72223.822090
        13 : 144447.644200
        12 : 288895.288400
        11 : 577790.576700
        10 : 1155581.153000
        9  : 2311162.307000
        8  : 4622324.614000
        7  : 9244649.227000
        6  : 18489298.450000
        5  : 36978596.910000
        4  : 73957193.820000
        3  : 147914387.600000
        2  : 295828775.300000
        1  : 591657550.500000
    */
    public float getZoomDistance(int dist)
    {
        if (dist < 100)
            return 19;
        if (dist < 200)
            return 18;
        if (dist < 400)
            return 17;
        if (dist < 600)
            return 16;
        if (dist < 800)
            return 15;
        if (dist < 900)
            return 14;
        if (dist < 1000)
            return 13;

        return 12;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub
    }
}
