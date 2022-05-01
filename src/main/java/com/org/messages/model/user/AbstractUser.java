package com.org.messages.model.user;

import com.org.messages.model.messaging.Chat;
import com.org.messages.model.comparators.ChatComparator;

import java.util.*;
import java.util.regex.Pattern;

public abstract class AbstractUser {
    private final int userID;
    private String email, password;
    private ArrayList<Chat> chats;


    protected AbstractUser(int userID, String email, String password) {
        checkUserID(userID);
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
        checkEmail(email);
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        checkPassword(password);
        this.password = password;
    }

    public ArrayList<Chat> getChats() {
        ArrayList<Chat> chats = new ArrayList(Arrays.asList(this.chats));
        ChatComparator chatComparator = new ChatComparator();
        Collections.sort(chats, chatComparator);
        return chats;
    }

    public void addChat(Chat chat) {
        if (chat == null) throw new IllegalArgumentException("Chat cannot be null");

        ArrayList<Chat> chats = getChats();
        if (!chats.contains(chat)) chats.add(chat);

        this.chats = chats;
    }

    private void checkUserID(int userID) {
        if (userID < 0) throw new IllegalArgumentException("Invalid userID");
    }

    private void checkEmail(String email) {
        String regex = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";

        if (email == null || email.isBlank()) throw new IllegalArgumentException("Email cannot be empty");
        if (!email.matches(regex)) throw new IllegalArgumentException("Invalid email");
    }

    private void checkPassword(String password) {
        String regex = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";

        if (password == null || password.isBlank()) throw new IllegalArgumentException("Password cannot be empty");
        if (!password.matches(regex)) throw new IllegalArgumentException(
                "Invalid password. Please ensure the password matches the following requirements:\n" +
                "At least eight characters long\n" +
                "At least one uppercase- and lowercase letter, and at least one number"
        );
    }
}
