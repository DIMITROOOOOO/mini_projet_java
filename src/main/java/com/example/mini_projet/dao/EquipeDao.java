package com.example.mini_projet.dao;

import com.example.mini_projet.model.equipe;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EquipeDao {
    public equipe findById(int id) {
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
    public  void  ajouterEquipe(equipe equipe) {
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

}
