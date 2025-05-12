package com.example.mini_projet;

import com.example.mini_projet.dao.laConnexion;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class HelloApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        URL fxmlLocation = getClass().getResource("/view/login.fxml");
        if (fxmlLocation == null) {
            throw new IOException("Cannot find /view/login.fxml in classpath");
        }
        FXMLLoader loader = new FXMLLoader(fxmlLocation);
        Scene scene = new Scene(loader.load(), 600, 400);
        primaryStage.setTitle("Tunisair Aircraft Management");
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setOnCloseRequest(event -> {
            laConnexion.closeConnection();
            Platform.exit();
        });

    }


    public static void main(String[] args) {
        laConnexion cn=new laConnexion();
        cn.setUser("root");
        cn.setPassWord("");
        cn.seConnecter();
        launch(args);
    }
}