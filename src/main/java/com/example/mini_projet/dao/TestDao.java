package com.example.mini_projet.dao;

import com.example.mini_projet.model.avion;
import com.example.mini_projet.model.equipe;
import com.example.mini_projet.model.user;

public class TestDao {
    public static void main(String[] args) {
        laConnexion c= new laConnexion();
        c.setUser("root");
        c.setPassWord("");
        c.seConnecter();

        UserDao userDao = new UserDao();

        user user = new user(0, "agent3", "password123");
        userDao.ajouterUser(user);

        user authenticated = userDao.authenticate("agent1", "password123");
        if (authenticated != null) {
            System.out.println("Authentication successful for " + authenticated.getUsername());
        }
    }
}
