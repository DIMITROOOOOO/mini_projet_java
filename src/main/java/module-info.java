module com.example.mini_projet {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires java.sql;
    requires jbcrypt;
    requires java.desktop;
    opens com.example.mini_projet.controller to javafx.fxml;
    opens com.example.mini_projet to javafx.fxml;
    exports com.example.mini_projet;
    opens com.example.mini_projet.model to javafx.base;
    exports com.example.mini_projet.controller;

}