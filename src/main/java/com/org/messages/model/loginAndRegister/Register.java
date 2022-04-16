package com.org.messages.model.loginAndRegister;

import com.org.messages.DB.Driver;
import com.org.messages.DB.UserDriver;

import java.sql.SQLException;
import java.time.LocalDateTime;

public class Register {
    public static void register(String email, String password, String firstName, String surname, LocalDateTime dateOfBirth) throws SQLException, ClassNotFoundException {
        UserDriver ud = new UserDriver();
        ud.createUser(email, password, firstName, surname, dateOfBirth);
    }
}
