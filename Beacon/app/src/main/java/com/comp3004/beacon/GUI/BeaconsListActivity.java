package com.comp3004.beacon.GUI;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.comp3004.beacon.Networking.MessageSenderHandler;
import com.comp3004.beacon.R;
import com.comp3004.beacon.User.Beacon;
import com.comp3004.beacon.User.BeaconUser;
import com.comp3004.beacon.User.CurrentBeaconUser;

import java.util.ArrayList;

public class BeaconsListActivity extends AppCompatActivity {

    ArrayList<Beacon> beaconsList;
    ArrayList<String> beaconUsernames;
    ListView beaconsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.comp3004.beacon.R.layout.activity_beacons_list);

        beaconsList = new ArrayList<Beacon>();
        beaconUsernames = new ArrayList<String>();

        beaconsListView = (ListView) findViewById(R.id.beaconsListView);

        CurrentBeaconUser currentBeaconUser = CurrentBeaconUser.getInstance();

        if (currentBeaconUser.getBeacons() != null) {
            for (Object key : CurrentBeaconUser.getInstance().getBeacons().keySet()) {
                BeaconUser bc = (BeaconUser) CurrentBeaconUser.getInstance().getFriends().get(key);
                beaconUsernames.add(bc.getDisplayName());
                beaconsList.add(currentBeaconUser.getBeacons().get(key));
            }
        }
        populateBeaconsListView();
        registerBeaconsListviewCallback();
    }

    private void populateBeaconsListView() {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, beaconUsernames);
        beaconsListView.setAdapter(adapter);

    }

    private void registerBeaconsListviewCallback() {
        beaconsListView = (ListView) findViewById(R.id.beaconsListView);
        beaconsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Beacon selectedBeacon = beaconsList.get(position);
                showBeaconOptionDialog(selectedBeacon, position);
            }
        });
    }
    public void showBeaconOptionDialog(final Beacon beacon, final int index) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final Context context = this;
        builder
                .setItems(new String[]{"Track", "Delete", "Cancel"}, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Intent intent = new Intent(BeaconsListActivity.this, ArrowActivity.class);
                                intent.putExtra("CURRENT_BEACON_ID", beacon.getFromUserId());
                                startActivity(intent);
                                break;
                            case 1:
                                CurrentBeaconUser currentBeaconUser = CurrentBeaconUser.getInstance();
                                currentBeaconUser.getBeacons().remove(beacon.getFromUserId());
                                startActivity(getIntent());
                                break;
                            case 2:
                                break;
                        }
                    }
                });
        builder.create().show();
    }
}
