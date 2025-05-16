package com.example.mini_projet.controller;

import com.example.mini_projet.dao.VolDao;
import com.example.mini_projet.model.avion;
import com.example.mini_projet.model.equipe;
import com.example.mini_projet.model.status;
import com.example.mini_projet.model.vol;
import com.example.mini_projet.service.avionService;
import com.example.mini_projet.service.equipeService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.logging.Logger;

public class FlightsController {
    private static final Logger LOGGER = Logger.getLogger(FlightsController.class.getName());
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    @FXML private TableView<vol> flightsTable;
    @FXML private TableColumn<vol, Integer> colFlightNumber;
    @FXML private TableColumn<vol, String> colDestination;
    @FXML private TableColumn<vol, String> colAircraft;
    @FXML private TableColumn<vol, String> colCrew;
    @FXML private TableColumn<vol, String> colStatus;

    private avionService avionService = new avionService();
    private equipeService equipeService = new equipeService();
    private VolDao volDao = new VolDao();
    private ObservableList<vol> flightList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        colFlightNumber.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDestination.setCellValueFactory(new PropertyValueFactory<>("destination"));
        colAircraft.setCellValueFactory(cellData -> {
            avion avion = cellData.getValue().getAvion();
            return new javafx.beans.property.SimpleStringProperty(
                    avion != null ? avion.getModel() + " (" + avion.getNbPlace() + ")" : "N/A");
        });
        colCrew.setCellValueFactory(cellData -> {
            equipe equipe = cellData.getValue().getEquipe();
            return new javafx.beans.property.SimpleStringProperty(equipe != null ? "Crew " + equipe.getId() : "N/A");
        });
        colStatus.setCellValueFactory(cellData -> {
            avion avion = cellData.getValue().getAvion();
            return new javafx.beans.property.SimpleStringProperty(
                    avion != null ? avion.getStatus().toString() : "N/A");
        });

        loadFlightData();
    }

    private void loadFlightData() {
        try {
            flightList.clear();
            flightList.addAll(volDao.findAll());
            flightsTable.setItems(flightList);
            LOGGER.info("Loaded " + flightList.size() + " flights");
        } catch (RuntimeException e) {
            LOGGER.severe("Failed to load flights: " + e.getMessage());
            showAlert("Error", "Failed to load flight data: " + e.getMessage());
        }
    }

    @FXML
    public void handleAddFlight(ActionEvent actionEvent) {
        Dialog<vol> dialog = new Dialog<>();
        dialog.setTitle("Add Flight");
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getStyleClass().add("dialog-card");

        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialogPane.getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        VBox content = new VBox(15);
        content.getStyleClass().add("dialog-content");

        Label headerLabel = new Label("Schedule New Flight");
        headerLabel.getStyleClass().add("dialog-header");

        TextField destinationField = new TextField();
        destinationField.setPromptText("Destination (e.g., Paris)");
        destinationField.getStyleClass().add("input-field");
        destinationField.setPrefWidth(400);

        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("Select Departure Date");
        datePicker.getStyleClass().add("input-field");
        datePicker.setPrefWidth(400);
        datePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(LocalDate.now()));
            }
        });

        TextField timeField = new TextField();
        timeField.setPromptText("Time (HH:mm, e.g., 14:30)");
        timeField.getStyleClass().add("input-field");
        timeField.setPrefWidth(400);

        ComboBox<avion> aircraftCombo = new ComboBox<>();
        aircraftCombo.setPromptText("Select Aircraft");
        aircraftCombo.getStyleClass().add("input-field");
        aircraftCombo.setPrefWidth(400);
        aircraftCombo.setCellFactory(lv -> new ListCell<avion>() {
            @Override
            protected void updateItem(avion item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getModel() + " (" + item.getNbPlace() + " seats)");
            }
        });
        aircraftCombo.setButtonCell(new ListCell<avion>() {
            @Override
            protected void updateItem(avion item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getModel() + " (" + item.getNbPlace() + " seats)");
            }
        });
        loadAvailableAircraft(aircraftCombo);

        ComboBox<equipe> crewCombo = new ComboBox<>();
        crewCombo.setPromptText("Select Crew");
        crewCombo.getStyleClass().add("input-field");
        crewCombo.setPrefWidth(400);
        crewCombo.setCellFactory(lv -> new ListCell<equipe>() {
            @Override
            protected void updateItem(equipe item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : "Crew " + item.getId() + " (2 Pilots, 2 Attendants, 2 Stewards, 1 Mechanic)");
            }
        });
        crewCombo.setButtonCell(new ListCell<equipe>() {
            @Override
            protected void updateItem(equipe item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : "Crew " + item.getId() + " (2 Pilots, 2 Attendants, 2 Stewards, 1 Mechanic)");
            }
        });
        loadAvailableCrews(crewCombo);

        Label destinationLabel = new Label("Destination");
        destinationLabel.getStyleClass().add("input-label");
        Label dateLabel = new Label("Departure Date");
        dateLabel.getStyleClass().add("input-label");
        Label timeLabel = new Label("Departure Time");
        timeLabel.getStyleClass().add("input-label");
        Label aircraftLabel = new Label("Aircraft");
        aircraftLabel.getStyleClass().add("input-label");
        Label crewLabel = new Label("Crew");
        crewLabel.getStyleClass().add("input-label");

        content.getChildren().addAll(
                headerLabel,
                destinationLabel,
                destinationField,
                dateLabel,
                datePicker,
                timeLabel,
                timeField,
                aircraftLabel,
                aircraftCombo,
                crewLabel,
                crewCombo
        );

        dialogPane.setContent(content);

        dialogPane.lookupButton(addButtonType).setDisable(true);
        destinationField.textProperty().addListener((obs, old, newValue) ->
                updateAddButtonState(dialogPane, addButtonType, destinationField, datePicker, timeField, aircraftCombo, crewCombo));
        datePicker.valueProperty().addListener((obs, old, newValue) ->
                updateAddButtonState(dialogPane, addButtonType, destinationField, datePicker, timeField, aircraftCombo, crewCombo));
        timeField.textProperty().addListener((obs, old, newValue) ->
                updateAddButtonState(dialogPane, addButtonType, destinationField, datePicker, timeField, aircraftCombo, crewCombo));
        aircraftCombo.valueProperty().addListener((obs, old, newValue) ->
                updateAddButtonState(dialogPane, addButtonType, destinationField, datePicker, timeField, aircraftCombo, crewCombo));
        crewCombo.valueProperty().addListener((obs, old, newValue) ->
                updateAddButtonState(dialogPane, addButtonType, destinationField, datePicker, timeField, aircraftCombo, crewCombo));

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                try {
                    String destination = destinationField.getText().trim();
                    LocalDate date = datePicker.getValue();
                    LocalTime time = LocalTime.parse(timeField.getText().trim(), TIME_FORMATTER);
                    avion selectedAvion = aircraftCombo.getValue();
                    equipe selectedEquipe = crewCombo.getValue();

                    if (destination.isEmpty()) {
                        showAlert("Invalid Input", "Destination is required.");
                        return null;
                    }
                    if (date == null || time == null) {
                        showAlert("Invalid Input", "Valid date and time are required.");
                        return null;
                    }
                    LocalDateTime departureDateTime = LocalDateTime.of(date, time);
                    if (departureDateTime.isBefore(LocalDateTime.now())) {
                        showAlert("Invalid Input", "Flight cannot be scheduled in the past.");
                        return null;
                    }
                    if (selectedAvion == null || selectedEquipe == null) {
                        showAlert("Invalid Input", "Aircraft and crew are required.");
                        return null;
                    }

                    vol newVol = new vol(0, destination, date, selectedEquipe, selectedAvion);
                    return newVol;
                } catch (DateTimeParseException e) {
                    showAlert("Invalid Input", "Time must be in HH:mm format (e.g., 14:30).");
                } catch (Exception e) {
                    showAlert("Invalid Input", "Please check all fields: " + e.getMessage());
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(newVol -> {
            try {
                // Add flight to database
                VolDao.ajouterVol(newVol);

                // Update aircraft status to INDISPONIBLE
                avion updatedAvion = newVol.getAvion();
                updatedAvion.setStatus(status.OCUPE);
                avionService.editAvion(updatedAvion);

                // Update crew status to unavailable
                equipe updatedEquipe = newVol.getEquipe();
                equipeService.setDisponible(updatedEquipe.getId(), false);

                loadFlightData();
                showSuccess("Success", "Flight added successfully");
            } catch (IllegalStateException e) {
                LOGGER.severe("Add flight failed: " + e.getMessage());
                showAlert("Error", "Failed to add flight: " + e.getMessage());
            }
        });
    }

    private void loadAvailableAircraft(ComboBox<avion> aircraftCombo) {
        try {
            List<avion> availableAircraft = avionService.getAllAvions()
                    .stream()
                    .filter(a -> a.getStatus() == status.DISPONIBLE)
                    .toList();
            aircraftCombo.setItems(FXCollections.observableArrayList(availableAircraft));
        } catch (RuntimeException e) {
            LOGGER.severe("Failed to load available aircraft: " + e.getMessage());
            showAlert("Error", "Failed to load aircraft data: " + e.getMessage());
        }
    }

    private void loadAvailableCrews(ComboBox<equipe> crewCombo) {
        try {
            List<equipe> availableCrews = equipeService.getAllEquipes()
                    .stream()
                    .filter(equipe::isDisponible)
                    .toList();
            crewCombo.setItems(FXCollections.observableArrayList(availableCrews));
        } catch (RuntimeException e) {
            LOGGER.severe("Failed to load available crews: " + e.getMessage());
            showAlert("Error", "Failed to load crew data: " + e.getMessage());
        }
    }

    private void updateAddButtonState(DialogPane dialogPane, ButtonType addButtonType,
                                      TextField destinationField, DatePicker datePicker, TextField timeField,
                                      ComboBox<avion> aircraftCombo, ComboBox<equipe> crewCombo) {
        boolean isValidTime = true;
        try {
            if (!timeField.getText().trim().isEmpty()) {
                LocalTime.parse(timeField.getText().trim(), TIME_FORMATTER);
            }
        } catch (DateTimeParseException e) {
            isValidTime = false;
        }
        dialogPane.lookupButton(addButtonType).setDisable(
                destinationField.getText().trim().isEmpty() ||
                        datePicker.getValue() == null ||
                        timeField.getText().trim().isEmpty() ||
                        !isValidTime ||
                        aircraftCombo.getValue() == null ||
                        crewCombo.getValue() == null
        );
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