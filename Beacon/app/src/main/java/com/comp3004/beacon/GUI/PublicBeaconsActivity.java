package com.comp3004.beacon.GUI;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.comp3004.beacon.FirebaseServices.DatabaseManager;
import com.comp3004.beacon.Networking.MessageSenderHandler;
import com.comp3004.beacon.Networking.PublicBeaconHandler;
import com.comp3004.beacon.R;
import com.comp3004.beacon.User.PublicBeacon;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class PublicBeaconsActivity extends AppCompatActivity {

    ListView publicBeaconsListView;
    ArrayList<PublicBeacon> beaconsList;
    ArrayList beaconUsernames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.comp3004.beacon.R.layout.activity_public_beacons);

        beaconsList = new ArrayList<PublicBeacon>();
        beaconUsernames = new ArrayList<String>();

        DatabaseManager.getInstance().loadPublicBeacons();
        publicBeaconsListView = (ListView) findViewById(R.id.publicBeaconListView);



        if (PublicBeaconHandler.getInstance().getBeacons() != null) {
            for (Object key : PublicBeaconHandler.getInstance().getBeacons().keySet()) {
                PublicBeacon beacon = PublicBeaconHandler.getInstance().getBeacon(String.valueOf(key));
                beaconsList.add(beacon);
                beaconUsernames.add(beacon.getDisplayName());
            }
        }


        populateBeaconsListView();
        registerFriendsListviewCallback();

    }

    private void populateBeaconsListView() {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, beaconUsernames);
        publicBeaconsListView.setAdapter(adapter);

    }

    private void registerFriendsListviewCallback() {
        publicBeaconsListView = (ListView) findViewById(R.id.publicBeaconListView);
        final Context context = this;

        publicBeaconsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog alertDialog;
                PublicBeacon selectedBeacon = beaconsList.get(position);
                showBeaconOptionDialog(selectedBeacon, position);
            }
        });
    }

    public void showBeaconOptionDialog(PublicBeacon publicBeacon, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final Context context = this;
        builder.setTitle("Follow " + publicBeacon.getDisplayName() + "?")
                .setItems(new String[]{"Yes", "No"}, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                MessageSenderHandler.getInstance().followBeacon(beaconsList.get(position));
                                break;
                            case 1:
                                break;
                        }
                    }
                });
        builder.create().show();
    }


}
