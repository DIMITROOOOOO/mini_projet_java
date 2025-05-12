package com.example.mini_projet.controller;

import com.example.mini_projet.dao.UserDao;
import com.example.mini_projet.model.user;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class SignUpController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;

    private UserDao userDao;

    @FXML
    public void initialize() {
        userDao = new UserDao();
    }

    @FXML
    private void handleSignUp() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Form Error", "Please fill in all fields.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert(Alert.AlertType.ERROR, "Password Mismatch", "Passwords do not match.");
            return;
        }

        try {
            user existingUser = userDao.findByUsername(username);
            if (existingUser != null) {
                showAlert(Alert.AlertType.ERROR, "Username Taken", "This username is already taken.");
                return;
            }

            user newUser = new user(0, username, password);
            userDao.ajouterUser(newUser);
            showAlert(Alert.AlertType.INFORMATION, "Sign Up Success", "Account created for " + username + "!");
            navigateToDashboard();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to create account: " + e.getMessage());
        }
    }

    @FXML
    private void handleBackToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login.fxml"));
            Scene loginScene = new Scene(loader.load(), 1000, 600);
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(loginScene);
            stage.setTitle("Tunisair Gestion des Vols - Login");
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Failed to return to login: " + e.getMessage());
        }
    }

    private void navigateToDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/main_layout.fxml"));
            Scene dashboardScene = new Scene(loader.load(), 1000, 600);
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(dashboardScene);
            stage.setTitle("Tunisair Gestion des Vols - Dashboard");
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Failed to load dashboard: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}