package com.example.mini_projet.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardController {

    @FXML
    private void goToAircraft() {
        navigateTo("/view/aircraft.fxml", "Tunisair Aircraft Management");
    }

    @FXML
    private void goToFlights() {
        navigateTo("/view/flights.fxml", "Tunisair Flights Management");
    }

    @FXML
    private void goToCrew() {
        navigateTo("/view/crew.fxml", "Tunisair Crew Management");
    }

    @FXML
    private void goToSearch() {
        navigateTo("/view/search.fxml", "Tunisair Flight Search");
    }

    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login.fxml"));
            Scene loginScene = new Scene(loader.load(), 1000, 600);
            Stage stage = (Stage) spacer.getScene().getWindow();
            stage.setScene(loginScene);
            stage.setTitle("Tunisair Gestion des Vols - Login");
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Failed to return to login: " + e.getMessage());
        }
    }

    @FXML
    private Region spacer; // Matches fx:id in dashboard.fxml

    private void navigateTo(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Scene scene = new Scene(loader.load(), 1000, 600);
            Stage stage = (Stage) spacer.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle(title);
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Failed to load " + title + ": " + e.getMessage());
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