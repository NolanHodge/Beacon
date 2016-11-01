package com.comp3004.beacon.GUI;

import android.app.SearchManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.comp3004.beacon.R;
import com.comp3004.beacon.User.BeaconUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;

public class UserSearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_search);

        final ArrayList<String> userNames = new ArrayList<String>();
        ListView friendsListView = (ListView) findViewById(R.id.user_searchList);
        Intent intent = getIntent();

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {

            final String query = intent.getStringExtra(SearchManager.QUERY);
            final ArrayAdapter arrayAdapter = new ArrayAdapter(UserSearchActivity.this, android.R.layout.simple_list_item_1, userNames);

            friendsListView.setAdapter(arrayAdapter);

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("beaconUsers");
            databaseReference.orderByChild("name").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    BeaconUser user = dataSnapshot.getValue(BeaconUser.class);
                    if (user.getDisplayName().toLowerCase().contains(query.toLowerCase())) {
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

    }

}
