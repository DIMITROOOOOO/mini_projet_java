package com.example.mini_projet.model;

import java.time.LocalDate;

public class vol {
    private int id;
    private String destination;
    private LocalDate dateDepart;
    private avion avion;
    public equipe equipe;

    public vol(int id, String destination, LocalDate dateDepart, equipe equipe, avion avion) {
        this.id = id;
        this.destination = destination;
        this.dateDepart = dateDepart;
        this.equipe = equipe;
        this.avion = avion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public LocalDate getDateDepart() {
        return dateDepart;
    }

    public void setDateDepart(LocalDate dateDepart) {
        this.dateDepart = dateDepart;
    }

    public equipe getEquipe() {
        return equipe;
    }

    public void setEquipe(equipe equipe) {
        this.equipe = equipe;
    }

    public avion getAvion() {
        return avion;
    }

    public void setAvion(avion avion) {
        this.avion = avion;
    }
}
