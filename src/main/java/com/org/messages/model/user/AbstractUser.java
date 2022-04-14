package com.org.messages.model.user;

import com.org.messages.model.messaging.Chat;
import com.org.messages.model.comparators.ChatComparator;

import java.util.*;

public abstract class AbstractUser {
    private final int userID;
    private String email, password;
    private ArrayList<Chat> chats;


    protected AbstractUser(int userID, String email, String password) {
        this.userID = userID;
        setEmail(email);
        setPassword(password);
    }

    public int getUserID() {
        return userID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<Chat> getChats() {
        ArrayList<Chat> chats = new ArrayList(Arrays.asList(this.chats));
        ChatComparator chatComparator = new ChatComparator();
        Collections.sort(chats, chatComparator);
        return chats;
    }

    public void addChat(Chat chat) {
        ArrayList<Chat> chats = getChats();
        chats.add(chat);
        this.chats = chats;
    }
}
