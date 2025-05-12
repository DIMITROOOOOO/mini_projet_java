package com.example.mini_projet.dao;

import com.example.mini_projet.model.user;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDao {
    public void ajouterUser(user user) {
        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
        try (Connection cn = laConnexion.seConnecter();
             PreparedStatement pst = cn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
            pst.setString(1, user.getUsername());
            pst.setString(2, hashedPassword);
            pst.executeUpdate();
            ResultSet rs = pst.getGeneratedKeys();
            if (rs.next()) {
                user.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to add user: " + e.getMessage(), e);
        }
    }

    public user findById(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection cn = laConnexion.seConnecter();
             PreparedStatement stmt = cn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new user(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding user: " + e.getMessage(), e);
        }
        return null;
    }

    public user findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection cn = laConnexion.seConnecter();
             PreparedStatement stmt = cn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new user(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding user by username: " + e.getMessage(), e);
        }
        return null;
    }

    public user authenticate(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection cn = laConnexion.seConnecter();
             PreparedStatement stmt = cn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String hashedPassword = rs.getString("password");
                if (BCrypt.checkpw(password, hashedPassword)) {
                    return new user(
                            rs.getInt("id"),
                            rs.getString("username"),
                            hashedPassword
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error authenticating user: " + e.getMessage(), e);
        }
        return null;
    }

    public List<user> findAll() {
        List<user> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (Connection cn = laConnexion.seConnecter();
             PreparedStatement stmt = cn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                users.add(new user(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all users: " + e.getMessage(), e);
        }
        return users;
    }

    public void updateUser(user user) {
        String sql = "UPDATE users SET username = ?, password = ? WHERE id = ?";
        try (Connection cn = laConnexion.seConnecter();
             PreparedStatement stmt = cn.prepareStatement(sql)) {
            String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
            stmt.setString(1, user.getUsername());
            stmt.setString(2, hashedPassword);
            stmt.setInt(3, user.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update user: " + e.getMessage(), e);
        }
    }

    public void deleteUser(int id) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection cn = laConnexion.seConnecter();
             PreparedStatement stmt = cn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete user: " + e.getMessage(), e);
        }
    }
}