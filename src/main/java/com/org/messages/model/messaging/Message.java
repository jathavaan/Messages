package com.org.messages.model.messaging;

import com.org.messages.model.user.User;

import java.time.LocalDateTime;

public class Message extends ChatElement {
    private final String message;

    public Message(int chatElementID, String message, User sender) {
        super(chatElementID, sender);
        this.message = message;
    }

    public Message(int chatElementID, LocalDateTime sent, LocalDateTime opened, String message, User sender) {
        super(chatElementID, sent, opened, sender);
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
