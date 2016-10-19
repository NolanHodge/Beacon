package com.comp3004.beacon.Networking;

/**
 * Created by julianclayton on 16-10-16.
 */
public class ChatMessage {

    String fromUserId;
    String toUserId;
    String message;
    long timeStamp;
    String messageId;
    String threadId;

    public ChatMessage() {}

    public ChatMessage(String toUserId, String fromUserId, String message, String threadId) {
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.message = message;
        this.timeStamp = System.currentTimeMillis();
        this.threadId = threadId;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
    public String getThreadId() {
        return threadId;
    }

    public void setThreadId(String threadId) {
        this.threadId = threadId;
    }

    public String getMessageId() {
        return messageId;
    }
}
