package com.example.mini_projet.dao;

import com.example.mini_projet.model.avion;
import com.example.mini_projet.model.equipe;
import com.example.mini_projet.model.status;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AvionDao {
    public static avion findById(int id) {
        String sql = "SELECT * FROM avion WHERE id = ?";
        try (Connection con = laConnexion.seConnecter();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String statutStr = rs.getString("status");
                avion status;
                status statut = statutStr != null ? avion.status.valueOf(statutStr) : null;
                return new avion(
                        rs.getString("modele"),
                        rs.getInt("capacite"),
                        statut
                );
            }
        } catch (SQLException e) {
            System.err.println("Error : " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("invalid statut: " + e.getMessage());
        }
        return null;
    }
    public static void  ajouterEquipe(avion avion) {
        Connection cn = laConnexion.seConnecter();
        String sql = "insert into avion(modele,capacite,statut) values(?,?,?)";

        try {
            PreparedStatement pst = cn.prepareStatement(sql);
            pst.setString(1, avion.getModel());
            pst.setInt(2, avion.getNbPlace());
           pst.setString(3, avion.getStatus().name());
            int n = pst.executeUpdate();
            if (n >= 1)
                System.out.println("ajout avec succes");
        } catch (SQLException e) {
            System.out.println("erreur" + e.getMessage());
        }
    }
}
