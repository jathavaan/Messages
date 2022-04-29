package com.org.messages.view;

import com.org.messages.DB.UserDriver;
import com.org.messages.model.loginAndRegister.Login;
import com.org.messages.model.user.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;

public class LoginController {
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    TextField emailField, passwordField;

    @FXML
    Button loginButton;


    @FXML
    public void onLogin(ActionEvent event) {
        UserDriver ud = null;

        try {
            ud = new UserDriver();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String email = emailField.getText();
        String password = passwordField.getText();

        try {
            if (Login.login(email, password)) {
                // Sends to next scene with user credentials
                User loggedInUser = ud.retrieveUser(email);

                URL url = new File("src/main/resources/com/org/messages/app.fxml").toURI().toURL();
                FXMLLoader loader = new FXMLLoader(url);
                root = (Parent) loader.load();

                AppController appController = loader.getController();
                appController.setLoggedInUser(loggedInUser);

                stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } else {
                System.out.println("Incorrect login credentials");
            }
        } catch (Exception e) {
            // Add proper error handling
            e.printStackTrace();
            System.err.println(e.getMessage());
        }
    }
}
