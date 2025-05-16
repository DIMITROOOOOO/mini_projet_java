package com.example.mini_projet.service;

import com.example.mini_projet.dao.AvionDao;
import com.example.mini_projet.model.avion;
import com.example.mini_projet.model.status;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class avionService {
    private final AvionDao avionDao = new AvionDao();
    private static final Map<String, Integer> lesavions = new HashMap<>();

    static {
        lesavions.put("Boeing 737", 180);
        lesavions.put("Airbus A320", 150);
        lesavions.put("Boeing 747", 400);
        lesavions.put("Airbus A380", 550);
        lesavions.put("Embraer E175", 76);
    }

    public void addAvion(String model, int nbPlace, status status) {
        avion avion = new avion(model, nbPlace, status);
        avionDao.ajouteravion(avion);
    }

    public List<avion> getAllAvions() {
        return avionDao.findAll();
    }

    public void editAvion(avion avion) {
        avionDao.editavion(avion);
    }

    public void deleteAvion(int id) {
        avionDao.supprimeravion(id);
    }

    public Map<String, Integer> getModelCapacityMap() {
        return lesavions;
    }

    public List<String> getModelList() {
        return List.copyOf(lesavions.keySet());
    }
}