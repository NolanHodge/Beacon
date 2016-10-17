package com.comp3004.beacon.GUI;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.comp3004.beacon.FirebaseServices.DatabaseManager;
import com.comp3004.beacon.Networking.ChatMessage;
import com.comp3004.beacon.Networking.MailBox;
import com.comp3004.beacon.R;
import com.comp3004.beacon.User.BeaconUser;
import com.comp3004.beacon.User.CurrentBeaconUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChatMessageThreadsActivity extends AppCompatActivity {

    ListView conversationsListView;
    ArrayList<String> chatTitles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.comp3004.beacon.R.layout.activity_chat_message_threads);
        chatTitles = new ArrayList<String>();
        conversationsListView = (ListView) findViewById(R.id.conversationsListView);

        DatabaseManager.getInstance().subscribeToMessageThread();

        CurrentBeaconUser currentBeaconUser = CurrentBeaconUser.getInstance();
        MailBox mailBox = MailBox.getInstance();

        for(Object key : mailBox.getInbox().keySet()) {
            ArrayList<ChatMessage> thread = (ArrayList) mailBox.getInbox().get(key);

            //Below will be better when multi-user messaging is enabled
            List<String> chatParticipantIds =  Arrays.asList(thread.get(0).getThreadId().split("_"));
            BeaconUser chatParticipant = (BeaconUser) currentBeaconUser.getFriends().get(chatParticipantIds.get(1));
            String chatTitle = "Chat with " + chatParticipant.getDisplayName();

            chatTitles.add(chatTitle);

        }

        populateFriendsListView();
   }
    private void populateFriendsListView() {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, chatTitles);
        conversationsListView.setAdapter(adapter);
    }
}
