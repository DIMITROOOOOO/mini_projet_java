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

public class LoginController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    private UserDao userDao;

    @FXML
    public void initialize() {
        userDao = new UserDao();
    }

    @FXML
    private void handleLogin(javafx.event.ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Form Error", "Please enter both username and password.");
            return;
        }

        try {
            user authenticatedUser = userDao.authenticate(username, password);
            if (authenticatedUser != null) {
                showAlert(Alert.AlertType.INFORMATION, "Login Success", "Welcome, " + username + "!");
                navigateToDashboard();
            } else {
                showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid username or password.");
            }
        } catch (RuntimeException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Authentication error: " + e.getMessage());
        }
    }

    @FXML
    private void handleSignUpNavigation() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/signup.fxml"));
            Scene signupScene = new Scene(loader.load(), 1000, 600);
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(signupScene);
            stage.setTitle("Tunisair Gestion des Vols - Sign Up");
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Failed to load sign-up page: " + e.getMessage());
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