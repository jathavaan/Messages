package com.org.messages.DB;

import com.org.messages.model.user.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class Driver {
    private final Connection con;
    private final Statement stm;

    public Driver() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        this.con = DriverManager.getConnection("jdbc:mysql://:3306/messagedb", "root", "jathavaan12"); // Connects to DB
        this.stm = con.createStatement();
    }

    protected Connection getCon() {
        return con;
    }

    protected Statement getStm() {
        return stm;
    }

    protected User generateUserFromResultSet(ResultSet rs) throws SQLException {
        int userID = rs.getInt("userID");
        String email = rs.getString("email");
        String password = rs.getString("password");
        String firstName = rs.getString("firstName");
        String surname = rs.getString("surname");
        LocalDateTime dateOfBirth = formatDate(rs.getString("dateOfBirth"));

        return new User(userID, email, password, firstName, surname, dateOfBirth);
    }

    protected LocalDateTime formatDate(String dateString) {
        if (dateString == null)
            return null;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(dateString, formatter);
    }
}
