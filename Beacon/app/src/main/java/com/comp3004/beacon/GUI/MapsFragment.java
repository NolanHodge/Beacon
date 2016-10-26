package com.comp3004.beacon.GUI;

import android.Manifest;
import android.support.v4.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.comp3004.beacon.FirebaseServices.DatabaseManager;
import com.comp3004.beacon.Networking.CurrentBeaconInvitationHandler;
import com.comp3004.beacon.Networking.MessageSenderHandler;
import com.comp3004.beacon.Networking.SubscriptionHandler;

import com.comp3004.beacon.R;
import com.comp3004.beacon.User.PrivateBeacon;
import com.comp3004.beacon.User.CurrentBeaconUser;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

public class MapsFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;

    public static final String LOCATION_MESSAGE_CHILD = "locations";

    // Firebase instance variables


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager()
                .findFragmentById(R.id.map);
        //mapFragment.getMapAsync(this);


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

        CurrentBeaconUser currentBeaconUser = CurrentBeaconUser.getInstance();
        //Requesting permission

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        //LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        //final Location current = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) == null ?
        //        locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) :
        //        locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        mMap = googleMap;
        mMap.setMyLocationEnabled(true);

        //CameraPosition cameraPosition = new CameraPosition.Builder()
        //        .target(new LatLng(current.getLatitude(), current.getLongitude()))
        //        .zoom(13)
        //        .build();
        //mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        //Add PrivateBeacon markers to the map
        for (PrivateBeacon privateBeacon : currentBeaconUser.getBeacons().values()) {
            LatLng position = new LatLng(Double.parseDouble(privateBeacon.getLat()), Double.parseDouble(privateBeacon.getLon()));
            String userId = privateBeacon.getFromUserId();

            mMap.addMarker(new MarkerOptions()
                    .title("Beacon")
                    .snippet(currentBeaconUser.getFriend(userId).getDisplayName())
                    .position(position))
                    .setDraggable(true);
        }

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(final LatLng latLng) {
                mMap.addMarker(new MarkerOptions().position(latLng));
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                    @Override
                    public void onCameraIdle() {
                        AlertDialog dialog = new AlertDialog.Builder(getActivity())
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
                                        mMap.clear();
                                        dialog.cancel(); //could've left this empty
                                    }
                                })
                                .show();

                        mMap.setOnCameraIdleListener(null);
                    }
                });

            }
        });
    }
}
