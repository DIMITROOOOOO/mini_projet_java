package com.example.mini_projet.service;

import com.example.mini_projet.dao.AvionDao;
import com.example.mini_projet.model.avion;
import com.example.mini_projet.model.status;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class avionService {
    private final AvionDao avionDao = new AvionDao();

    public void addAvion(String model, int nbPlace, status status) {
        avion newAvion = new avion(model, nbPlace, status);
        avionDao.ajouteravion(newAvion);
    }

    public void editAvion(avion avion) {
        avionDao.editavion(avion);
    }

    public void deleteAvion(int id) {
        avionDao.supprimeravion(id);
    }

    public ObservableList<avion> getAllAvions() {
        List<avion> avions = avionDao.findAll();
        return FXCollections.observableArrayList(avions);
    }
}