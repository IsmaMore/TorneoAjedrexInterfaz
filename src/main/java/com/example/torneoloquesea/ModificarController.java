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
    private Button btmVolver;

    @FXML
    protected void volver() throws IOException {
        FXMLLoader loader= new FXMLLoader(getClass().getResource("Interfaz2.fxml"));
        Parent root= loader.load();
        Scene scene= new Scene(root);
        Stage stage=(Stage) btmVolver.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Torneos");
        stage.show();
    }
}
