package com.comp3004.beacon.GUI;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.comp3004.beacon.FirebaseServices.DatabaseManager;
import com.comp3004.beacon.R;
import com.comp3004.beacon.User.PrivateBeacon;
import com.comp3004.beacon.User.BeaconUser;
import com.comp3004.beacon.User.CurrentBeaconUser;

import java.util.ArrayList;

public class BeaconsListActivity extends AppCompatActivity {

    ArrayList<PrivateBeacon> beaconsList;
    ArrayList<String> beaconUsernames;
    ListView beaconsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.comp3004.beacon.R.layout.activity_beacons_list);

        beaconsList = new ArrayList<PrivateBeacon>();
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

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, beaconUsernames){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                TextView text1 = (TextView) view.findViewById(android.R.id.text1);

                if((position % 2) == 1)
                {
                    view.setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimary));
                    text1.setTextColor(getContext().getResources().getColor(android.R.color.white));
                }
                else{
                    view.setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimaryDark));
                    text1.setTextColor(getContext().getResources().getColor(android.R.color.white));
                }

                return view;
            }
        };
        beaconsListView.setAdapter(adapter);

    }

    private void registerBeaconsListviewCallback() {
        beaconsListView = (ListView) findViewById(R.id.beaconsListView);
        beaconsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PrivateBeacon selectedPrivateBeacon = beaconsList.get(position);
                showBeaconOptionDialog(selectedPrivateBeacon, position);
            }
        });
    }
    public void showBeaconOptionDialog(final PrivateBeacon privateBeacon, final int index) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyDialogTheme);
        builder
                .setItems(new String[]{"Track", "Delete", "Cancel"}, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Intent intent = new Intent(BeaconsListActivity.this, ArrowActivity2.class);
                                intent.putExtra(ArrowActivity2.CURRENT_BEACON_ID_KEY, privateBeacon.getFromUserId());
                                startActivity(intent);
                                break;
                            case 1:
                                CurrentBeaconUser currentBeaconUser = CurrentBeaconUser.getInstance();
                                currentBeaconUser.getBeacons().remove(privateBeacon.getFromUserId());
                                DatabaseManager.getInstance().removeBeaconFromDb(privateBeacon.getBeaconId());
                                finish();
                                startActivity(getIntent());
                                beaconUsernames.remove(index);
                                beaconsList.remove(index);
                                beaconsListView.invalidate();
                                break;
                            case 2:
                                break;
                        }
                    }
                }).show();

    }
}
