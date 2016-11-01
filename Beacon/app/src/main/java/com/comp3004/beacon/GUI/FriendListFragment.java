package com.comp3004.beacon.GUI;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
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
import android.widget.Toast;

import com.comp3004.beacon.FirebaseServices.DatabaseManager;
import com.comp3004.beacon.Networking.MailBox;
import com.comp3004.beacon.Networking.MessageSenderHandler;
import com.comp3004.beacon.R;
import com.comp3004.beacon.User.BeaconUser;
import com.comp3004.beacon.User.CurrentBeaconUser;
import java.util.ArrayList;


public class FriendListFragment extends Fragment {

    ArrayList<BeaconUser> friendsList;
    ArrayList<String> userNames;
    ListView friendsListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_friend_list, container, false);
    }


    @Override
    public void onStart() {
        super.onStart();

        DatabaseManager.getInstance().subscribeToMessageThread();

        friendsList = new ArrayList<BeaconUser>();
        userNames = new ArrayList<String>();
        friendsListView = (ListView) getView().findViewById(R.id.friendListView);
        FloatingActionButton messageButton = (FloatingActionButton) getView().findViewById(R.id.message_friends_button);
        Toast.makeText(getActivity(), "Running Add Friends",
                Toast.LENGTH_SHORT).show();
        //Add friends to list for GUI
        if (CurrentBeaconUser.getInstance().getFriends() != null) {

            for (Object key : CurrentBeaconUser.getInstance().getFriends().keySet()) {
                BeaconUser beaconUser = (BeaconUser) CurrentBeaconUser.getInstance().getFriends().get(key);
                if (!beaconUser.getUserId().equals(CurrentBeaconUser.getInstance().getUserId())) {
                    friendsList.add(beaconUser);
                    userNames.add(beaconUser.getDisplayName());
                }
            }
        }

        messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChatMessageThreadsActivity.class);
                getActivity().startActivity(intent);
            }
        });


        populateFriendsListView();
        registerFriendsListviewCallback();

    }

    private void populateFriendsListView() {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, userNames) {
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
        friendsListView.setAdapter(adapter);
    }
    private void registerFriendsListviewCallback() {
        friendsListView = (ListView) getView().findViewById(R.id.friendListView);
        final Context context = getActivity();

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
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final Context context = getActivity();
        builder.setTitle("Send " + beaconUser.getDisplayName())
                .setItems(new String[]{"Beacon", "Message", "Cancel"}, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                    return;
                                }
                                Location current = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) == null ?
                                        locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) :
                                        locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                MessageSenderHandler.getInstance().sendBeaconRequest(friendsList.get(userIndex).getUserId(), current);
                                break;
                            case 1:
                                String chatId = CurrentBeaconUser.getInstance().getUserId() + "_" + friendsList.get(userIndex).getUserId();
                                if (!MailBox.getInstance().getInbox().containsKey(chatId)) {
                                    MailBox.getInstance().initializeThread(chatId);
                                    MessageSenderHandler.getInstance().sendMessage(friendsList.get(userIndex).getUserId(), "Has started a chat");
                                }
                                Intent intent = new Intent(getActivity(), ChatActivity.class);
                                intent.putExtra("CHAT_ID", CurrentBeaconUser.getInstance().getUserId() + "_" + friendsList.get(userIndex).getUserId());
                                intent.putExtra("CHAT_PARTICIPANT", friendsList.get(userIndex).getUserId());
                                getActivity().startActivity(intent);
                                break;
                            case 2:
                                break;
                        }
                    }
                });
        builder.create().show();
    }
}
