package com.comp3004.beacon.Networking;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.comp3004.beacon.User.BeaconUser;

import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * Created by julianclayton on 16-10-29.
 */
public class PhotoMessage {

    private String fromUserId;
    private String fromDisplayName;
    private String imageBinary;

    public PhotoMessage() {}

    public PhotoMessage(File file, BeaconUser beaconUser) {
        Bitmap imageBitmap= BitmapFactory.decodeFile(file.getPath());

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        String base64Image = Base64.encodeToString(bytes, Base64.DEFAULT);
        imageBinary = base64Image;
        fromUserId = beaconUser.getUserId();
        fromDisplayName = beaconUser.getDisplayName();
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getFromDisplayName() {
        return fromDisplayName;
    }

    public void setFromDisplayName(String fromDisplayName) {
        this.fromDisplayName = fromDisplayName;
    }


    public String getImageBinary() {
        return imageBinary;
    }

    public void setImageBinary(String image) {
        this.imageBinary = image;
    }

}
