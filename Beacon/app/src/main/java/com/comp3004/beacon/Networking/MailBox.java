package com.comp3004.beacon.Networking;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by julianclayton on 16-10-16.
 */
public class MailBox {

    private static MailBox mailBox = null;

    HashMap<String, ArrayList<ChatMessage>> inbox;

    public MailBox() {
        inbox = new HashMap<>();
        mailBox = this;
    }

    public static MailBox getInstance() {
        if (mailBox == null) {
            mailBox = new MailBox();
        }
        return  mailBox;
    }

    public void addToChatMessageMailbox(String threadId, ChatMessage chatMessage) {
        System.out.println("thread2" + threadId);
        System.out.println("message: " + chatMessage.getMessage());
        inbox.get(threadId).add(chatMessage);
    }

    public void createNewMessageThread(String threadId) {
        inbox.put(threadId, new ArrayList<ChatMessage>());
    }

    public HashMap getInbox() {
        return inbox;
    }
}
