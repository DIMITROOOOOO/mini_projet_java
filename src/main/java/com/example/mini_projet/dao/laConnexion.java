package com.example.mini_projet.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class laConnexion {
    private static Connection con;
    private static String user;
    private static String passWord;

    public static Connection seConnecter() {
        if (con == null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver"); // Add this line
                String url = "jdbc:mysql://localhost:3306/bdavion?useSSL=false&serverTimezone=UTC";
                con = DriverManager.getConnection(url, user, passWord);
                System.out.println("Connexion établie");
            } catch (ClassNotFoundException e) {
                System.out.println("Driver not found: " + e.getMessage());
            } catch (SQLException ex) {
                System.out.println("bd non trouver: " + ex.getMessage());
            }
        }
        return con;
    }
    public  void setUser(String user) {
        laConnexion.user = user;
    }

    public  void setPassWord(String passWord) {
        laConnexion.passWord = passWord;
    }

}