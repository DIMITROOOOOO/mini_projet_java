package com.example.mini_projet.dao;

import com.example.mini_projet.model.avion;
import com.example.mini_projet.model.status;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AvionDao {
    public AvionDao() {
        laConnexion c = new laConnexion();
        if (laConnexion.seConnecter() == null) {
            c.setUser("root");
            c.setPassWord("");
        }
    }

    public avion findById(int id) {
        String sql = "SELECT * FROM avion WHERE id = ?";
        try (Connection cn = laConnexion.seConnecter();
             PreparedStatement stmt = cn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String statutStr = rs.getString("statut");
                status statut = parseStatus(statutStr);
                avion avion = new avion(
                        rs.getString("modele"),
                        rs.getInt("capacite"),
                        statut
                );
                avion.setId(rs.getInt("id"));
                return avion;
            }
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
        return null;
    }

    public void ajouteravion(avion avion) {
        String sql = "INSERT INTO avion (modele, capacite, statut) VALUES (?, ?, ?)";
        try (Connection cn = laConnexion.seConnecter();
             PreparedStatement pst = cn.prepareStatement(sql)) {
            pst.setString(1, avion.getModel());
            pst.setInt(2, avion.getNbPlace());
            pst.setString(3, avion.getStatus() != null ? avion.getStatus().name() : null);
            int n = pst.executeUpdate();
            if (n >= 1) {
                System.out.println("ajout avec succes");
            }
        } catch (SQLException e) {
            System.out.println("erreur: " + e.getMessage());
        }
    }

    public List<avion> findAll() {
        List<avion> avions = new ArrayList<>();
        String sql = "SELECT * FROM avion";
        try (Connection conn = laConnexion.seConnecter();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                String statutStr = rs.getString("statut");
                status statut = parseStatus(statutStr);
                avion avion = new avion(
                        rs.getString("modele"),
                        rs.getInt("capacite"),
                        statut
                );
                avion.setId(rs.getInt("id"));
                avions.add(avion);
            }
        } catch (SQLException e) {
            System.err.println("Error finding all avions: " + e.getMessage());
        }
        return avions;
    }

    private status parseStatus(String statutStr) {
        if (statutStr == null) {
            return null;
        }
        try {
            return status.valueOf(statutStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid status value: " + statutStr + ". Using null as fallback.");
            return null;
        }
    }

    public void supprimeravion(int id) {
        String sql = "DELETE FROM avion WHERE id = ?";
        try (Connection cn = laConnexion.seConnecter();
             PreparedStatement pst = cn.prepareStatement(sql)) {
            pst.setInt(1, id);
            int n = pst.executeUpdate();
            if (n >= 1) {
                System.out.println("suppression avec succes");
            } else {
                System.err.println("No avion found with ID: " + id);
            }
        } catch (SQLException e) {
            System.out.println("erreur: " + e.getMessage());
        }
    }

    public void editavion(avion avion) {
        String sql = "UPDATE avion SET modele = ?, capacite = ?, statut = ? WHERE id = ?";
        try (Connection cn = laConnexion.seConnecter();
             PreparedStatement pst = cn.prepareStatement(sql)) {
            pst.setString(1, avion.getModel());
            pst.setInt(2, avion.getNbPlace());
            pst.setString(3, avion.getStatus() != null ? avion.getStatus().name() : null);
            pst.setInt(4, avion.getId());
            int n = pst.executeUpdate();
            if (n >= 1) {
                System.out.println("modification avec succes");
            } else {
                System.err.println("No avion found with ID: " + avion.getId());
            }
        } catch (SQLException e) {
            System.out.println("erreur: " + e.getMessage());
        }
    }
}