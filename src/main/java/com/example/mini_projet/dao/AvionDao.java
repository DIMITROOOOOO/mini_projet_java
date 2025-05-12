package com.example.mini_projet.dao;

import com.example.mini_projet.model.avion;
import com.example.mini_projet.model.equipe;
import com.example.mini_projet.model.status;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AvionDao {
    public AvionDao() {
        // Ensure credentials are set
        laConnexion c = new laConnexion();
        if (laConnexion.seConnecter() == null) {
            c.setUser("root");
            c.setPassWord("");
        }
    }
    public avion findById(int id) {
        String sql = "SELECT * FROM avion WHERE id = ?";
        try ( Connection cn = laConnexion.seConnecter();
             PreparedStatement stmt = cn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String statutStr = rs.getString("statut");
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
    public  void  ajouteravion(avion avion) {
        String sql = "insert into avion(modele,capacite,statut) values(?,?,?)";

        try ( Connection cn = laConnexion.seConnecter();){
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
    public List<avion> findAll() {
        List<avion> avions = new ArrayList<>();
        String sql = "SELECT * FROM avion";
        try (Connection conn = laConnexion.seConnecter();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                String statusStr = rs.getString("statut");
                status status = statusStr != null ? avion.status.valueOf(statusStr) : null;
                avion avion = new avion(
                        rs.getString("modele"),
                        rs.getInt("capacite"),
                        status
                );
                avion.setId(rs.getInt("id"));
                avions.add(avion);
            }
        } catch (SQLException e) {
            System.err.println("Error finding all avions: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid status value: " + e.getMessage());
        }
        return avions;
    }
    public void supprimeravion(int id) {

        String sql = "delete from avion where id=?";
        try ( Connection cn = laConnexion.seConnecter();){
            PreparedStatement pst = cn.prepareStatement(sql);
            pst.setInt(1, id);
            int n = pst.executeUpdate();
            if (n >= 1)
                System.out.println("suppression avec succes");
            else {
                System.err.println("No avion found with ID: " + id);
            }
        } catch (SQLException e) {
            System.out.println("erreur" + e.getMessage());
        }

    }
    public void editavion(avion avion){
        String sql="update avion set modele=?,capacite=?,statut=? where id=?";
        try ( Connection cn = laConnexion.seConnecter();){
            PreparedStatement pst=cn.prepareStatement(sql);
            pst.setString(1,avion.getModel());
            pst.setInt(2,avion.getNbPlace());
            pst.setString(3,avion.getStatus().name());
            pst.setInt(4,avion.getId());
            int n= pst.executeUpdate();
            if (n>=1)
                System.out.println("modification avec succes");
            else {
                System.err.println("No avion found with ID: " + avion.getId());
            }

        }catch (SQLException e){
            System.out.println("erreur"+e.getMessage());
        }
    }

}
