package com.example.torneoloquesea;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;


/**
 * Clase PremioAController. Clase para usar los metodos en la interfaz de Premio A.
 *
 * @version     1.0 01/06/2023
 * @author      Ismael Moreno
 */
public class PremioAController extends  ModificarController implements Initializable{

    static Connection cnxA;

    static {
        try {
            cnxA = DriverManager.getConnection("jdbc:mariadb://localhost:3306/torneoA", "root", "root");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<Premio> premios;

    @FXML
    private Label labelExA;

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

    /**
     * Metodo encargado de exportar en formato de texto los premios finales del torneo.
     */
    @FXML
    protected void exportarA(){
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("PremiosA.txt"));
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
                if (tipo_Premio != null){
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
        labelExA.setText("Se ha exportado la tabla de premios.");
        labelExA.setVisible(true);
    }

    /**
     * Metodo para volver a la interfaz de tabla.
     *
     * @throws IOException
     */
    @Override
    protected void volverA() throws IOException {
        super.volverA();
    }

    /**
     * Metodo que se ejecuta cuando se carga esta interfaz. Usada para que de alguna manera se pueda ejecutar codigo sin tener que presionar nada. Muestra los premios en la tabla.
     *
     * @param url url
     * @param resourceBundle rB
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Torneo.ejecutarGenerarClasificacionA();
        clmPosicionA.setCellValueFactory(new PropertyValueFactory<>("Posicion"));
        clmNombreA.setCellValueFactory(new PropertyValueFactory<>("Nombre"));
        clmTipoA.setCellValueFactory(new PropertyValueFactory<>("Tipo_Premio"));
        clmCantidadA.setCellValueFactory(new PropertyValueFactory<>("Cantidad"));
        ObservableList<Premio> ob = FXCollections.observableArrayList();
        premios = Premio.obtenerPremios(cnxA);
        ob.addAll(premios);
        tablaPremioA.setItems(ob);
        labelExA.setVisible(false);
    }
}

