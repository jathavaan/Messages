package com.org.messages.view;

import com.org.messages.DB.ChatDriver;
import com.org.messages.DB.MessagingDriver;
import com.org.messages.model.dataHandling.Conversion;
import com.org.messages.model.messaging.*;
import com.org.messages.model.user.User;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AppController extends AbstractController implements Initializable {
    private User loggedInUser;
    private ArrayList<Chat> chats = new ArrayList<Chat>();
    private Chat activeChat;
    @FXML
    ListView<Chat> chatList;
    @FXML
    GridPane chatGrid;
    @FXML
    VBox vBox;
    @FXML
    ScrollPane messageScrollPane;

    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
        updateChatView();
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        vBox.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                messageScrollPane.setVvalue((Double) t1);
            }
        });
    }

    @FXML
    public void populateChatGrid() {
        MessagingDriver md = null;
        try {
            md = new MessagingDriver();
            ArrayList<ChatElement> messages = md.getMessagesInChat(activeChat);

            // Iterates through all the sent messages in the active chat and prints them out
            messages.forEach(message -> {
                if (message.getSender().getUserID() == loggedInUser.getUserID()) {
                    // Creates the messages the logged in user has sent
                    HBox userMessage = createChatElement(message, true);
                    userMessage.autosize();
                    vBox.getChildren().add(userMessage);
                } else {
                    // Creates the messages anyone but the logged in user has sent
                    HBox otherMessage = createChatElement(message, false);
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

    private HBox createChatElement(ChatElement message, boolean isSender) {
        if (message instanceof Message) {
            HBox hBox = new HBox();

            String senderString = message.getSender().getFirstName() + "\n";
            String messageString = ((Message) message).getMessage() + "\n";
            String timeString = Conversion.dateToString(message.getSent());

            Text senderText = new Text(senderString);
            Text messageText = new Text(messageString);
            Text timeText = new Text(timeString);

            senderText.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
            messageText.setStyle("-fx-font-size: 14px;");
            timeText.setStyle("-fx-font-size: 14px; -fx-font-style: italic;");

            TextFlow textFlow = new TextFlow();

            if (isSender) {
                hBox.setAlignment(Pos.CENTER_RIGHT);
                hBox.setPadding(new Insets(5, 10, 5, 5));
                textFlow.setStyle("-fx-color: #eff2ff;" + "-fx-background-color: #0f7df2;" + "-fx-background-radius: 10px;");

                senderText.setFill(Color.color(0.924, 0.945, 0.996));
                messageText.setFill(Color.color(0.924, 0.945, 0.996));
                timeText.setFill(Color.color(0.924, 0.945, 0.996));

                textFlow.getChildren().addAll(senderText, messageText, timeText);
            } else {
                hBox.setAlignment(Pos.CENTER_LEFT);
                hBox.setPadding(new Insets(5, 5, 5, 10));
                textFlow.setStyle("-fx-background-color: #e9e9eb;" + "-fx-background-radius: 10px;");

                textFlow.getChildren().addAll(senderText, messageText, timeText);
            }

            textFlow.setPadding(new Insets(5, 10, 5, 10));
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
