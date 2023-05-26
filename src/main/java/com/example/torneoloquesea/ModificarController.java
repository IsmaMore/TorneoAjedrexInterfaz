package com.example.torneoloquesea;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class ModificarController {
    @FXML
    private Button btmVolverA;
    @FXML
    private Button btmVolverB;
    @FXML
    private Button btmVolverM;

    @FXML
    protected void volverA() throws IOException {
        FXMLLoader loader= new FXMLLoader(getClass().getResource("Interfaz2.fxml"));
        Parent root= loader.load();
        Scene scene= new Scene(root);
        Stage stage=(Stage) btmVolverA.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Torneos");
        stage.show();
    }
    @FXML
    protected void volverB() throws IOException {
        FXMLLoader loader= new FXMLLoader(getClass().getResource("Interfaz2.fxml"));
        Parent root= loader.load();
        Scene scene= new Scene(root);
        Stage stage=(Stage) btmVolverB.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Torneos");
        stage.show();
    }
    @FXML
    protected void volverM() throws IOException {
        FXMLLoader loader= new FXMLLoader(getClass().getResource("Interfaz2.fxml"));
        Parent root= loader.load();
        Scene scene= new Scene(root);
        Stage stage=(Stage) btmVolverM.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Torneos");
        stage.show();
    }
}
