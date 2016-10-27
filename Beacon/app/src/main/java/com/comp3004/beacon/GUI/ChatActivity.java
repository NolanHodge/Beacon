package com.comp3004.beacon.GUI;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Paint;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.graphics.Color;
import android.widget.Toast;

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
        chatListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            String chatId = extras.getString("CHAT_ID");
            chatThread = MailBox.getInstance().getChatThread(chatId);
            otherChatParticipantId = extras.getString("CHAT_PARTICIPANT");
        }

        chatListView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    return true; // Indicates that this has been handled by you and will not be forwarded further.
                }
                return false;
            }
        });

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
            BeaconUser friend = currentBeaconUser.getFriend(chatMessage.getFromUserId());
            if (!friend.getDisplayName().equals(CurrentBeaconUser.getInstance().getDisplayName())) chatWith = friend.getDisplayName();
            userAndMesMap.put("username", friend.getDisplayName());
            userAndMesMap.put("message", chatMessage.getMessage());

            // only want last 6 messages m8
            userAndMessage.add(userAndMesMap);

            if (userAndMessage.size() > 5)
                for (int i=0; i<userAndMessage.size(); i++) if (i < userAndMessage.size() - 6) userAndMessage.remove(i);

        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(this, userAndMessage,
                R.layout.chat_left,
                new String[] {"username", "message"},
                new int[] {android.R.id.text1,
                        android.R.id.text2})
        {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                int MAX_MESSAGE_LEN = 30;
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);
                if (text1.getText().equals(CurrentBeaconUser.getInstance().getDisplayName()))
                {

                    LayoutParams layout = (LayoutParams)text2.getLayoutParams();
                    layout.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    text2.setLayoutParams(layout);
                }

                text1.setText("");
                return view;
            }
        };

        chatListView.setAdapter(simpleAdapter);
        setTitle("Chat with " + chatWith);
    }

}
