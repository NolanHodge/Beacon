package com.comp3004.beacon.GUI;

import android.content.DialogInterface;
import android.content.Intent;
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
import com.comp3004.beacon.R;
import com.comp3004.beacon.User.Beacon;
import com.comp3004.beacon.User.PrivateBeacon;
import com.comp3004.beacon.User.BeaconUser;
import com.comp3004.beacon.User.CurrentBeaconUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BeaconsListFragment extends Fragment {

    ArrayList<PrivateBeacon> beaconsList;
    ArrayList<String> beaconTitles;
    ListView beaconsListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_beacons_list, container, false);
    }
    @Override
    public void onStart() {
        super.onStart();
        getActivity().setTitle("your title");
        beaconsList = new ArrayList<PrivateBeacon>();
        beaconTitles = new ArrayList<String>();

        beaconsListView = (ListView) getView().findViewById(R.id.beaconsListView);

        CurrentBeaconUser currentBeaconUser = CurrentBeaconUser.getInstance();

        if (currentBeaconUser.getBeacons() != null) {
            for (Object key : CurrentBeaconUser.getInstance().getBeacons().keySet()) {
                Beacon beacon = CurrentBeaconUser.getInstance().getBeacons().get(key);
                BeaconUser bc = (BeaconUser) CurrentBeaconUser.getInstance().getFriends().get(beacon.getFromUserId());
                beaconTitles.add(getTitle(beacon));
                beaconsList.add(currentBeaconUser.getBeacons().get(key));
            }
        }
        populateBeaconsListView();
        registerBeaconsListviewCallback();

    }
    public static BeaconsListFragment newInstance() {
        BeaconsListFragment fragment = new BeaconsListFragment();;
        return fragment;
    }
    private void populateBeaconsListView() {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, beaconTitles){
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
        beaconsListView = (ListView) getView().findViewById(R.id.beaconsListView);
        beaconsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PrivateBeacon selectedPrivateBeacon = beaconsList.get(position);
                showBeaconOptionDialog(selectedPrivateBeacon, position);
            }
        });
    }
    public void showBeaconOptionDialog(final PrivateBeacon privateBeacon, final int index) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme);
        builder .setTitle(beaconsListView.getItemAtPosition(index).toString())
                .setItems(new String[]{"Track", "Delete", "Cancel"}, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Intent intent = new Intent(getActivity(), ArrowActivity2.class);
                                intent.putExtra(ArrowActivity2.CURRENT_BEACON_ID_KEY, privateBeacon.getBeaconId());
                                startActivity(intent);
                                break;
                            case 1:
                                CurrentBeaconUser currentBeaconUser = CurrentBeaconUser.getInstance();
                                currentBeaconUser.getBeacons().remove(privateBeacon.getBeaconId());
                                DatabaseManager.getInstance().removeBeaconFromDb(privateBeacon.getBeaconId());
                                getActivity().finish();
                                startActivity(getActivity().getIntent());
                                beaconTitles.remove(index);
                                beaconsList.remove(index);
                                beaconsListView.invalidate();
                                break;
                            case 2:
                                break;
                        }
                    }
                }).show();

    }

    private String getTitle(Beacon beacon) {
        List<String> ids = Arrays.asList(beacon.getBeaconId().split("_"));
        String displayName = CurrentBeaconUser.getInstance().getFriend(beacon.getFromUserId()).getDisplayName();
        if (ids.size() == 3 && ids.get(2).equals("private")) {
            return "Private Beacon: " + displayName;
        }
        else return "Public Beacon: " + displayName;

    }
    

}
