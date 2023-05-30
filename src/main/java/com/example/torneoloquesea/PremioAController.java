package com.example.torneoloquesea;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;


public class PremioAController extends  ModificarController implements Initializable{

    static Connection cnxA;

    static {
        try {
            cnxA = DriverManager.getConnection("jdbc:mariadb://localhost:3306/torneoA", "root", "root");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private TableView<Premio> tablaPremioA = new TableView<>();

    @FXML
    private TableColumn<Premio, Integer> clmPosicionA;

    @FXML
    private TableColumn<Premio, String> clmNombreA;

    @FXML
    private TableColumn<Premio, String> clmTipoA;

    @FXML
    private TableColumn<Premio, String> clmCantidadA;

    @Override
    protected void volverA() throws IOException {
        super.volverA();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Torneo.ejecutarGenerarClasificacionA();
        clmPosicionA.setCellValueFactory(new PropertyValueFactory<>("Posicion"));
        clmNombreA.setCellValueFactory(new PropertyValueFactory<>("Nombre"));
        clmTipoA.setCellValueFactory(new PropertyValueFactory<>("Tipo_Premio"));
        clmCantidadA.setCellValueFactory(new PropertyValueFactory<>("Cantidad"));
        ObservableList<Premio> ob = FXCollections.observableArrayList();
        ob.addAll(Premio.obtenerPremios(cnxA));
        tablaPremioA.setItems(ob);
    }
}

