package com.org.messages.view;

import com.org.messages.DB.ChatDriver;
import com.org.messages.model.messaging.Chat;
import com.org.messages.model.user.User;
import javafx.fxml.Initializable;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AppController {
    private User loggedInUser;
    private ArrayList<Chat> chats;

    public AppController() {
        ChatDriver cd = null;

        try {
            cd = new ChatDriver();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            this.chats = cd.retrieveChatByUser(loggedInUser);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }
}
