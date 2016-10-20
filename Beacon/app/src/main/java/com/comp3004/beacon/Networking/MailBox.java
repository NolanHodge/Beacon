package com.comp3004.beacon.Networking;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by julianclayton on 16-10-16.
 */
public class MailBox {

    private static MailBox mailBox = null;

    HashMap<String, ArrayList<ChatMessage>> inbox;

    public MailBox() {
        inbox = new HashMap<String, ArrayList<ChatMessage>>();
        mailBox = this;
    }

    public static MailBox getInstance() {
        if (mailBox == null) {
            mailBox = new MailBox();
        }
        return  mailBox;
    }

    public void addToChatMessageMailbox(String threadId, ChatMessage chatMessage) {
        if (!inbox.containsKey(threadId)) {
            inbox.put(threadId, new ArrayList<ChatMessage>());
        }
        inbox.get(threadId).add(chatMessage);

    }

    public void createNewMessageThread(String threadId) {
        inbox.put(threadId, new ArrayList<ChatMessage>());
    }

    public HashMap getInbox() {
        return inbox;
    }

    public ArrayList<ChatMessage> getChatThread(String id) {

        ArrayList<ChatMessage> thread1;
        ArrayList<ChatMessage> thread2;
        ArrayList<ChatMessage> combinedThread;

        thread1 = inbox.get(id);
        thread2 = inbox.get(reverseChatId(id));

        if (!inbox.containsKey(reverseChatId(id))) {
            return thread1;
        }

        List<ChatMessage> newList = new ArrayList<ChatMessage>(thread1);
        newList.addAll(thread2);
        combinedThread = (ArrayList<ChatMessage>) newList;

        Collections.sort(combinedThread, new Comparator<ChatMessage>() {
            @Override
            public int compare(ChatMessage o1, ChatMessage o2) {
                return (int) (o1.getTimeStamp() - o2.getTimeStamp());
            }
        });

        Set<String> hs = new HashSet<>();
        ArrayList<ChatMessage> combinedThread2 = new ArrayList<>();
        
        for (ChatMessage chatMessage : combinedThread)
            if (hs.add(String.valueOf(chatMessage.getTimeStamp())))
                combinedThread2.add(chatMessage);

        return combinedThread2;

    }

    public void removeChatMessageFromThread(String threadId, ChatMessage chatMessage) {
        ArrayList<ChatMessage> thread;

        if (inbox.containsKey(threadId)) {
            thread = inbox.get(threadId);

            for (int i = 0; i < thread.size(); i++) {
                if (thread.get(i).getTimeStamp() == chatMessage.getTimeStamp()) {
                    thread.remove(i);
                }
            }
        }
        if (inbox.containsKey(reverseChatId(threadId))) {
            thread = inbox.get(reverseChatId(threadId));
            for (int i = 0; i < thread.size(); i++) {
                if (thread.get(i).getTimeStamp() == chatMessage.getTimeStamp()) {
                    thread.remove(i);
                }
            }
        }
    }

    public String reverseChatId(String chatId) {
        List<String> ids = Arrays.asList(chatId.split("_"));
        String id1 = ids.get(0);
        String id2 = ids.get(1);
        return id2 + "_" + id1;
    }
}
