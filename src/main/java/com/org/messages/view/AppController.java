package com.org.messages.view;

import com.org.messages.DB.ChatDriver;
import com.org.messages.DB.MessagingDriver;
import com.org.messages.model.dataHandling.Conversion;
import com.org.messages.model.messaging.*;
import com.org.messages.model.user.User;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.ArrayList;

public class AppController extends AbstractController {
    private User loggedInUser;
    private ArrayList<Chat> chats = new ArrayList<Chat>();
    private Chat activeChat;
    @FXML
    ListView<Chat> chatList;
    @FXML
    GridPane chatGrid;
    @FXML
    VBox vBox;

    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
        updateChatView();
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    @FXML
    public void populateChatGrid() {
        MessagingDriver md = null;
        try {
            md = new MessagingDriver();
            ArrayList<ChatElement> messages = md.getMessagesInChat(activeChat);

            // Iterates through all the sent messages in the active chat and prints them out
            messages.forEach(message -> {
                if (message.getSender().equals(getLoggedInUser())) {
                    // Creates the messages the logged in user has sent
                    HBox userMessage = createChatElement(message, Pos.CENTER_RIGHT);
                    userMessage.autosize();
                    vBox.getChildren().add(userMessage);
                } else {
                    // Creates the messages anyone but the logged in user has sent
                    HBox otherMessage = createChatElement(message, Pos.CENTER_LEFT);
                    otherMessage.autosize();
                    vBox.getChildren().add(otherMessage);
                }
            });
        } catch (Exception e) {
            showErrorMessage("Something went wrong");
        }
    }

    @FXML
    public void updateChatView() {
        try {
            ChatDriver cd = new ChatDriver();
            chats = cd.retrieveChatByUser(getLoggedInUser());
            chatList.getItems().addAll(chats);

            chatList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Chat>() {
                @Override
                public void changed(ObservableValue<? extends Chat> observableValue, Chat chat, Chat t1) {
                    // Clears active messages
                    if (vBox != null) vBox.getChildren().clear();
                    if (vBox != null) vBox.getChildren().clear();

                    // Opens the active chat
                    activeChat = chatList.getSelectionModel().getSelectedItem();
                    populateChatGrid();
                }
            });
        } catch (IllegalArgumentException e) {
            showErrorMessage(e.getMessage());
        } catch (IllegalStateException e) {
            showErrorMessage(e.getMessage());
        } catch (Exception e) {
            showErrorMessage(e.getMessage());
        }
    }

    private HBox createChatElement(ChatElement message, Pos pos) {
        if (message instanceof Message) {
            String textString = ((Message) message).getMessage();
            String msg = message.getSender().getFirstName() + " " + message.getSender().getSurname() + "\n" +
                    textString + "\n" + Conversion.dateToString(message.getSent());

            HBox hBox = new HBox();
            hBox.setAlignment(pos);
            hBox.setPadding(new Insets(5, 5, 5, 10));

            Text text = new Text(msg);
            TextFlow textFlow = new TextFlow(text);


            textFlow.setStyle(
                 "-fx-color: rgb(239, 242, 255) " +
                 "-fx-background-color: rgb(15, 125, 242) "
            );

            textFlow.setPadding(new Insets(5, 10, 5, 10));
            // text.setFill(Color.color(0.924, 0.945, 0.996));
            hBox.getChildren().add(textFlow);

            return hBox;
        } else if (message instanceof Media) {
            return null;
        } else if (message instanceof VoiceNote) {
            return null;
        } else {
            showErrorMessage("Unsupported datatype for message");
            return null;
        }
    }
}
