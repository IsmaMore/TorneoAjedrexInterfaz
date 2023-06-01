package com.example.torneoloquesea;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Clase Inicio_Torneo. Ejectuta el programa y la interfaz, e importa los datos de los premios.
 *
 * @version     0.1 05/05/2023
 * @author      Abel
 * @version     1.0 01/06/2023
 * @author      Ismael Moreno
 */
public class Inicio_Torneo extends Application {
    /**
     * Carga la primera interfaz.
     *
     * @param stage
     * @throws IOException
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Inicio_Torneo.class.getResource("InterfazInicio.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add(this.getClass().getResource("estilo1.css").toExternalForm());
        stage.setTitle("Inicio");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Llama al metodo main de la clase Torneo, donde se importa los datos de premio. Y lanza la interfaz.
     *
     * @param args
     */
    public static void main(String[] args) {
        Torneo.ejecutarMain();
        launch();
    }
}