package com.comp3004.beacon.GUI;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
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
import com.comp3004.beacon.R;
import com.comp3004.beacon.User.BeaconUser;
import com.comp3004.beacon.User.CurrentBeaconUser;

import java.util.ArrayList;
import java.util.HashMap;

public class FriendListActivity extends AppCompatActivity {

    ArrayList<BeaconUser> friendsList;
    ArrayList<String> userNames;
    ListView friendsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

        friendsList = new ArrayList<BeaconUser>();
        userNames = new ArrayList<String>();
        friendsListView = (ListView) findViewById(R.id.friendListView);

        //Add friends to list for GUI
        if (CurrentBeaconUser.getInstance().getFriends() != null) {
            for (Object key : CurrentBeaconUser.getInstance().getFriends().keySet()) {
                BeaconUser beaconUser = (BeaconUser) CurrentBeaconUser.getInstance().getFriends().get(key);
                friendsList.add(beaconUser);
                userNames.add(beaconUser.getDisplayName());
            }
        }
        populateFriendsListView();
        registerFriendsListviewCallback();

    }

    private void populateFriendsListView() {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, userNames);
        friendsListView.setAdapter(adapter);
    }

    private void registerFriendsListviewCallback() {
        friendsListView = (ListView) findViewById(R.id.friendListView);
        final Context context = this;

        friendsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog alertDialog;
                BeaconUser selectedBeaconUser = friendsList.get(position);
                showBeaconOptionDialog(selectedBeaconUser, position);
            }
        });
    }

    public void showBeaconOptionDialog(BeaconUser beaconUser, final int userIndex) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final Context context = this;
        builder.setTitle("Send " + beaconUser.getDisplayName() + " a....")
                .setItems(new String[]{"Beacon", "Message", "Cancel"}, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                    return;
                                }
                                Location current = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                                MessageSenderHandler.getInstance().sendBeaconRequest(friendsList.get(userIndex).getUserId(), current);
                                break;
                            case 1:
                                break; //TODO Sends message;
                            case 2:
                                break;
                        }
                    }
                });
        builder.create().show();
    }
}
