package com.example.mini_projet.service;

import com.example.mini_projet.dao.AvionDao;
import com.example.mini_projet.model.avion;
import com.example.mini_projet.model.status;

import java.util.List;

public class avionService {
    private AvionDao avionDao=new AvionDao();
    public void addAvion(String modele, int capacite, status status) {
        if (modele == null || modele.trim().isEmpty()) {
            throw new IllegalArgumentException("Modele cannot be empty");
        }
        if (capacite <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }
        if (status == null) {
            throw new IllegalArgumentException("Statut cannot be null");
        }
        avion avion = new avion( modele, capacite, status);
        avionDao.ajouteravion(avion);
    }
    public List<avion> getAllAvions() {
        return avionDao.findAll();
    }
    public void deleteAvion(int id) {
        avionDao.supprimeravion(id);
    }
    public void editAvion(avion avion) {
        if (avion == null || avion.getId() <= 0) {
            throw new IllegalArgumentException("Modele cannot be empty");
        }
        if (avion.getModel() == null || avion.getModel().trim().isEmpty()) {
            throw new IllegalArgumentException("Modele cannot be empty");
        }
        if (avion.getNbPlace() <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }
        if (avion.getStatus() == null) {
            throw new IllegalArgumentException("Statut cannot be null");
        }
        avionDao.editavion(avion);
    }
    public avion findById(int id) {
        return avionDao.findById(id);
    }


}
