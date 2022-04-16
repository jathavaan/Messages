package com.org.messages.model.loginAndRegister;

import com.org.messages.DB.Driver;
import com.org.messages.model.user.User;

import java.sql.SQLException;

public class Login {
    public static boolean login(String email, String password) throws SQLException, ClassNotFoundException {
        Driver d = new Driver();
        email = email.toLowerCase();
        User user = d.retrieveUser(email);

        if (user == null) return false; // Checks if a user with the provied email exists
        if (user.getEmail() == email && user.getPassword() == password) return true; // Valid email and password

        return false;
    }
}
