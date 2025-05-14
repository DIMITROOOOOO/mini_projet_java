package com.example.mini_projet;

import com.example.mini_projet.dao.laConnexion;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class HelloApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        // Verify FXML file location
        URL fxmlLocation = getClass().getResource("/view/login.fxml");
        if (fxmlLocation == null) {
            throw new IOException("Cannot find login.fxml at path: /view/login.fxml");
        }

        // Verify CSS file location
        URL cssLocation = getClass().getResource("/view/style.css");
        if (cssLocation == null) {
            throw new IOException("Cannot find style.css at path: /view/style.css");
        }

        FXMLLoader loader = new FXMLLoader(fxmlLocation);
        Scene scene = new Scene(loader.load(), 900, 600);
        scene.getStylesheets().add(cssLocation.toExternalForm());
        primaryStage.setTitle("Tunisair Aircraft Management");
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setOnCloseRequest(event -> {
            laConnexion.closeConnection();
            Platform.exit();
        });
    }

    public static void main(String[] args) {
        try {
            laConnexion cn = new laConnexion();
            cn.setUser("root");
            cn.setPassWord("");
            cn.seConnecter();
            launch(args);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}