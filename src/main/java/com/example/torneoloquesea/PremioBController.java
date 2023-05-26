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


public class PremioBController extends  ModificarController implements Initializable{


    static Connection cnxB;

    static {
        try {
            cnxB = DriverManager.getConnection("jdbc:mariadb://localhost:3306/torneoB", "root", "root");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private TableView<Premio> tablaPremioB = new TableView<>();

    @FXML
    private TableColumn<Premio, Integer> clmPosicionB;

    @FXML
    private TableColumn<Premio, String> clmNombreB;

    @FXML
    private TableColumn<Premio, String> clmTipoB;

    @FXML
    private TableColumn<Premio, String> clmCantidadB;

    @FXML
    private Label lblPremio;

    @Override
    protected void volverB() throws IOException {
        super.volverB();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Torneo.ejecutarGenerarClasificacionB();
        clmPosicionB.setCellValueFactory(new PropertyValueFactory<>("Posicion"));
        clmNombreB.setCellValueFactory(new PropertyValueFactory<>("Nombre"));
        clmTipoB.setCellValueFactory(new PropertyValueFactory<>("Tipo_Premio"));
        clmCantidadB.setCellValueFactory(new PropertyValueFactory<>("Cantidad"));
        ObservableList<Premio> ob = FXCollections.observableArrayList();
        ob.addAll(Premio.obtenerPremios(cnxB));
        tablaPremioB.setItems(ob);
        //System.out.println(Stage.("label_premio"));
    }
}


