package com.org.messages.model.messaging;

import com.org.messages.model.user.User;

import java.time.LocalDateTime;

public abstract class ChatElement {
    private final int chatElementID;
    private final User sender;
    private final LocalDateTime sent;
    private LocalDateTime opened;

    protected ChatElement(int chatElementID, User sender) {
        this.chatElementID = chatElementID;
        this.sender = sender;
        this.sent = LocalDateTime.now();
    }

    public int getChatElementID() {
        return chatElementID;
    }

    public LocalDateTime getSent() {
        return sent;
    }

    public LocalDateTime getOpened() {
        return opened;
    }

    public void setOpened(LocalDateTime opened) {
        this.opened = opened;
    }
}
