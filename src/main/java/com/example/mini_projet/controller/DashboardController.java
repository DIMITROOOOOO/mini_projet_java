package com.example.mini_projet.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import java.io.IOException;

public class DashboardController {

    @FXML
    private StackPane contentArea;

    @FXML
    private void initialize() {
        // Load the Flights page by default
       // loadPage("/com/example/mini_projet/Flights.fxml");
    }

    @FXML
    private void goToFlights() {
        loadPage("/view/flights.fxml");
    }

    @FXML
    private void goToAircraft() {
        loadPage("/view/aircraft.fxml");
    }

    @FXML
    private void goToCrew() {
        loadPage("/view/crew.fxml");
    }

    @FXML
    private void goToSearch() {
        loadPage("/view/search.fxml");
    }

    @FXML
    private void handleLogout() {
        try {
            Parent loginPage = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
            contentArea.getScene().setRoot(loginPage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadPage(String fxmlPath) {
        try {
            Parent page = FXMLLoader.load(getClass().getResource(fxmlPath));
            contentArea.getChildren().clear();
            contentArea.getChildren().add(page);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}