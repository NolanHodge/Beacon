package com.comp3004.beacon.GUI;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.comp3004.beacon.R;

public class ChatMessageThreadsActivity extends AppCompatActivity {

    ListView conversationsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.comp3004.beacon.R.layout.activity_chat_message_threads);

        conversationsListView = (ListView) findViewById(R.id.conversationsListView);


    }
}
