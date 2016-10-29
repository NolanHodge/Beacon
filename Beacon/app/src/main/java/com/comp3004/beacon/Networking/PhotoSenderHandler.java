package com.comp3004.beacon.Networking;

import android.graphics.Bitmap;

import java.util.BitSet;
import java.util.HashMap;

/**
 * Created by julianclayton on 16-10-26.
 */
public class PhotoSenderHandler {

    private static PhotoSenderHandler photoSenderHandler = null;
    private Bitmap photoBitmap;

    private HashMap<String, Bitmap> photos;

    public PhotoSenderHandler() {
        photoSenderHandler = this;
    }

    public static PhotoSenderHandler getInstance() {
        if (photoSenderHandler == null) {
            photoSenderHandler = new PhotoSenderHandler();
        }
        return photoSenderHandler;
    }

    public void setPhoto(Bitmap bitmap) {
        System.out.println("Bitmap: " + bitmap);
        photoBitmap = bitmap;
    }

    public Bitmap getImageBitmap() {
        return photoBitmap;
    }



}
