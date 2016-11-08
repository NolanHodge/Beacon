package com.comp3004.beacon.GUI;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.comp3004.beacon.R;
import com.comp3004.beacon.User.Beacon;
import com.comp3004.beacon.User.CurrentBeaconUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyBeaconsActivity extends AppCompatActivity {

    ListView myBeaconsListView;
    ArrayList<Beacon> myBeacons;
    ArrayList<String> beaconTitles;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.comp3004.beacon.R.layout.activity_my_beacons);
        myBeacons = new ArrayList<Beacon>();
        beaconTitles = new ArrayList<String>();

        myBeaconsListView = (ListView) findViewById(R.id.myBeaconsListView);

        for (Object beaconId : CurrentBeaconUser.getInstance().getMyBeacons().keySet()) {
            System.out.println("Really doe: " + (String )beaconId);
            myBeacons.add(CurrentBeaconUser.getInstance().getMyBeacon((String )beaconId));
            beaconTitles.add(getTitle((String)beaconId));

        }
        populateBeaconsListView();

    }

    private String getTitle(String beaconId) {
        List<String> ids = Arrays.asList(beaconId.split("_"));
        if (ids.size() == 3 && ids.get(2).equals("private")) {
            return "Private Beacon";
        }
        else return "Public Beacon";

    }

    private void populateBeaconsListView() {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, beaconTitles) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                TextView text1 = (TextView) view.findViewById(android.R.id.text1);


                return view;
            }
        };
        myBeaconsListView.setAdapter(adapter);
    }
}
