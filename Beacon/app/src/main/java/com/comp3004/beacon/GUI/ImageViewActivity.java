package com.comp3004.beacon.GUI;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.comp3004.beacon.FirebaseServices.DatabaseManager;
import com.comp3004.beacon.Networking.MailBox;
import com.comp3004.beacon.Networking.PhotoSenderHandler;
import com.comp3004.beacon.R;

import java.io.File;

public class ImageViewActivity extends AppCompatActivity {

    ImageView imageView;
    Handler  mHandler;
    public static final String IMAGE_USER_ID = "USER_ID";
    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.comp3004.beacon.R.layout.activity_image_view);
        mHandler = new Handler();
        imageView = (ImageView) findViewById(R.id.imageView2);
        //progressBar.setVisibility(View.GONE);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            userId = extras.getString(IMAGE_USER_ID);
        }
        System.out.println("USER ID " + userId);

        File file = DatabaseManager.getInstance().loadPhotos(userId);



        System.out.println("FILE " + file.getAbsolutePath());
        imageView.setImageDrawable(Drawable.createFromPath(file.getPath()));
    }
    @Override
    protected void onResume() {
        super.onResume();
        //DatabaseManager.getInstance().loadPhotos(userId + "_photos");
    }
}
