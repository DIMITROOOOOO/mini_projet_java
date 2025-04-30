package com.example.mini_projet.model;

public class equipe {
    private int id;
    private String nom;
    private String prenom;
    private String role;
    private  boolean disponible;
    public equipe(int id, String nom, String prenom, String role, boolean disponible) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.role = role;
        this.disponible = disponible;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }
}
