package com.comp3004.beacon.Networking;

import android.graphics.Bitmap;

import com.comp3004.beacon.User.Beacon;
import com.comp3004.beacon.User.BeaconUser;
import com.comp3004.beacon.User.CurrentBeaconUser;

import java.io.File;
import java.util.BitSet;
import java.util.HashMap;

/**
 * Created by julianclayton on 16-10-26.
 */
public class PhotoSenderHandler {

    private static PhotoSenderHandler photoSenderHandler = null;
    private HashMap<String, File> photos;

    public PhotoSenderHandler() {
        photoSenderHandler = this;
        photos = new HashMap<String, File>();
        BeaconUser currentBeaconUser = CurrentBeaconUser.getInstance();
        for (Object key : currentBeaconUser.getBeacons().keySet()) {
            photos.put((String) key, null);
        }
    }


    public static PhotoSenderHandler getInstance() {
        if (photoSenderHandler == null) {
            photoSenderHandler = new PhotoSenderHandler();
        }
        return photoSenderHandler;
    }


    public File getFile(String id ) {
         return photos.get(id);
    }

    public void addFile(String id, File file ){

        photos.put(id, file);
    }

}
