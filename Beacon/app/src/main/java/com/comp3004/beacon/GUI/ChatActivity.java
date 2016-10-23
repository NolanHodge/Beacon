package com.comp3004.beacon.GUI;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Paint;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.graphics.Color;
import com.comp3004.beacon.Networking.ChatMessage;
import com.comp3004.beacon.Networking.MailBox;
import com.comp3004.beacon.Networking.MessageSenderHandler;
import com.comp3004.beacon.R;
import com.comp3004.beacon.User.BeaconUser;
import com.comp3004.beacon.User.CurrentBeaconUser;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    ArrayList<ChatMessage> chatThread;
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

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            String chatId = extras.getString("CHAT_ID");
            chatThread = MailBox.getInstance().getChatThread(chatId);
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

    private void populateFriendsListView() {
        CurrentBeaconUser currentBeaconUser = CurrentBeaconUser.getInstance();
        List<Map<String, String>> userAndMessage = new ArrayList<Map<String, String>>();
        chatListView = (ListView) findViewById(R.id.chatListView);


        for (ChatMessage chatMessage : chatThread) {
            //otherChatParticipantId = chatMessage.getFromUserId();
            Map<String, String> userAndMesMap = new HashMap<String, String>(2);
            System.out.println("From:" + chatMessage.getFromUserId());
            BeaconUser friend = currentBeaconUser.getFriend(chatMessage.getFromUserId());
            if (!friend.getDisplayName().equals(CurrentBeaconUser.getInstance().getDisplayName())) chatWith = friend.getDisplayName();
            userAndMesMap.put("username", friend.getDisplayName());
            userAndMesMap.put("message", chatMessage.getMessage());
            userAndMessage.add(userAndMesMap);
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(this, userAndMessage,
                R.layout.chat_left,
                new String[] {"username", "message"},
                new int[] {android.R.id.text1,
                        android.R.id.text2})
        {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);
                if (text1.getText().equals(CurrentBeaconUser.getInstance().getDisplayName()))
                {
                    text2.getLayoutParams().width = -500;
                    text2.setTextColor(Color.WHITE);
                    LayoutParams layout = (LayoutParams)text2.getLayoutParams();
                    layout.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    text2.setLayoutParams(layout);
                }
                else
                {
                    text2.getLayoutParams().width = -500;
                    text2.setBackgroundColor(Color.GRAY);
                    text2.setTextColor(Color.WHITE);
                }
                text1.setText("");
                return view;
            }
        };

        chatListView.setAdapter(simpleAdapter);

        chatListView.setSelection(simpleAdapter.getCount() - 1);

        setTitle("Chat with " + chatWith);
    }
}
