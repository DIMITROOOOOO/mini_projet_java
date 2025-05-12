package com.example.mini_projet.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class laConnexion {
    private static Connection con;
    private static String user = "root";
    private static String passWord = "";
    private static final String URL = "jdbc:mysql://localhost:3306/bdavion?useSSL=false&serverTimezone=UTC";

    public static void setUser(String user) {
        laConnexion.user = user;
    }

    public static void setPassWord(String passWord) {
        laConnexion.passWord = passWord;
    }

    public static Connection seConnecter() {
        try {
            if (con == null || con.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                con = DriverManager.getConnection(URL, user, passWord);
            }
            return con;
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Failed to connect to database: " + e.getMessage(), e);
        }
    }

    public static void closeConnection() {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error closing connection: " + e.getMessage(), e);
        }
    }
}