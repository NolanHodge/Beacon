package com.comp3004.beacon.GUI;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.comp3004.beacon.R;

public class GenericActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generic_list);


        setSupportActionBar((Toolbar) findViewById(R.id.generic_toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Log.e("BACK", "back");
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showProgressBar(boolean show) {
        if (show) {
            findViewById(R.id.generic_progress_bar).setVisibility(View.VISIBLE);
            return;
        }
        findViewById(R.id.generic_progress_bar).setVisibility(View.INVISIBLE);
    }
}