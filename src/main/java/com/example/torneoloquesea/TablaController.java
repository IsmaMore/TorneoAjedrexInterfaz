package com.example.torneoloquesea;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TablaController {

    static Connection cnxA;
    static Connection cnxB;

    static {
        try {
            cnxA = DriverManager.getConnection("jdbc:mariadb://localhost:3306/torneoA", "root", "root");
            cnxB = DriverManager.getConnection("jdbc:mariadb://localhost:3306/torneoB", "root", "root");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Stage stage;

    private Label label_premio;

    @FXML
    private Button btmVolver;

    @FXML
    private Button btmModifica;

    @FXML
    private Button btmTablaA;

    @FXML
    private Button btmTablaB;

    @FXML
    private Button btmDesca;

    @FXML
    private Button btmPremio;

    @FXML
    private Button btmReset;

    @FXML
    private TableView<Jugador> tablas = new TableView<Jugador>();

    @FXML
    private TableColumn<Jugador, Integer> colRank;

    @FXML
    private TableColumn<Jugador, String> colNombre;

    @FXML
    private TableColumn<Jugador, Integer> colFide;

    @FXML
    private TableColumn<Jugador, Integer> colFide_Id;

    @FXML
    private TableColumn<Jugador, String> colOrigen;

    @FXML
    private TableColumn<Jugador, String> colAlojado;

    @FXML
    private TableColumn<Jugador, Boolean> colParti;


    @FXML
    protected void volver() throws IOException {
        FXMLLoader loader= new FXMLLoader(getClass().getResource("Interfaz1.fxml"));
        Parent root= loader.load();
        Scene scene= new Scene(root);
        stage=(Stage) btmVolver.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Inicio");
        stage.show();
    }




    @FXML
    protected void mostrarTabla(){
        colRank.setCellValueFactory(new PropertyValueFactory<Jugador, Integer>("Ranking"));
        colNombre.setCellValueFactory(new PropertyValueFactory<Jugador, String>("Nombre"));
        colFide.setCellValueFactory(new PropertyValueFactory<Jugador, Integer>("Fide"));
        colFide_Id.setCellValueFactory(new PropertyValueFactory<Jugador, Integer>("Id_Fide"));
        colOrigen.setCellValueFactory(new PropertyValueFactory<Jugador, String>("Origen"));
        colAlojado.setCellValueFactory(new PropertyValueFactory<Jugador, String>("Alojado"));
        colParti.setCellValueFactory(new PropertyValueFactory<Jugador, Boolean>("Participa"));
        if(btmTablaA.isFocused()){
            ObservableList<Jugador> ob = FXCollections.observableArrayList();
            for (Jugador jugador: Jugador.obtenerJugadores(cnxA)){
                ob.add(jugador);
            }
            label_premio =new Label("Torneo A");
            label_premio.setFont(new Font("Arial",35));
            label_premio.setLayoutX(180);
            label_premio.setLayoutY(25);
            tablas.setItems(ob);
        }

        if (btmTablaB.isFocused()){
            ObservableList<Jugador> ob = FXCollections.observableArrayList();
            for (Jugador jugador: Jugador.obtenerJugadores(cnxB)){
                ob.add(jugador);
            }
            label_premio =new Label("Torneo B");
            label_premio.setFont(new Font("Arial",35));
            label_premio.setLayoutX(180);
            label_premio.setLayoutY(25);
            tablas.setItems(ob);
        }
    }

    @FXML
    protected void modificar() throws IOException{
        FXMLLoader loader= new FXMLLoader(getClass().getResource("Interfaz4.fxml"));
        Parent root= loader.load();
        Scene scene= new Scene(root);
        stage=(Stage) btmModifica.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Modificar jugador");
        stage.show();
    }

    @FXML
    protected void verPremios() throws IOException{
        FXMLLoader loader= new FXMLLoader(getClass().getResource("Interfaz3.fxml"));
        Parent root= loader.load();
        Scene scene= new Scene(new Group(label_premio,root));
        stage=(Stage) btmPremio.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Premios de jugadores");
        stage.show();

    }
}
