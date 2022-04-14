package com.org.messages.DB;

import com.org.messages.model.user.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Driver {
    private final Connection con;
    private final Statement stm;

    public Driver() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        this.con = DriverManager.getConnection("jdbc:mysql://:3306/messagedb", "root", "jathavaan12");
        this.stm = con.createStatement();
    }

    public Connection getCon() {
        return con;
    }

    public Statement getStm() {
        return stm;
    }

    // HANDLE USER

    public void createUser(String email, String password, String firstName, String surname, LocalDateTime dateOfBirth) throws SQLException {
        email = email.toLowerCase(); // changes email to lowercase letters

        if (retrieveUser(email) != null)
            throw new IllegalStateException("An account with this email has already been registered.");

        User user = new User(0, email, password, firstName, surname, dateOfBirth); // Validation of user input

        String sql = "insert into user (email, password, firstName, surname, dateOfBirth) values ('" + email + "', '" + password + "', '" + firstName + "', '" + surname + "', '" + dateOfBirth + "')";
        getStm().executeUpdate(sql);
        System.out.println("User added to database: " + user);
    }

    public User retrieveUser(String email) throws SQLException {
        email = email.toLowerCase();
        String sql = "select * from messagedb.user where email = '" + email + "'";
        ResultSet rs = getStm().executeQuery(sql);

        ArrayList<User> result = new ArrayList<>(); // Should not contain more than 1 element

        while (rs.next()) {
            int dbUserID = rs.getInt("userID");
            String dbEmail = rs.getString("email");
            String dbPassword = rs.getString("password");
            String dbFirstName = rs.getString("firstName");
            String dbSurname = rs.getString("surname");
            String dbDateOfBirth = rs.getString("dateOfBirth");

            // Converting date of birth to LocalDateTime object
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime dbDob = LocalDateTime.parse(dbDateOfBirth, formatter);

            if (result.size() == 0) {
                result.add(new User(dbUserID, dbEmail, dbPassword, dbFirstName, dbSurname, dbDob));
            } else {
                break;
            }
        }

        if (result.size() == 1) {
            return result.get(0);
        } else {
            return null;
        }
    }

    // HANDLE CHAT



    // HANDLE MESSAGE

    public static void main(String[] args) {
        Driver d = null;
        try {
            d = new Driver();
            d.createUser("jathavaan12@gmail.com", null, null, null, null);
            User user = d.retrieveUser("jathavaan12@gmail.com");
            System.out.println(user);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        } catch (IllegalStateException e) {
            System.err.println(e.getMessage());
        }
    }
}
