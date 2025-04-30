package com.example.mini_projet.model;

public class avion {
    private int id;
    private String model;
    private int nbPlace;
    public static status status;

    public avion( String model, int nbPlace, status status) {
        this.model = model;
        this.nbPlace = nbPlace;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getNbPlace() {
        return nbPlace;
    }

    public void setNbPlace(int nbPlace) {
        this.nbPlace = nbPlace;
    }

    public status getStatus() {
        return status;
    }

    public void setStatus(status status) {
        this.status = status;
    }
}
