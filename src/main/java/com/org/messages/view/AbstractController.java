package com.org.messages.view;

import javafx.scene.control.Alert;

import java.time.LocalDateTime;

public class AbstractController {
    protected void showErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(null);
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }
}
