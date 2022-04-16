package com.org.messages.DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class Driver {
    private final Connection con;
    private final Statement stm;

    public Driver() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        this.con = DriverManager.getConnection("jdbc:mysql://:3306/messagedb", "root", "jathavaan12"); // Connects to DB
        this.stm = con.createStatement();
    }

    public Connection getCon() {
        return con;
    }

    public Statement getStm() {
        return stm;
    }
}
