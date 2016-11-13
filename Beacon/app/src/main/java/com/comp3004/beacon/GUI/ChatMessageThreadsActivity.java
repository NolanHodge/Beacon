package com.comp3004.beacon.GUI;

import android.content.Context;
import android.content.Intent;

import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;

import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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
    ArrayList<String> chatIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.comp3004.beacon.R.layout.activity_chat_message_threads);
        chatTitles = new ArrayList<String>();
        chatIds = new ArrayList<String>();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey("CHAT_WITH")) {
                String chatWith = extras.getString("CHAT_WITH");
                setTitle(chatWith);
            }
        }

        conversationsListView = (ListView) findViewById(R.id.conversationsListView);

        DatabaseManager.getInstance().subscribeToMessageThread();

        CurrentBeaconUser currentBeaconUser = CurrentBeaconUser.getInstance();
        MailBox mailBox = MailBox.getInstance();

        if (mailBox.getInbox() != null ) {
            for (Object key : mailBox.getInbox().keySet()) {
                ArrayList<ChatMessage> thread = (ArrayList) mailBox.getInbox().get(key);
                //Below will be better when multi-user messaging is enabled
                if (!thread.isEmpty()) {
                    List<String> chatParticipantIds = Arrays.asList(thread.get(0).getThreadId().split("_"));

                    BeaconUser chatParticipant = (BeaconUser) currentBeaconUser.getFriends().get(chatParticipantIds.get(1));
                    if (chatParticipant != null) {
                        String chatTitle = chatParticipant.getDisplayName();
                        if (!chatParticipant.getUserId().equals(currentBeaconUser.getUserId())) {
                            chatTitles.add(chatTitle);
                            chatIds.add((String) key);
                        }
                    }
                }
            }
        }
        populateFriendsListView();
        registerChatMessageListviewCallback();
   }
    private void populateFriendsListView() {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, chatTitles){
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

        conversationsListView.setAdapter(adapter);
    }

    private void registerChatMessageListviewCallback() {
        conversationsListView = (ListView) findViewById(R.id.conversationsListView);
        final Context context = this;

        conversationsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String chatId = chatIds.get(position);
                Intent intent = new Intent(ChatMessageThreadsActivity.this, ChatActivity.class);
                intent.putExtra("CHAT_ID", chatId);
                List<String> chatParticipantIds =  Arrays.asList(chatId.split("_"));
                intent.putExtra("CHAT_PARTICIPANT", chatParticipantIds.get(1));
                startActivity(intent);
            }
        });
    }
}
