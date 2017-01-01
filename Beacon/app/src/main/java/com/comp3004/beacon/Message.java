package com.comp3004.beacon;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Jason on 27/12/2016.
 */

public class Message implements Parcelable {
    String from, body;
    long dateSent;
    boolean read;

    public Message() {
    }

    protected Message(Parcel in) {
        from = in.readString();
        body = in.readString();
        dateSent = in.readLong();
    }

    public static final Creator<Message> CREATOR = new Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

    public void setRead(boolean read) {
        this.read = read;
    }

    public boolean getRead() {
        return read;
    }

    public long getDateSent() {
        return dateSent;
    }

    public String getBody() {
        return body;
    }

    public String getFrom() {
        return from;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setDateSent(long dateSent) {
        this.dateSent = dateSent;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(from);
        parcel.writeString(body);
        parcel.writeLong(dateSent);
    }
}
