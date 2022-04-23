package com.org.messages.model.loginAndRegister;

import com.org.messages.DB.Driver;
import com.org.messages.DB.UserDriver;
import com.org.messages.model.user.User;

import java.sql.SQLException;

public class Login {
    public static boolean login(String email, String password) throws SQLException, ClassNotFoundException {
        UserDriver ud = new UserDriver();
        email = email.toLowerCase();
        User user = ud.retrieveUser(email);

        if (user == null) return false; // Checks if a user with the provied email exists
        if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
            return true; // Valid email and password
        } else {
            return false;
        }
    }
}
