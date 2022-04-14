package com.org.messages.model.messaging;

import com.org.messages.model.comparators.MessageComparator;
import com.org.messages.model.user.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class Chat {
    private final int chatID;
    private final LocalDateTime createdDate = LocalDateTime.now();
    private User creator;
    private Collection<User> admins = new ArrayList<User>();
    private Collection<User> chatListeners;
    private ArrayList<ChatElement> messages = new ArrayList<ChatElement>();
    private LocalDateTime lastActive;

    public Chat(int chatID, User creator, Collection<User> chatMembers) {
        this.chatID = chatID;
        setCreator(creator);
        chatMembers.stream().forEach(chatMember -> this.addChatListener(chatMember));
        setLastActive();
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        if (!getAdmins().contains(creator))
            addAdmin(creator);

        this.creator = creator;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public Collection<User> getAdmins() {
        return admins;
    }

    public void addAdmin(User user) {
        Collection<User> admins = getAdmins();
        admins.add(user);
        this.admins = admins;
    }

    public Collection<User> getChatListeners() {
        return new ArrayList(Arrays.asList(this.chatListeners));
    }

    public void addChatListener(User chatListener) {
        Collection<User> chatListeners = getChatListeners();
        if (!getChatListeners().contains(chatListener))
            chatListeners.add(chatListener);

        this.chatListeners = chatListeners;
    }

    public void removeChatListener(User chatListener) {
        Collection<User> chatListeners = (Collection<User>) getChatListeners().stream().filter((cl) -> !cl.equals(chatListener));
        this.chatListeners = chatListeners;
    }


    public ArrayList<ChatElement> getMessages() {
        Comparator<ChatElement> messageComparator = new MessageComparator();
        ArrayList<ChatElement> messages = new ArrayList(Arrays.asList(this.messages));
        Collections.sort(messages, messageComparator);
        return messages;
    }

    public void sendMessage(ChatElement message) {
        ArrayList<ChatElement> messages = getMessages();
        messages.add(message);
        setLastActive();
        this.messages = messages;
    }

    public LocalDateTime getLastActive() {
        return lastActive;
    }

    public void setLastActive() {
        this.lastActive = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Chat{" +
                "chatID=" + chatID +
                ", createdDate=" + createdDate +
                ", creator=" + creator +
                ", admins=" + admins +
                ", chatListeners=" + chatListeners +
                ", messages=" + messages +
                ", lastActive=" + lastActive +
                '}';
    }

    public static void main(String[] args) {
        User jatha = new User(1, "jathavaan12@gmail.com", "jathavaan12", "Jathavaan", "Shankarr", null);
        Collection<User> members = new ArrayList(Arrays.asList(jatha));

        Chat chat = new Chat(1, jatha, members);
        System.out.println(chat);
    }
}
