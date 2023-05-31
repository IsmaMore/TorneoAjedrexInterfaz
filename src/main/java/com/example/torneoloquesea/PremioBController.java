package com.example.torneoloquesea;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
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

    private ArrayList<Premio> premios;

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
    private Label labelExB;

    @FXML
    protected void exportarB(){
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("PremiosB.txt"));
            int espNombre = 40, espTipo_Premio = 25;
            bw.write(" Posicion | Nombre                                   | Tipo_Premio               | Cantidad");
            bw.newLine();
            bw.write(" ---------|------------------------------------------|---------------------------|---------");
            bw.newLine();
            for (Premio premio: premios){
                int position = premio.getPosicion();
                bw.write(" " + position);
                if (position < 10){
                    bw.write("       ");
                }else if (position < 100){
                    bw.write("      ");
                }else if (position < 1000){
                    bw.write("     ");
                }
                bw.write(" | ");
                String nombre = premio.getNombre();
                bw.write(nombre);
                for (int i = 1; i <= espNombre - nombre.length(); i++ ){
                    bw.write(" ");
                }
                bw.write(" | ");
                String tipo_Premio = premio.getTipo_Premio();
                if (!(tipo_Premio == null)){
                    bw.write(tipo_Premio);
                    for (int i = 1; i <= espTipo_Premio - tipo_Premio.length(); i++ ){
                        bw.write(" ");
                    }
                }else {
                    for (int i = 1; i <= espTipo_Premio; i++ ){
                        bw.write(" ");
                    }
                }

                bw.write(" | ");
                String cantidad = premio.getCantidad();
                bw.write(" " + cantidad);
                if (cantidad.length() < 3){
                    bw.write("      ");
                }else if (cantidad.length() < 4){
                    bw.write("     ");
                }else if (cantidad.length() < 5){
                    bw.write("    ");
                }
                bw.newLine();
            }
            bw.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        labelExB.setText("Se ha exportado la tabla de premios.");
        labelExB.setVisible(true);
    }

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
        premios = Premio.obtenerPremios(cnxB);
        ob.addAll(premios);
        tablaPremioB.setItems(ob);
        labelExB.setVisible(false);
    }
}


