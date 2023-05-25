package com.example.torneoloquesea;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;


public class PremioController extends  ModificarController implements Initializable{

    @FXML
    private Label lblPremio;

    @Override
    protected void volver() throws IOException {
        super.volver();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println(lblPremio.getText());
    }
}


