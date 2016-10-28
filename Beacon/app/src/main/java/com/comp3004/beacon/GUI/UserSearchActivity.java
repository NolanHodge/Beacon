package com.comp3004.beacon.GUI;

import android.app.SearchManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.comp3004.beacon.R;

public class UserSearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_search);

        if (Intent.ACTION_SEARCH.equals(getIntent().getAction())) {
            System.out.println(getIntent().getStringExtra(SearchManager.QUERY));
        }
    }

}
