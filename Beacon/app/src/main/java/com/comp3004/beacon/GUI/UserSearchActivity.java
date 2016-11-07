package com.comp3004.beacon.GUI;

import android.app.SearchManager;

import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AlertDialog;

import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.TextView;

import com.comp3004.beacon.FirebaseServices.DatabaseManager;
import com.comp3004.beacon.Networking.CurrentFriendRequestsHandler;
import com.comp3004.beacon.Networking.MessageSenderHandler;
import com.comp3004.beacon.R;
import com.comp3004.beacon.User.BeaconUser;
import com.comp3004.beacon.User.CurrentBeaconUser;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class UserSearchActivity extends AppCompatActivity {

    ArrayList<String>     userNames;
    ArrayList<BeaconUser> users;
    ListView              friendsListView;
    String                query;
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_search);

        userNames = new ArrayList<>();
        friendsListView = (ListView) findViewById(R.id.user_searchList);
        users = new ArrayList<>();

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
             query = intent.getStringExtra(SearchManager.QUERY);
             arrayAdapter = new ArrayAdapter<String>(UserSearchActivity.this,
                    android.R.layout.simple_list_item_1, userNames){
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);

                    TextView text1 = (TextView) view.findViewById(android.R.id.text1);

                    if ((position % 2) == 1) {
                        view.setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimary));
                        text1.setTextColor(getContext().getResources().getColor(android.R.color.white));
                    } else {
                        view.setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimaryDark));
                        text1.setTextColor(getContext().getResources().getColor(android.R.color.white));
                    }

                    return view;
                }
            };

            friendsListView.setAdapter(arrayAdapter);

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("beaconUsers");
            databaseReference.orderByChild("name").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    BeaconUser user = dataSnapshot.getValue(BeaconUser.class);

                    // We do not want our own name to show up in search, check that here
                    if (user.getDisplayName().toLowerCase().contains(query.toLowerCase()) &&
                            !user.getDisplayName().equals(CurrentBeaconUser.getInstance().getDisplayName())) {
                        users.add(user);
                        userNames.add(user.getDisplayName());
                        arrayAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        registerUserSearchDialog();
    }

    public void registerUserSearchDialog() {
        final ListView friendsListView = (ListView) findViewById(R.id.user_searchList);

        friendsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BeaconUser selectedBeaconUser = users.get(position);
                showUserSearchDialog(selectedBeaconUser, position);
            }
        });
    }

    public void showUserSearchDialog(BeaconUser beaconUser, final int userIndex) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(beaconUser.getDisplayName())
                .setItems(new String[]{"Add Friend", "Cancel"}, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                BeaconUser selectedUser = users.get(userIndex);
                                MessageSenderHandler.getInstance().sendFriendRequest(users.get(userIndex).getUserId());
                                CurrentFriendRequestsHandler.getInstance().setPendingAprovalUser(users.get(userIndex));
                                break;
                            case 1:
                                break;
                        }
                    }
                });
        builder.create().show();
    }

}
