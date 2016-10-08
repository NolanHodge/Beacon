package com.comp3004.beacon.GUI;

import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.comp3004.beacon.FirebaseServices.DatabaseManager;
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
        for (Object key : CurrentBeaconUser.getInstance().getFriends().keySet()) {
            BeaconUser beaconUser = (BeaconUser) CurrentBeaconUser.getInstance().getFriends().get(key);
            friendsList.add(beaconUser);
            userNames.add(beaconUser.getDisplayName());
        }


        populateFriendsListView();

    }

    private void populateFriendsListView() {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, userNames);
        friendsListView.setAdapter(adapter);
    }

}
