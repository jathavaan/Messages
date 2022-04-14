package com.org.messages.DB;
import com.org.messages.model.user.User;

import java.sql.*;
import java.time.LocalDateTime;

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

    public void createUser(String email, String password, String firstName, String surname, LocalDateTime dateOfBirth) throws SQLException {
        User user = new User(0, email, password, firstName, surname, null);

        String sql = "insert into User (email, password, firstName, surname, dateOfBirth) values ('"+ email +"', '"+ password +"', '"+ firstName +"', '"+ surname + "', '"+ dateOfBirth +"')";
        getStm().executeUpdate(sql);
        System.out.println("User added to database");
    }

    public static void main(String[] args) {
        try {
            Driver driver = new Driver();
            String email = "jathavaan12@gmail.com";
            String password = "jathavaan12";
            String firstName = "Jathavaan";
            String surname = "Shankarr";
            LocalDateTime dateOfBirth = LocalDateTime.now();
            driver.createUser(email, password, firstName, surname, dateOfBirth);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
