package com.org.messages.view;

import com.org.messages.DB.ChatDriver;
import com.org.messages.model.messaging.Chat;
import com.org.messages.model.user.User;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.sql.SQLException;
import java.util.ArrayList;

public class AppController {
    private User loggedInUser;
    private ArrayList<Chat> chats = new ArrayList<Chat>();
    private Chat activeChat;

    @FXML
    ListView<Chat> chatList;

    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
        updateChatView();
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public void updateChatView() {
        ChatDriver cd = null;
        try {
            cd = new ChatDriver();
            chats = cd.retrieveChatByUser(getLoggedInUser());
            chatList.getItems().addAll(chats);

            chatList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Chat>() {
                @Override
                public void changed(ObservableValue<? extends Chat> observableValue, Chat chat, Chat t1) {
                    activeChat = chatList.getSelectionModel().getSelectedItem();
                    System.out.println(activeChat);
                }
            });
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void addChatToListView(ListView listView, Chat chat) {
        listView.setItems((ObservableList) chats);
    }


}
