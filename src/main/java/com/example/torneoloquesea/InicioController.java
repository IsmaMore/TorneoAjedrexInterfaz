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

public class InicioController {
    @FXML
    private Button btmsalida;
    @FXML
    private Button btmIniciar;
    @FXML
    public void entrarTorneo() throws IOException {
        FXMLLoader loader= new FXMLLoader(getClass().getResource("Interfaz2.fxml"));
        Parent root= loader.load();
        if (btmIniciar.isFocused()){
            Torneo.main(null);
        }
        Scene scene= new Scene(root);
            Stage stage = (Stage) btmIniciar.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Torneos");
            stage.show();
    }

    @FXML
    protected void salir(){
        Stage stage=(Stage) btmsalida.getScene().getWindow();
        stage.close();
    }

}