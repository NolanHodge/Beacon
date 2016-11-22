package com.comp3004.beacon.Networking;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.comp3004.beacon.GUI.ChatActivity;

import java.io.InputStream;

/**
 * Created by Jason on 19/11/2016.
 */

public class GetImage extends AsyncTask<String, Void, Bitmap[]> {
    @Override
    protected Bitmap[] doInBackground(String... params) {
        Bitmap[] images = new Bitmap[params.length];
        for(int i = 0; i < params.length; i++){
            try {
                InputStream in = new java.net.URL(params[i]).openStream();
                images[i] = BitmapFactory.decodeStream(in);
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return images;
    }
}

