package com.example.torneoloquesea;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Clase InicioController. Clase para usar los metodos en la interfaz de inicio.
 *
 * @version     1.0 05/05/2023
 * @author      Abel
 */
public class InicioController {
    @FXML
    private Button btmsalida;
    @FXML
    private Button btmIniciar;

    /**
     * Funcion que se lanza al presionar el boton de "Iniciar" y pasa a la siguiente interfaz.
     *
     * @throws IOException
     */
    @FXML
    public void entrarTorneo() throws IOException {
        FXMLLoader loader= new FXMLLoader(getClass().getResource("InterfazTabla.fxml"));
        Parent root= loader.load();
        Scene scene= new Scene(root);
            Stage stage = (Stage) btmIniciar.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Torneos");
            stage.show();
    }

    /**
     * Funcion que se lanza al presionar el boton de "Exit" y sale del programa.
     */
    @FXML
    protected void salir(){
        Stage stage=(Stage) btmsalida.getScene().getWindow();
        stage.close();
    }

}