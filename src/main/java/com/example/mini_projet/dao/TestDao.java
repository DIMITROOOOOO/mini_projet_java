package com.example.mini_projet.dao;

import com.example.mini_projet.model.avion;

public class TestDao {
    public static void main(String[] args) {
        laConnexion c= new laConnexion();
        c.setUser("root");
        c.setPassWord("");
        c.seConnecter();
        avion a=new avion("avion",10,avion.status.disponible);
        AvionDao.ajouterEquipe(a);

    }
}
