package com.org.messages.model.loginAndRegister;

import com.org.messages.DB.Driver;

import java.sql.SQLException;
import java.time.LocalDateTime;

public class Register {
    public static void register(String email, String password, String firstName, String surname, LocalDateTime dateOfBirth) throws SQLException, ClassNotFoundException {
        Driver d = new Driver();
        d.createUser(email, password, firstName, surname, dateOfBirth);
    }
}
