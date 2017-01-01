package com.comp3004.beacon;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Chat implements Parcelable {
    String key;
    ArrayList<Message> messages = new ArrayList<>();
    ArrayList<String> members = new ArrayList<>();

    public Chat() {

    }

    protected Chat(Parcel in) {
        key = in.readString();
        in.readTypedList(messages, Message.CREATOR);
        members = in.createStringArrayList();
    }

    public static final Creator<Chat> CREATOR = new Creator<Chat>() {
        @Override
        public Chat createFromParcel(Parcel in) {
            return new Chat(in);
        }

        @Override
        public Chat[] newArray(int size) {
            return new Chat[size];
        }
    };

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public ArrayList<String> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<String> members) {
        this.members = members;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(key);
        parcel.writeTypedList(messages);
        parcel.writeStringList(members);
    }

    public Message getLastMessage() {
        return messages.get(messages.size() - 1);
    }
}
