package com.comp3004.beacon.GUI;

import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.comp3004.beacon.FirebaseServices.DatabaseManager;
import com.comp3004.beacon.Networking.ChatMessage;
import com.comp3004.beacon.Networking.MailBox;
import com.comp3004.beacon.Networking.MessageSenderHandler;
import com.comp3004.beacon.R;
import com.comp3004.beacon.User.BeaconUser;
import com.comp3004.beacon.User.CurrentBeaconUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    ArrayList<ChatMessage> chatThread;
    ArrayList<ChatMessage> chatThread2;
    ArrayList<ChatMessage> combinedChatThread;

    ListView chatListView;
    EditText chatTextbox;
    Button sendButton;
    String otherChatParticipantId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        chatListView = (ListView) findViewById(R.id.chatListView);
        chatTextbox = (EditText) findViewById(R.id.chatEditText);
        sendButton = (Button) findViewById(R.id.sendChatMessageButton);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            String chatId = extras.getString("CHAT_ID");
            chatThread = (ArrayList<ChatMessage>) MailBox.getInstance().getChatThread(chatId);
            otherChatParticipantId = extras.getString("CHAT_PARTICIPANT");
        }




        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageSenderHandler.getInstance().sendMessage(otherChatParticipantId, chatTextbox.getText().toString());
                finish();
                startActivity(getIntent());
            }
        });

        populateFriendsListView();
    }
    private void populateFriendsListView() {
        CurrentBeaconUser currentBeaconUser = CurrentBeaconUser.getInstance();
        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        chatListView = (ListView) findViewById(R.id.chatListView);

        for (ChatMessage chatMessage : chatThread) {
            //otherChatParticipantId = chatMessage.getFromUserId();
            Map<String, String> datum = new HashMap<String, String>(2);
            System.out.println("From:" + chatMessage.getFromUserId());
            BeaconUser friend = currentBeaconUser.getFriend(chatMessage.getFromUserId());
            datum.put("username", friend.getDisplayName());
            datum.put("message", chatMessage.getMessage());
            data.add(datum);
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(this, data,
                android.R.layout.simple_list_item_2,
                new String[] {"username", "message"},
                new int[] {android.R.id.text1,
                        android.R.id.text2});

        chatListView.setAdapter(simpleAdapter);
    }

}
