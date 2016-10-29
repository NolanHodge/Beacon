package com.comp3004.beacon.GUI;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.comp3004.beacon.Networking.PhotoSenderHandler;
import com.comp3004.beacon.R;

public class ImageViewActivity extends AppCompatActivity {

    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.comp3004.beacon.R.layout.activity_image_view);

        imageView = (ImageView) findViewById(R.id.imageView2);


        if (PhotoSenderHandler.getInstance().getImageBitmap() != null) {
            imageView.setImageBitmap(PhotoSenderHandler.getInstance().getImageBitmap());
        }
    }
}
