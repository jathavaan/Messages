package com.org.messages.model.messaging;

import com.org.messages.model.user.User;

public class Message extends ChatElement {
    private final String message;

    public Message(int chatElementID, String message, User sender) {
        super(chatElementID, sender);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "Message{" +
                "message='" + message + '\'' +
                '}';
    }
}
