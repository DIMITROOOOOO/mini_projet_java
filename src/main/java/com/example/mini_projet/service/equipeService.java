package com.example.mini_projet.service;

import com.example.mini_projet.model.equipe;
import com.example.mini_projet.dao.EquipeDao;
import java.util.List;

public class equipeService {
    private EquipeDao equipeDao = new EquipeDao();

    public List<equipe> getAllEquipes() {
        return equipeDao.findAll();
    }

    public void setDisponible(int id, boolean disponible) {
        equipe equipe = equipeDao.findById(id);
        if (equipe != null) {
            equipe.setDisponible(disponible);
            equipeDao.updateEquipe(equipe);
        }
    }
}