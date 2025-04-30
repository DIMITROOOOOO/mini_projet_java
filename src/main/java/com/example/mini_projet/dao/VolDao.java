package com.example.mini_projet.dao;

import com.example.mini_projet.model.avion;
import com.example.mini_projet.model.equipe;
import com.example.mini_projet.model.vol;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class VolDao {
    private  AvionDao avionDao =new AvionDao();
    private  EquipeDao equipeDao=new EquipeDao();

    public static void ajouterVol(vol vol) {
        String sql = "INSERT INTO vol (destination, dateDepart, equipe_id, avion_id) VALUES (?, ?, ?, ?)";
        try (Connection con = laConnexion.seConnecter();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, vol.getDestination());
            stmt.setString(2, vol.getDateDepart().toString());
            stmt.setInt(3, vol.getEquipe().getId()); // Use setInt for integer equipe_id
            stmt.setInt(4, vol.getAvion().getId());  // Use setInt for integer avion_id
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error adding flight: " + e.getMessage());
            throw new RuntimeException("Failed to add flight", e);
        }
    }
    public List<vol> trouverVol(String destination, LocalDate date){
        List<vol> lesvols=new ArrayList<>();
        String sql="SELECT * FROM vol WHERE destination like ? AND dateDepart like ?";
        try(Connection con = laConnexion.seConnecter();
            PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1,destination);
            stmt.setString(2,date.toString());
            ResultSet rs=stmt.executeQuery();
            while (rs.next()){
                avion avion=avionDao.findById(rs.getInt("avion_id"));
                equipe eq=equipeDao.findById(rs.getInt("equipe_id"));
                vol vol=new vol(rs.getInt("id")
                        ,rs.getString("destination"),
                        LocalDate.parse(rs.getString("dateDepart"))
                        ,eq,avion);
                lesvols.add(vol);

            }



        } catch (SQLException e) {
            System.out.println("erreur"+e.getMessage());
        }

        return lesvols;
    }
}
