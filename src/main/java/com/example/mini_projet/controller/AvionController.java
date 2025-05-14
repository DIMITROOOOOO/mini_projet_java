package com.example.mini_projet.controller;

import com.example.mini_projet.model.avion;
import com.example.mini_projet.model.status;
import com.example.mini_projet.service.avionService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import java.util.logging.Logger;

public class AvionController {
    private static final Logger LOGGER = Logger.getLogger(AvionController.class.getName());

    @FXML private TableView<avion> aircraftTable;
    @FXML private TableColumn<avion, Integer> colId;
    @FXML private TableColumn<avion, String> colmodel;
    @FXML private TableColumn<avion, Integer> capacity;
    @FXML private TableColumn<avion, status> colsatuts;
    @FXML private TableColumn<avion, Void> actionCol;

    private avionService avionService = new avionService();
    private ObservableList<avion> avionList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // Bind columns to properties
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colmodel.setCellValueFactory(new PropertyValueFactory<>("model"));
        capacity.setCellValueFactory(new PropertyValueFactory<>("nbPlace"));
        colsatuts.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Action column with Edit and Delete buttons
        actionCol.setCellFactory(p -> new TableCell<>() {
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");
            private final HBox buttons = new HBox(5, editButton, deleteButton);

            {
                editButton.setOnAction(e -> {
                    avion avion = getTableView().getItems().get(getIndex());
                    handleEditAircraft(avion);
                });
                deleteButton.setOnAction(e -> {
                    avion avion = getTableView().getItems().get(getIndex());
                    handleDeleteAircraft(avion);
                });
                editButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
                deleteButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(buttons);
                }
            }
        });

        // Load initial data
        loadAvionData();
    }

    private void loadAvionData() {
        try {
            avionList.clear();
            avionList.addAll(avionService.getAllAvions());
            aircraftTable.setItems(avionList);
            LOGGER.info("Loaded " + avionList.size() + " avions");
        } catch (RuntimeException e) {
            LOGGER.severe("Failed to load avions: " + e.getMessage());
            showAlert("Error", "Failed to load aircraft data: " + e.getMessage());
        }
    }

    @FXML
    private void handleAddAircraft() {
        Dialog<avion> dialog = new Dialog<>();
        dialog.setTitle("Add Aircraft");
        dialog.setHeaderText("Enter aircraft details");

        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField modeleField = new TextField();
        modeleField.setPromptText("Model");
        TextField capaciteField = new TextField();
        capaciteField.setPromptText("Capacity");
        ComboBox<status> statutCombo = new ComboBox<>();
        statutCombo.setItems(FXCollections.observableArrayList(status.values()));
        statutCombo.setPromptText("Status");

        grid.add(new Label("Model:"), 0, 0);
        grid.add(modeleField, 1, 0);
        grid.add(new Label("Capacity:"), 0, 1);
        grid.add(capaciteField, 1, 1);
        grid.add(new Label("Status:"), 0, 2);
        grid.add(statutCombo, 1, 2);

        dialog.getDialogPane().setContent(grid);

        dialog.getDialogPane().lookupButton(addButtonType).setDisable(true);
        modeleField.textProperty().addListener((obs, old, newValue) ->
                dialog.getDialogPane().lookupButton(addButtonType).setDisable(
                        newValue.trim().isEmpty() || capaciteField.getText().trim().isEmpty() || statutCombo.getValue() == null));
        capaciteField.textProperty().addListener((obs, old, newValue) ->
                dialog.getDialogPane().lookupButton(addButtonType).setDisable(
                        modeleField.getText().trim().isEmpty() || newValue.trim().isEmpty() || statutCombo.getValue() == null));
        statutCombo.valueProperty().addListener((obs, old, newValue) ->
                dialog.getDialogPane().lookupButton(addButtonType).setDisable(
                        modeleField.getText().trim().isEmpty() || capaciteField.getText().trim().isEmpty() || newValue == null));

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                try {
                    int capacite = Integer.parseInt(capaciteField.getText());
                    return new avion(modeleField.getText(), capacite, statutCombo.getValue());
                } catch (NumberFormatException e) {
                    showAlert("Invalid Input", "Capacity must be a number.");
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(avion -> {
            try {
                avionService.addAvion(avion.getModel(), avion.getNbPlace(), avion.getStatus());
                loadAvionData(); // Reload data from database
                showSuccess("Success", "Aircraft added successfully");
            } catch (IllegalArgumentException e) {
                LOGGER.severe("Add aircraft failed: " + e.getMessage());
                showAlert("Error", "Failed to add aircraft: " + e.getMessage());
            } catch (RuntimeException e) {
                LOGGER.severe("Add aircraft failed: " + e.getMessage());
            }
        });
    }

    private void handleEditAircraft(avion avion) {
        Dialog<avion> dialog = new Dialog<>();
        dialog.setTitle("Edit Aircraft");
        dialog.setHeaderText("Edit aircraft details");

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField modeleField = new TextField(avion.getModel());
        modeleField.setPromptText("Model");
        TextField capaciteField = new TextField(String.valueOf(avion.getNbPlace()));
        capaciteField.setPromptText("Capacity");
        ComboBox<status> statutCombo = new ComboBox<>();
        statutCombo.setItems(FXCollections.observableArrayList(status.values()));
        statutCombo.setValue(avion.getStatus());
        statutCombo.setPromptText("Status");

        grid.add(new Label("Model:"), 0, 0);
        grid.add(modeleField, 1, 0);
        grid.add(new Label("Capacity:"), 0, 1);
        grid.add(capaciteField, 1, 1);
        grid.add(new Label("Status:"), 0, 2);
        grid.add(statutCombo, 1, 2);

        dialog.getDialogPane().setContent(grid);

        dialog.getDialogPane().lookupButton(saveButtonType).setDisable(false);
        modeleField.textProperty().addListener((obs, old, newValue) ->
                dialog.getDialogPane().lookupButton(saveButtonType).setDisable(
                        newValue.trim().isEmpty() || capaciteField.getText().trim().isEmpty() || statutCombo.getValue() == null));
        capaciteField.textProperty().addListener((obs, old, newValue) ->
                dialog.getDialogPane().lookupButton(saveButtonType).setDisable(
                        modeleField.getText().trim().isEmpty() || newValue.trim().isEmpty() || statutCombo.getValue() == null));
        statutCombo.valueProperty().addListener((obs, old, newValue) ->
                dialog.getDialogPane().lookupButton(saveButtonType).setDisable(
                        modeleField.getText().trim().isEmpty() || capaciteField.getText().trim().isEmpty() || newValue == null));

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                try {
                    int capacite = Integer.parseInt(capaciteField.getText());
                    // Create a new avion object to avoid modifying the original until confirmed
                    avion updatedAvion = new avion(modeleField.getText(), capacite, statutCombo.getValue());
                    updatedAvion.setId(avion.getId()); // Preserve the ID
                    return updatedAvion;
                } catch (NumberFormatException e) {
                    showAlert("Invalid Input", "Capacity must be a number.");
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(updatedAvion -> {
            try {
                avionService.editAvion(updatedAvion);
                // Instead of modifying the original avion object, reload the entire list
                loadAvionData();
                showSuccess("Success", "Aircraft updated successfully");
            } catch (IllegalArgumentException e) {
                LOGGER.severe("Edit aircraft failed: " + e.getMessage());
                showAlert("Error", "Failed to edit aircraft: " + e.getMessage());
            } catch (RuntimeException e) {
                LOGGER.severe("Edit aircraft failed: " + e.getMessage());
            }
        });
    }

    private void handleDeleteAircraft(avion avion) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete Aircraft");
        confirm.setHeaderText("Are you sure you want to delete " + avion.getModel() + "?");
        confirm.setContentText("This action cannot be undone.");

        if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            try {
                avionService.deleteAvion(avion.getId());
                loadAvionData(); // Reload data from database
                showSuccess("Success", "Aircraft deleted successfully");
            } catch (IllegalArgumentException e) {
                LOGGER.severe("Delete aircraft failed: " + e.getMessage());
                showAlert("Error", "Failed to delete aircraft: " + e.getMessage());
            } catch (RuntimeException e) {
                LOGGER.severe("Delete aircraft failed: " + e.getMessage());
            }
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccess(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}