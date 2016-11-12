package com.comp3004.beacon.GUI;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.os.AsyncTask;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    ArrayList<ChatMessage> chatThread;
    ArrayList<String> chats;
    ListView chatListView;
    EditText chatTextbox;
    Button sendButton;
    String otherChatParticipantId, chatWith = "";
    //ArrayAdapter<String> adapter = null;
    ChatAdapter adapter;
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

            CurrentBeaconUser currentBeaconUser = CurrentBeaconUser.getInstance();
            BeaconUser friend = currentBeaconUser.getFriend(otherChatParticipantId);

            getSupportActionBar().setTitle("Chat with " + friend.getDisplayName());

            populateChatListView();
            new GetImage(currentBeaconUser.getPhotoUrl(), friend.getPhotoUrl()).execute();
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

    }

    @Override
    protected void onStart() {
        super.onStart();
        new messageListener().execute();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(broadcastReceiver);
    }

    private void populateChatListView() {

        CurrentBeaconUser currentBeaconUser = CurrentBeaconUser.getInstance();
        BeaconUser aFriend = new BeaconUser();
        chats = new ArrayList<>();
        chatListView = (ListView) findViewById(R.id.chatListView);
        HashMap<String, String> photoUrls = new HashMap<>();
        for (ChatMessage chatMessage : chatThread) {
            //otherChatParticipantId = chatMessage.getFromUserId();
            Map<String, String> userAndMesMap = new HashMap<String, String>(2);
            BeaconUser friend = currentBeaconUser.getFriend(chatMessage.getFromUserId());
            photoUrls.put(currentBeaconUser.getUserId(), currentBeaconUser.getPhotoUrl());
            if (!friend.getDisplayName().equals(CurrentBeaconUser.getInstance().getDisplayName())) {
                chatWith = friend.getDisplayName();
                aFriend = friend;
                photoUrls.put(friend.getUserId(), friend.getPhotoUrl());

            }
            userAndMesMap.put("username", friend.getDisplayName());
            userAndMesMap.put("message", chatMessage.getMessage());
            chats.add(friend.getDisplayName() + "\n" + chatMessage.getMessage());

        }

        new GetImage(photoUrls.get(currentBeaconUser.getUserId()), photoUrls.get(aFriend.getUserId())).execute();
        getSupportActionBar().setTitle("Chat with " + aFriend.getDisplayName());

        //setAdapter();

    }

    private class GetImage extends AsyncTask<Void, Void, Bitmap[]> {
        String userIcon = "";
        String friendIcon = "";

        public GetImage(String userIcon, String friendIcon) {
            this.userIcon = userIcon;
            this.friendIcon = friendIcon;
        }

        @Override
        protected Bitmap[] doInBackground(Void... params) {

            Bitmap mIcon11 = null;
            Bitmap mIcon12 = null;
            try {
                InputStream in = new java.net.URL(userIcon).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
                in.close();
                in = new java.net.URL(friendIcon).openStream();
                mIcon12 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return new Bitmap[]{mIcon11, mIcon12};
        }

        @Override
        protected void onPostExecute(Bitmap[] aVoid) {
            adapter = new ChatAdapter(chatThread, aVoid[0], aVoid[1]);
            chatListView.setAdapter(adapter);
            chatListView.setSelection(chatListView.getCount() - 1);
        }
    }

    /*
        private void setAdapter() {
            adapter = new ArrayAdapter<String>(this, R.layout.chat_left, chats) {

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {

                    View view = super.getView(position, convertView, parent);

                    //View text1 = view.findViewById(R.id.chat_layout);

                    if ((position % 2) == 1) {
                        view.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));

                    } else {
                        view.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
                    }
                    return view;
                }
            };
            chatListView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            chatListView.setSelection(adapter.getCount() - 1);
            setTitle("Chat with " + chatWith);
        }
    */
    private class messageListener extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (intent.getAction().equals(UPDATE_MESSAGE_THREAD)) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                finish();
                                startActivity(getIntent());
                            }
                        });
                    }
                }
            };

            IntentFilter filter = new IntentFilter(UPDATE_MESSAGE_THREAD);
            registerReceiver(broadcastReceiver, filter);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            populateChatListView();
            adapter.notifyDataSetChanged();
        }
    }

    private class ChatAdapter extends ArrayAdapter<ChatMessage> {
        ArrayList<ChatMessage> messages;
        Bitmap userIcon, friendIcon;

        public ChatAdapter(ArrayList<ChatMessage> objects, Bitmap userIcon, Bitmap friendIcon) {
            super(ChatActivity.this, R.layout.chat_left, objects);
            this.userIcon = userIcon;
            this.friendIcon = friendIcon;
            messages = objects;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.chat_left, parent, false);

                viewHolder.message = (TextView) convertView.findViewById(R.id.text1);
                viewHolder.layout = convertView.findViewById(R.id.chat_layout);
                viewHolder.icon = (ImageView) convertView.findViewById(R.id.user_icon);
                convertView.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            if (messages.get(position).getFromUserId().equals(CurrentBeaconUser.getInstance().getUserId()) && userIcon != null) {
                viewHolder.icon.setImageBitmap(userIcon);
            } else {
                if (friendIcon != null)
                    viewHolder.icon.setImageBitmap(friendIcon);
            }

            viewHolder.message.setText(messages.get(position).getMessage());
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) viewHolder.layout.getLayoutParams();

            if (!messages.get(position).getFromUserId().equals(CurrentBeaconUser.getInstance().getUserId())) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    params.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                }
                params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    params.removeRule(RelativeLayout.ALIGN_PARENT_LEFT);
                }
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            }
            viewHolder.layout.setLayoutParams(params);

            return convertView;
        }
    }

    private static class ViewHolder {
        public TextView message;
        public View layout;
        public ImageView icon;
    }
}


