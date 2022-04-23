package com.org.messages.view;

import com.org.messages.model.user.User;
import javafx.fxml.FXML;

public class AppController {
    private User loggedInUser;

    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }


}
