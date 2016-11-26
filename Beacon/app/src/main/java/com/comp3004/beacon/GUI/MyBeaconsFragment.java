package com.comp3004.beacon.GUI;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.SymbolTable;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.comp3004.beacon.FirebaseServices.DatabaseManager;
import com.comp3004.beacon.Networking.PublicBeaconHandler;
import com.comp3004.beacon.R;
import com.comp3004.beacon.User.Beacon;
import com.comp3004.beacon.User.CurrentBeaconUser;
import com.comp3004.beacon.User.CurrentUserPublicBeaconHandler;
import com.comp3004.beacon.User.PublicBeacon;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyBeaconsFragment extends Fragment {

    ListView myBeaconsListView;
    ArrayList<Beacon> myBeacons;
    ArrayList<String> beaconTitles;
    ArrayAdapter<String> adapter;

    Handler handler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_my_beacons, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        myBeacons = new ArrayList<Beacon>();
        beaconTitles = new ArrayList<String>();
        handler = new Handler();

        myBeaconsListView = (ListView) getView().findViewById(R.id.myBeaconsListView);

        for (Object beaconId : CurrentBeaconUser.getInstance().getMyBeacons().keySet()) {
            myBeacons.add(CurrentBeaconUser.getInstance().getMyBeacon((String)beaconId));
            Beacon beacon = CurrentBeaconUser.getInstance().getMyBeacon((String) beaconId);
            beaconTitles.add(getTitle(beacon));

        }
        populateBeaconsListView();
        registerFriendsListviewCallback();
    }

    //creates an instance of mybeacons for swipe view
    public static MyBeaconsFragment newInstance() {
        MyBeaconsFragment fragment = new MyBeaconsFragment();;
        return fragment;
    }

    private String getTitle(Beacon beacon) {
        List<String> ids = Arrays.asList(beacon.getBeaconId().split("_"));
        if (ids.size() == 3 && ids.get(2).equals("private")) {
            String displayName = CurrentBeaconUser.getInstance().getFriend(beacon.getToUserId()).getDisplayName();
            return "Private Beacon with " + displayName;
        }
        else return "Public Beacon";

    }

    private void populateBeaconsListView() {

        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, beaconTitles) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);


                return view;
            }
        };
        myBeaconsListView.setAdapter(adapter);
    }

    private void registerFriendsListviewCallback() {
        myBeaconsListView = (ListView) getView().findViewById(R.id.myBeaconsListView);
        final Context context = getActivity();

        myBeaconsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog alertDialog;
                Beacon selectedBeacon = myBeacons.get(position);
                showBeaconOptionDialog(selectedBeacon, position);
            }
        });
    }



    private void showBeaconOptionDialog(final Beacon beacon, final int position) {
        final CurrentBeaconUser currentBeaconUser = CurrentBeaconUser.getInstance();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme);
        builder .setTitle(myBeaconsListView.getItemAtPosition(position).toString())
                .setItems(new String[]{"Delete", "Cancel"}, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                myBeacons.remove(position);
                                beaconTitles.remove(position);
                                currentBeaconUser.removeMyBeacon(beacon.getBeaconId());
                                DatabaseManager.getInstance().removeYourBeaconFromDb(beacon);
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                adapter.notifyDataSetChanged();
                                                myBeaconsListView.invalidateViews();
                                            }
                                        });
                                    }
                                });
                                break;
                            case 1:
                                break;

                        }
                    }
                }).show();
    }

}
