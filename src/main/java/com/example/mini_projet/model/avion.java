package com.example.mini_projet.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class avion {
    private IntegerProperty id = new SimpleIntegerProperty();
    private StringProperty model = new SimpleStringProperty();
    private IntegerProperty nbPlace = new SimpleIntegerProperty();
    private ObjectProperty<status> status = new SimpleObjectProperty<>();

    public avion(String model, int nbPlace, status status) {
        this.model.set(model);
        this.nbPlace.set(nbPlace);
        this.status.set(status);
    }

    public avion(int id, String model, int nbPlace, status status) {
        this.id.set(id);
        this.model.set(model);
        this.nbPlace.set(nbPlace);
        this.status.set(status);
    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getModel() {
        return model.get();
    }

    public StringProperty modelProperty() {
        return model;
    }

    public void setModel(String model) {
        this.model.set(model);
    }

    public int getNbPlace() {
        return nbPlace.get();
    }

    public IntegerProperty nbPlaceProperty() {
        return nbPlace;
    }

    public void setNbPlace(int nbPlace) {
        this.nbPlace.set(nbPlace);
    }

    public status getStatus() {
        return status.get();
    }

    public ObjectProperty<status> statusProperty() {
        return status;
    }

    public void setStatus(status status) {
        this.status.set(status);
    }
}