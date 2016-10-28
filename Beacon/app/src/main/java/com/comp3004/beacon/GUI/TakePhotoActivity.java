package com.comp3004.beacon.GUI;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.comp3004.beacon.FirebaseServices.DatabaseManager;
import com.comp3004.beacon.Networking.PhotoSenderHandler;
import com.comp3004.beacon.R;

import java.io.File;
import java.net.URI;

public class TakePhotoActivity extends Activity {

    private final static String DEBUG_TAG = "TakePhotoActivity";
    static final private int CAM_REQUEST = 1;
    private ImageView imageView;
    private Button takePhotoButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo);

        imageView = (ImageView) findViewById(R.id.imageView);
        takePhotoButton = (Button) findViewById(R.id.captureImageButton);
        DatabaseManager.getInstance().loadPhotos();


        if (PhotoSenderHandler.getInstance().getImageBitmap() != null) {
            imageView.setImageBitmap(PhotoSenderHandler.getInstance().getImageBitmap());
        }

        takePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File file = getFile();
                camera_intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                startActivityForResult(camera_intent, CAM_REQUEST);
            }
        });
    }

    private File getFile() {
        File folder = new File("sdcard/camera_app");

        if (!folder.exists()) {
            folder.mkdir();
        }
        File imageFile = new File(folder, "camera_img.jpg");
        return imageFile;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        String path = "sdcard/camera_app/camera_img.jpg";
        imageView.setImageDrawable(Drawable.createFromPath(path));
    }
}