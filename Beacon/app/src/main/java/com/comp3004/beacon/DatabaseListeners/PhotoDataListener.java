package com.comp3004.beacon.DatabaseListeners;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.comp3004.beacon.Networking.PhotoSenderHandler;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Created by julianclayton on 16-10-26.
 */
public class PhotoDataListener implements ChildEventListener {


    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        String base64Image = (String) dataSnapshot.getValue();
        byte[] imageAsBytes = Base64.decode(base64Image.getBytes(), Base64.DEFAULT);
        InputStream inputStream  = new ByteArrayInputStream(imageAsBytes);
        Bitmap bitmap  = BitmapFactory.decodeStream(inputStream);

        PhotoSenderHandler.getInstance().setPhoto(bitmap);

    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
