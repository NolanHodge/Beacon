package com.comp3004.beacon.GUI;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.comp3004.beacon.Networking.ChatMessage;
import com.comp3004.beacon.Networking.MailBox;
import com.comp3004.beacon.Networking.MessageSenderHandler;
import com.comp3004.beacon.R;
import com.comp3004.beacon.User.BeaconUser;
import com.comp3004.beacon.User.CurrentBeaconUser;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    ArrayList<ChatMessage> chatThread;
    ArrayList<String>      chats;
    ListView chatListView;
    EditText chatTextbox;
    Button sendButton;
    String otherChatParticipantId, chatWith = "";
    private BroadcastReceiver broadcastReceiver;
    static final public String UPDATE_MESSAGE_THREAD = "UPDATE_MESSAGE_THREAD";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        chatListView = (ListView) findViewById(R.id.chatListView);
        chatTextbox = (EditText) findViewById(R.id.chatEditText);
        sendButton = (Button) findViewById(R.id.sendChatMessageButton);
        chatListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            String chatId = extras.getString("CHAT_ID");
            chatThread = MailBox.getInstance().getChatThread(chatId);
            otherChatParticipantId = extras.getString("CHAT_PARTICIPANT");
        }

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!chatTextbox.getText().toString().equals(""))  // don't send empty messages
                {
                    MessageSenderHandler.getInstance().sendMessage(otherChatParticipantId, chatTextbox.getText().toString());
                    finish();
                    startActivity(getIntent());
                }
            }
        });

        //populateFriendsListView();
        populateChatListView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(UPDATE_MESSAGE_THREAD ))
                {
                    finish();
                    startActivity(getIntent());
                }
            }
        };

        IntentFilter filter = new IntentFilter( UPDATE_MESSAGE_THREAD );
        registerReceiver(broadcastReceiver, filter);
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        unregisterReceiver(broadcastReceiver);
    }

    private void populateChatListView() {

        CurrentBeaconUser currentBeaconUser = CurrentBeaconUser.getInstance();
        List<Map<String, String>> userAndMessage = new ArrayList<Map<String, String>>();
        chats = new ArrayList<>();
        chatListView = (ListView) findViewById(R.id.chatListView);

        for (ChatMessage chatMessage : chatThread) {
            //otherChatParticipantId = chatMessage.getFromUserId();
            Map<String, String> userAndMesMap = new HashMap<String, String>(2);
            BeaconUser friend = currentBeaconUser.getFriend(chatMessage.getFromUserId());
            if (!friend.getDisplayName().equals(CurrentBeaconUser.getInstance().getDisplayName())) chatWith = friend.getDisplayName();
            userAndMesMap.put("username", friend.getDisplayName());
            userAndMesMap.put("message", chatMessage.getMessage());

            chats.add(friend.getDisplayName() + "\n" + chatMessage.getMessage());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, chats){
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

        chatListView.setAdapter(adapter);
        chatListView.setSelection(adapter.getCount() - 1);
        setTitle("Chat with " + chatWith);
    }
}
