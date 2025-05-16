package com.example.mini_projet.controller;

import com.example.mini_projet.model.avion;
import com.example.mini_projet.model.status;
import com.example.mini_projet.service.avionService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.util.Map;
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
    private ObservableList<String> modelList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colmodel.setCellValueFactory(new PropertyValueFactory<>("model"));
        capacity.setCellValueFactory(new PropertyValueFactory<>("nbPlace"));
        colsatuts.setCellValueFactory(new PropertyValueFactory<>("status"));

        actionCol.setCellFactory(p -> new TableCell<>() {
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");
            private final HBox buttons = new HBox(12, editButton, deleteButton);

            {
                editButton.getStyleClass().add("edit-button");
                deleteButton.getStyleClass().add("delete-button");
                editButton.setOnAction(e -> {
                    avion avion = getTableView().getItems().get(getIndex());
                    handleEditAircraft(avion);
                });
                deleteButton.setOnAction(e -> {
                    avion avion = getTableView().getItems().get(getIndex());
                    handleDeleteAircraft(avion);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : buttons);
            }
        });

        modelList.addAll(avionService.getModelList());
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
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getStyleClass().add("dialog-card");

        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialogPane.getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        VBox content = new VBox(15);
        content.getStyleClass().add("dialog-content");

        Label headerLabel = new Label("Add New Aircraft");
        headerLabel.getStyleClass().add("dialog-header");

        ComboBox<String> modelCombo = new ComboBox<>();
        modelCombo.setItems(modelList);
        modelCombo.setPromptText("Select Model");
        modelCombo.getStyleClass().add("input-field");
        modelCombo.setPrefWidth(400);

        TextField capaciteField = new TextField();
        capaciteField.setPromptText("Capacity");
        capaciteField.setEditable(false);
        capaciteField.getStyleClass().add("input-field");
        capaciteField.setPrefWidth(400);

        ComboBox<status> statutCombo = new ComboBox<>();
        statutCombo.setItems(FXCollections.observableArrayList(status.values()));
        statutCombo.setPromptText("Select Status");
        statutCombo.getStyleClass().add("input-field");
        statutCombo.setPrefWidth(400);

        Map<String, Integer> modelCapacityMap = avionService.getModelCapacityMap();
        modelCombo.valueProperty().addListener((obs, oldValue, newValue) -> {
            capaciteField.setText(newValue != null && modelCapacityMap.containsKey(newValue) ?
                    String.valueOf(modelCapacityMap.get(newValue)) : "");
        });

        Label modelLabel = new Label("Model");
        modelLabel.getStyleClass().add("input-label");
        Label capacityLabel = new Label("Capacity");
        capacityLabel.getStyleClass().add("input-label");
        Label statusLabel = new Label("Status");
        statusLabel.getStyleClass().add("input-label");

        content.getChildren().addAll(
                headerLabel,
                modelLabel,
                modelCombo,
                capacityLabel,
                capaciteField,
                statusLabel,
                statutCombo
        );

        dialogPane.setContent(content);

        dialogPane.lookupButton(addButtonType).setDisable(true);
        modelCombo.valueProperty().addListener((obs, old, newValue) ->
                dialogPane.lookupButton(addButtonType).setDisable(
                        newValue == null || capaciteField.getText().trim().isEmpty() || statutCombo.getValue() == null));
        statutCombo.valueProperty().addListener((obs, old, newValue) ->
                dialogPane.lookupButton(addButtonType).setDisable(
                        modelCombo.getValue() == null || capaciteField.getText().trim().isEmpty() || newValue == null));

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                try {
                    String selectedModel = modelCombo.getValue();
                    int capacite = Integer.parseInt(capaciteField.getText());
                    status selectedStatus = statutCombo.getValue();
                    if (selectedModel == null || selectedStatus == null) {
                        showAlert("Invalid Input", "All fields are required.");
                        return null;
                    }
                    return new avion(selectedModel, capacite, selectedStatus);
                } catch (NumberFormatException e) {
                    showAlert("Invalid Input", "Capacity must be a number.");
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(avion -> {
            try {
                avionService.addAvion(avion.getModel(), avion.getNbPlace(), avion.getStatus());
                loadAvionData();
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
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getStyleClass().add("dialog-card");

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialogPane.getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        VBox content = new VBox(15);
        content.getStyleClass().add("dialog-content");

        Label headerLabel = new Label("Edit Aircraft Details");
        headerLabel.getStyleClass().add("dialog-header");

        TextField modeleField = new TextField(avion.getModel());
        modeleField.setPromptText("Model");
        modeleField.getStyleClass().add("input-field");
        modeleField.setPrefWidth(400);

        TextField capaciteField = new TextField(String.valueOf(avion.getNbPlace()));
        capaciteField.setPromptText("Capacity");
        capaciteField.getStyleClass().add("input-field");
        capaciteField.setPrefWidth(400);

        ComboBox<status> statutCombo = new ComboBox<>();
        statutCombo.setItems(FXCollections.observableArrayList(status.values()));
        statutCombo.setValue(avion.getStatus());
        statutCombo.setPromptText("Select Status");
        statutCombo.getStyleClass().add("input-field");
        statutCombo.setPrefWidth(400);

        Label modelLabel = new Label("Model");
        modelLabel.getStyleClass().add("input-label");
        Label capacityLabel = new Label("Capacity");
        capacityLabel.getStyleClass().add("input-label");
        Label statusLabel = new Label("Status");
        statusLabel.getStyleClass().add("input-label");

        content.getChildren().addAll(
                headerLabel,
                modelLabel,
                modeleField,
                capacityLabel,
                capaciteField,
                statusLabel,
                statutCombo
        );

        dialogPane.setContent(content);

        dialogPane.lookupButton(saveButtonType).setDisable(false);
        modeleField.textProperty().addListener((obs, old, newValue) ->
                dialogPane.lookupButton(saveButtonType).setDisable(
                        newValue.trim().isEmpty() || capaciteField.getText().trim().isEmpty() || statutCombo.getValue() == null));
        capaciteField.textProperty().addListener((obs, old, newValue) ->
                dialogPane.lookupButton(saveButtonType).setDisable(
                        modeleField.getText().trim().isEmpty() || newValue.trim().isEmpty() || statutCombo.getValue() == null));
        statutCombo.valueProperty().addListener((obs, old, newValue) ->
                dialogPane.lookupButton(saveButtonType).setDisable(
                        modeleField.getText().trim().isEmpty() || capaciteField.getText().trim().isEmpty() || newValue == null));

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                try {
                    String selectedModel = modeleField.getText();
                    int capacite = Integer.parseInt(capaciteField.getText());
                    status selectedStatus = statutCombo.getValue();
                    if (selectedModel.trim().isEmpty() || selectedStatus == null) {
                        showAlert("Invalid Input", "All fields are required.");
                        return null;
                    }
                    return new avion(avion.getId(), selectedModel, capacite, selectedStatus);
                } catch (NumberFormatException e) {
                    showAlert("Invalid Input", "Capacity must be a number.");
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(updatedAvion -> {
            try {
                avionService.editAvion(updatedAvion);
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
        DialogPane dialogPane = confirm.getDialogPane();
        dialogPane.getStyleClass().add("dialog-card");

        if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            try {
                avionService.deleteAvion(avion.getId());
                loadAvionData();
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
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStyleClass().add("dialog-card");
        alert.showAndWait();
    }

    private void showSuccess(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStyleClass().add("dialog-card");
        alert.showAndWait();
    }
}