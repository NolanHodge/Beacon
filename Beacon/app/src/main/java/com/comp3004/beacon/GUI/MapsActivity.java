package com.comp3004.beacon.GUI;

import android.Manifest;

import android.content.Context;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import android.location.Location;
import android.location.LocationManager;

import android.net.Uri;

import android.preference.PreferenceManager;

import android.provider.MediaStore;

import android.support.annotation.NonNull;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;

import android.os.Bundle;

import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;

import android.support.v7.widget.SearchView;
import android.util.Log;

import android.view.Menu;
import android.view.View;

import android.widget.Toast;

import com.comp3004.beacon.FirebaseServices.DatabaseManager;
import com.comp3004.beacon.Networking.CurrentBeaconInvitationHandler;
import com.comp3004.beacon.Networking.CurrentLocationRequestHandler;
import com.comp3004.beacon.Networking.MessageSenderHandler;
import com.comp3004.beacon.Networking.SubscriptionHandler;

import com.comp3004.beacon.R;
import com.comp3004.beacon.User.Beacon;
import com.comp3004.beacon.User.BeaconUser;
import com.comp3004.beacon.User.PrivateBeacon;
import com.comp3004.beacon.User.CurrentBeaconUser;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;

import java.io.File;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, OnMarkerClickListener {

    private GoogleMap mMap;

    public static final String ANONYMOUS = "anonymous";
    public static final String LOCATION_MESSAGE_CHILD = "locations";

    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mFirebaseDatabaseReference;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private GoogleApiClient mGoogleApiClient;
    private CurrentBeaconUser currentBeaconUser;
    private MessageSenderHandler messageHandler;
    private SharedPreferences mSharedPreferences;
    private String mUsername;
    private String mPhotoUrl;
    SubscriptionHandler subscriptionHandler;
    static final private int CAM_REQUEST = 1;
    private String fromMapLat, fromMapLon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Set default username is anonymous.
        mUsername = ANONYMOUS;
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        currentBeaconUser = CurrentBeaconUser.getInstance();
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        messageHandler = MessageSenderHandler.getInstance();
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();

        subscriptionHandler = SubscriptionHandler.getInstance();


        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        } else {
            mUsername = mFirebaseUser.getDisplayName();
            if (mFirebaseUser.getPhotoUrl() != null) {
                Toast.makeText(MapsActivity.this, getString(R.string.login_greeting) + " " + currentBeaconUser.getDisplayName(),
                        Toast.LENGTH_SHORT).show();
            }
        }


        MessageSenderHandler.getInstance().sendRegisterUserMessage();
        DatabaseManager.getInstance().loadCurrentUser();


        if (CurrentBeaconInvitationHandler.getInstance().currentInvitationExists()) {
            CurrentBeaconInvitationHandler.getInstance().setCurrentInvitationExists(false);
            openBeaconInvitationDialog();

        }
        if (CurrentLocationRequestHandler.getInstance().isCurrentLocationRequestExists()) {
            CurrentLocationRequestHandler.getInstance().setCurrentLocationRequestExists(false);
            openLocationRequestDialog();

        }

        for (PrivateBeacon privateBeacon : currentBeaconUser.getBeacons().values()) {

            DatabaseManager.getInstance().loadPhotos(privateBeacon.getFromUserId());
        }

        // Define Firebase Remote Config Settings.
        FirebaseRemoteConfigSettings firebaseRemoteConfigSettings =
                new FirebaseRemoteConfigSettings.Builder()
                        .setDeveloperModeEnabled(true)
                        .build();

        //GUI
        FloatingActionButton messageButton = (FloatingActionButton) findViewById(R.id.message_button);
        FloatingActionButton beaconsButton = (FloatingActionButton) findViewById(R.id.beacons_button);
        FloatingActionButton publicBeaconsButton = (FloatingActionButton) findViewById(R.id.public_beacons_button);
        FloatingActionButton cameraButon = (FloatingActionButton) findViewById(R.id.photo_activity_button);

        messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MapsActivity.this, FriendListActivity.class));
            }
        });

        beaconsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MapsActivity.this, BeaconsListActivity.class));
            }
        });

        publicBeaconsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MapsActivity.this, PublicBeaconsActivity.class));
            }
        });

        cameraButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(MapsActivity.this, TakePhotoActivity.class));
                Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File file = getFile();
                camera_intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                startActivityForResult(camera_intent, CAM_REQUEST);

            }
        });
    }

    public void fetchConfig() {
        long cacheExpiration = 3600; // 1 hour in seconds
        if (mFirebaseRemoteConfig.getInfo().getConfigSettings()
                .isDeveloperModeEnabled()) {
            cacheExpiration = 0;
        }
        mFirebaseRemoteConfig.fetch(cacheExpiration)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Make the fetched config available via
                        // FirebaseRemoteConfig get<type> calls.
                        mFirebaseRemoteConfig.activateFetched();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // There has been an error fetching the config
                        Log.w("", "Error fetching config: " +
                                e.getMessage());
                    }
                });

    }
    public void openLocationRequestDialog() {
        final Context context = this;
        CurrentBeaconUser currentBeaconUser = CurrentBeaconUser.getInstance();
        final CurrentLocationRequestHandler currentLocationRequestHandler = CurrentLocationRequestHandler.getInstance();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String message = CurrentBeaconUser.getInstance().getFriend(currentLocationRequestHandler.getLocationRequestMessage().getFromUserId()).getDisplayName() + " wants your location!";
        builder.setMessage(message);
        builder.setPositiveButton("Accept, send Beacon!", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {


                LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                Location current = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) == null ?
                        locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) :
                        locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                MessageSenderHandler.getInstance().sendBeaconRequest(currentLocationRequestHandler.getLocationRequestMessage().getFromUserId(), current);

            }
        });
        builder.setNegativeButton("Decline", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void openBeaconInvitationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyDialogTheme);
        final CurrentBeaconInvitationHandler currentBeaconInvitationHandler = CurrentBeaconInvitationHandler.getInstance();
        builder.setMessage(currentBeaconInvitationHandler.getMessage());
        builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                PrivateBeacon privateBeacon = new PrivateBeacon(currentBeaconInvitationHandler);
                CurrentBeaconUser.getInstance().addBeacon(privateBeacon);

                Intent intent = new Intent(MapsActivity.this, ArrowActivity.class);
                intent.putExtra("CURRENT_BEACON_ID", privateBeacon.getFromUserId());
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Decline", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                CurrentBeaconInvitationHandler.getInstance().setCurrentInvitationExists(false);
                DatabaseManager.getInstance().removeBeaconFromDb(CurrentBeaconInvitationHandler.getInstance().getBeaconId());

            }
        });

        AlertDialog dialog = builder.create();
        dialog.getButton(dialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(android.R.color.holo_red_light));
        dialog.getButton(dialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(android.R.color.holo_blue_light));
        dialog.show();
        CurrentBeaconInvitationHandler.getInstance().setCurrentInvitationExists(false);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        DatabaseManager.getInstance().loadCurrentUser();
        googleMap.setOnMarkerClickListener(this);
        CurrentBeaconUser currentBeaconUser = CurrentBeaconUser.getInstance();
        //Requesting permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        final Location current = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) == null ?
                locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) :
                locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        mMap = googleMap;

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(current.getLatitude(), current.getLongitude()))
                .zoom(13)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        // Uses a custom icon.

        // Turn this back on for the "little blue button"
        //mMap.setMyLocationEnabled(true);
        // comment out addMarker function to remove the little tower!
        mMap.addMarker(new MarkerOptions()
                .title("Your Beacon")
                .position(new LatLng(current.getLatitude(), current.getLongitude()))
                .snippet(currentBeaconUser.getDisplayName())
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.tower_icon_small)));

            //Add PrivateBeacon markers to the map
            for (PrivateBeacon privateBeacon : currentBeaconUser.getBeacons().values()) {
                if (privateBeacon == null) break;
                LatLng position = new LatLng(Double.parseDouble(privateBeacon.getLat()), Double.parseDouble(privateBeacon.getLon()));
                String userId = privateBeacon.getFromUserId();

                mMap.addMarker(new MarkerOptions()
                        .title("Private Beacon")
                        .position(position)
                        .snippet(currentBeaconUser.getFriend(userId).getDisplayName())
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.tower_icon_small)));
        }
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(final LatLng latLng) {

                CurrentBeaconUser currentBeaconUser = CurrentBeaconUser.getInstance();

                final Marker holdMarker = mMap.addMarker(new MarkerOptions()
                        .title("Your Public Beacon")
                        .position(latLng)
                        .snippet(currentBeaconUser.getDisplayName())
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.tower_icon_small)));

                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                    @Override
                    public void onCameraIdle() {
                        AlertDialog dialog = new AlertDialog.Builder(MapsActivity.this, R.style.MyDialogTheme)
                                .setTitle("Create a Public Beacon")
                                .setMessage("Would you like to create a beacon here?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        MessageSenderHandler.getInstance().sendPublicBeacon(latLng);
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //mMap.clear();
                                        holdMarker.remove();
                                        dialog.cancel(); //could've left this empty
                                    }
                                })
                                .show();

                        dialog.getButton(dialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(android.R.color.holo_red_light));
                        dialog.getButton(dialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(android.R.color.holo_blue_light));

                        mMap.setOnCameraIdleListener(null);
                    }
                });

            }
        });
    }

    @Override
    public boolean onMarkerClick (final Marker marker)
    {
        if (marker.getTitle().equals("Your Beacon"))
        {
            AlertDialog dialog = new AlertDialog.Builder(MapsActivity.this, R.style.MyDialogTheme)
                    .setMessage("Your friends will track you to this location.")
                    .setTitle(marker.getTitle())
                    .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //mMap.clear();
                            dialog.cancel(); //could've left this empty
                        }
                    }).show();

            dialog.getButton(dialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(android.R.color.holo_blue_light));
            return true;
        }
        AlertDialog dialog = new AlertDialog.Builder(MapsActivity.this, R.style.MyDialogTheme)
                .setMessage(marker.getSnippet())
                .setTitle(marker.getTitle())
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel(); //could've left this empty
                    }
                })
                .setPositiveButton("Track", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LatLng latlng = marker.getPosition();
                        String lat = "" + latlng.latitude;
                        String lon = "" + latlng.longitude;
                        Intent intent2 = new Intent(MapsActivity.this, ArrowActivity2.class);
                        intent2.putExtra(ArrowActivity2.FROM_MAP_TRACK_LAT, lat);
                        intent2.putExtra(ArrowActivity2.FROM_MAP_TRACK_LON, lon);
                        startActivity(intent2);
                    }
                })
                .show();
        dialog.getButton(dialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(android.R.color.holo_red_light));
        dialog.getButton(dialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(android.R.color.holo_blue_light));
        return true;
    }

    private File getFile() {
        File folder = new File("sdcard/camera_app");

        if (!folder.exists()) {
            folder.mkdir();
        }
        File imageFile = new File(folder, "camera_img.jpg");
        return imageFile;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        String path = "sdcard/camera_app/camera_img.jpg";
        //imageView.setImageDrawable(Drawable.createFromPath(path));
        //upload image
        File file = new File(path);
        MessageSenderHandler.getInstance().sendPhotoMessage(file);
    }
}
