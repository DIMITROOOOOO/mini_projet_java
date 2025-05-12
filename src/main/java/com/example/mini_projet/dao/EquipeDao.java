package com.example.mini_projet.dao;

import com.example.mini_projet.model.equipe;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EquipeDao {
    public static equipe findById(int id) {
        String sql = "SELECT * FROM equipe WHERE id = ?";
        try (Connection conn = laConnexion.seConnecter();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new equipe(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("prenom"),
                        rs.getString("role"),
                        rs.getBoolean("disponible")

                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static void ajouterEquipe(equipe equipe) {
        Connection cn = laConnexion.seConnecter();
        String sql = "insert into equipe(id,nom,prenom,role,disponible) values(?,?,?,?,?)";
        try {
            PreparedStatement pst = cn.prepareStatement(sql);
            pst.setInt(1, equipe.getId());
            pst.setString(2, equipe.getNom());
            pst.setString(3, equipe.getPrenom());
            pst.setString(4, equipe.getRole());
            pst.setBoolean(5, equipe.isDisponible());
            int n = pst.executeUpdate();
            if (n >= 1)
                System.out.println("ajout avec succes");
        } catch (SQLException e) {
            System.out.println("erreur" + e.getMessage());
        }
    }
    public List<equipe> findAll() {
        List<equipe> equipes = new ArrayList<>();
        String sql = "SELECT * FROM equipe";
        try (Connection cn = laConnexion.seConnecter();
             PreparedStatement stmt = cn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                equipes.add(new equipe(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("role"),
                        rs.getBoolean("disponible")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all equipes: " + e.getMessage(), e);
        }
        return equipes;
    }

    public List<equipe> findAvailable() {
        List<equipe> equipes = new ArrayList<>();
        String sql = "SELECT * FROM equipe WHERE disponible = ?";
        try (Connection cn = laConnexion.seConnecter();
             PreparedStatement stmt = cn.prepareStatement(sql)) {
            stmt.setBoolean(1, true);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                equipes.add(new equipe(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("role"),
                        rs.getBoolean("disponible")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding available equipes: " + e.getMessage(), e);
        }
        return equipes;
    }

    public void updateEquipe(equipe equipe) {
        String sql = "UPDATE equipe SET nom = ?, prenom = ?, role = ?, disponible = ? WHERE id = ?";
        try (Connection cn = laConnexion.seConnecter();
             PreparedStatement pst = cn.prepareStatement(sql)) {
            pst.setString(1, equipe.getNom());
            pst.setString(2, equipe.getPrenom());
            pst.setString(3, equipe.getRole());
            pst.setBoolean(4, equipe.isDisponible());
            pst.setInt(5, equipe.getId());
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update equipe: " + e.getMessage(), e);
        }
    }

    public void deleteEquipe(int id) {
        String sql = "DELETE FROM equipe WHERE id = ?";
        try (Connection cn = laConnexion.seConnecter();
             PreparedStatement pst = cn.prepareStatement(sql)) {
            pst.setInt(1, id);
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete equipe: " + e.getMessage(), e);
        }
    }
}
