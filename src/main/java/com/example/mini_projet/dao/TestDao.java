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
        System.out.println("cbn");


    }
}
