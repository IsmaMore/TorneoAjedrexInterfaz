package com.example.torneoloquesea;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ModificarController implements Initializable {

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

    String torneo, Nombre, Origen, Alojado, Participa;

    int Ranking;

    @FXML
    private ListView<String> lvOpta;

    @FXML
    private Button btnTA;

    @FXML
    private Button btnTB;

    @FXML
    private TextField tfId;

    @FXML
    private TextField tfNombre;

    @FXML
    private TextField tfOrigen;

    @FXML
    private ChoiceBox<String> cbAlojado;

    @FXML
    private ChoiceBox<String> cbParticipa;

    private String [] hotel={"Si","No"};

    @FXML
    private Label labelId;

    @FXML
    private Label labelNombre;

    @FXML
    private Label labelOrigen;

    @FXML
    private Label labelAlojado;

    @FXML
    private Label labelParticipa;

    @FXML
    private Label labelError;

    @FXML
    private Label labelOpta;

    @FXML
    private Button btmVolverA;
    @FXML
    private Button btmVolverB;
    @FXML
    private Button btmVolverM;

    private void volver(Button btn) throws IOException {
        FXMLLoader loader= new FXMLLoader(getClass().getResource("InterfazTabla.fxml"));
        Parent root= loader.load();
        Scene scene= new Scene(root);
        Stage stage=(Stage) btn.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Torneos");
        stage.show();
    }

    @FXML
    protected void volverA() throws IOException {
        volver(btmVolverA);
    }

    @FXML
    protected void volverB() throws IOException {
        volver(btmVolverB);
    }
    @FXML
    protected void volverM() throws IOException {
        volver(btmVolverM);
    }

    @FXML
    protected void aplicarCambios() {
        if (Ranking > 0){
            if (tfId.getText().matches("\\d+") && Ranking == Integer.parseInt(tfId.getText())){
                if (!tfNombre.getText().isEmpty()) {
                    if (!(Nombre.equals(tfNombre.getText()) || tfNombre.getText().isEmpty()) || tfOrigen.getText() != null || !Alojado.equals(cbAlojado.getValue()) || !Participa.equals(cbParticipa.getValue())) {
                        if (Origen != null ) {
                            if (!Origen.equals(tfOrigen.getText()) || !(Nombre.equals(tfNombre.getText()) || tfNombre.getText().isEmpty()) || !Alojado.equals(cbAlojado.getValue()) || !Participa.equals(cbParticipa.getValue())) {
                                boolean cambio;
                                if (torneo.equals("A")) {
                                    cambio = Jugador.modificarJugador(Ranking, tfNombre.getText(), tfOrigen.getText(), cbAlojado.getValue(), cbParticipa.getValue(), cnxA);
                                    fillListView(cnxA, Ranking);
                                } else {
                                    cambio = Jugador.modificarJugador(Ranking, tfNombre.getText(), tfOrigen.getText(), cbAlojado.getValue(), cbParticipa.getValue(), cnxB);
                                    fillListView(cnxB, Ranking);
                                }
                                if (cambio) {
                                    mostrarError("¡SE HAN APLICADO LOS CAMBIOS!");
                                    updateData();
                                } else {
                                    mostrarError("¡NO SE HAN APLICADO LOS CAMBIOS!");
                                }
                            }else {
                                mostrarError("¡NO HAS MODIFICADO NINGÚN CAMPO!");
                            }
                        }else {
                            boolean cambio;
                            if (torneo.equals("A")) {
                                cambio = Jugador.modificarJugador(Ranking, tfNombre.getText(), tfOrigen.getText(), cbAlojado.getValue(), cbParticipa.getValue(), cnxA);
                                fillListView(cnxA, Ranking);
                            } else {
                                cambio = Jugador.modificarJugador(Ranking, tfNombre.getText(), tfOrigen.getText(), cbAlojado.getValue(), cbParticipa.getValue(), cnxB);
                                fillListView(cnxA, Ranking);
                            }
                            if (cambio) {
                                mostrarError("¡SE HAN APLICADO LOS CAMBIOS!");
                                updateData();
                            } else {
                                mostrarError("¡NO SE HAN APLICADO LOS CAMBIOS!");
                            }
                        }
                    } else {
                        mostrarError("¡NO HAS MODIFICADO NINGÚN CAMPO!");
                    }
                }else {
                    mostrarError("¡NOMBRE NO PUEDE ESTAR VACÍO!");
                }
            }else {
                mostrarError("¡NO SE PUEDE MODIFICAR RANKING!");
            }
        }else {
            mostrarError("¡NO SE PUDO APLICAR CAMBIOS!");
        }
    }

    private void updateData() {
        Nombre = tfNombre.getText();
        Origen = tfOrigen.getText();
        Alojado = cbAlojado.getValue();
        Participa = cbParticipa.getValue();
    }

    @FXML
    protected void buscarJugador(){
        ResultSet rs = null;
        if (!torneo.equals("")) {
            if (!tfId.getText().isEmpty()) {
                try {
                    if (torneo.equals("A")) {
                        rs = obtenerJugadorUnico(cnxA);
                    } else {
                        rs = obtenerJugadorUnico(cnxB);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }else if (!tfNombre.getText().isEmpty()){
                try {
                    if (torneo.equals("A")) {
                        rs = obtenerJugadorPorNombre(cnxA);
                    } else {
                        rs = obtenerJugadorPorNombre(cnxB);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }else {
                mostrarError("¡INTRODUCIR RANKING(ID) O NOMBRE!");
            }
            if (rs != null) {
                try {
                    if (rs.last()) {
                        int size = rs.getRow();
                        if (size == 1) {
                            Ranking = rs.getInt(5);
                            tfId.setText(String.valueOf(Ranking));
                            Nombre = rs.getString(1);
                            tfNombre.setText(Nombre);
                            Origen = rs.getString(2);
                            tfOrigen.setText(Origen);
                            Alojado = rs.getString(3);
                            cbAlojado.setValue(Alojado);
                            if (rs.getBoolean(4)) {
                                Participa = "Si";
                            } else {
                                Participa = "No";
                            }
                            cbParticipa.setValue(Participa);
                            if (torneo.equals("A")){
                                fillListView(cnxA, Ranking);
                            }else {
                                fillListView(cnxB, Ranking);
                            }
                            setVisibility(true);
                            labelError.setVisible(false);
                        }else {
                            vaciarDatos();
                            mostrarError("¡HAY MUCHOS RESULTADOS!");
                            setVisibility(false);
                        }
                    } else {
                        vaciarDatos();
                        mostrarError("¡JUGADOR NO ENCONTRADO!");
                        tfNombre.setText("");
                        setVisibility(false);
                    }

                }catch (SQLException e){
                    e.printStackTrace();
                }
            }else {
                vaciarDatos();
                tfNombre.setText("");
                mostrarError("¡RANKING(ID) NO VÁLIDO!");
                setVisibility(false);
            }
        }else {
            vaciarDatos();
            mostrarError("¡SELECCIONA UN TORNEO!");
        }
    }

    private void mostrarError(String s) {
        labelError.setText(s);
        labelError.setVisible(true);
    }

    private void vaciarDatos() {
        Ranking = 0;
        Nombre = "";
        Origen = "";
        Alojado = "";
        Participa = "";
        tfOrigen.setText("");
        cbAlojado.setValue("");
        cbParticipa.setValue("");
        lvOpta.getItems().removeAll();
    }

    private void fillListView(Connection cnx, int Ranking){
        lvOpta.getItems().remove(0, lvOpta.getItems().size());
        ArrayList<String> tipoOpta = Premio.obtenerTipoPremioOpta(cnx, Ranking);
        if (tipoOpta != null){
            lvOpta.getItems().addAll(tipoOpta);
        }
    }

    private ResultSet obtenerJugadorUnico(Connection cnx) throws SQLException {
        if (tfId.getText().matches("\\d+")){
            return Jugador.buscarJugadorUnico(Integer.parseInt(tfId.getText()), cnx);
        }
        return null;
    }

    private ResultSet obtenerJugadorPorNombre(Connection cnx) throws SQLException {
        return Jugador.buscarJugadorPorNombre(tfNombre.getText(), cnx);
    }

    private void mostrarID(){
        setEmptyValues();
        setVisibility(false);
        tfId.setVisible(true);
        labelId.setVisible(true);
        labelNombre.setVisible(true);
        tfNombre.setVisible(true);
    }

    @FXML
    protected void mostrarIDA(){
        mostrarID();
        torneo = "A";
        if (btnTB.getStyleClass().size() == 3){
            btnTB.getStyleClass().remove("mantener");
        }
        if (btnTA.getStyleClass().size() == 2){
            btnTA.getStyleClass().add("mantener");
        }
    }

    @FXML
    protected void mostrarIDB(){
        mostrarID();
        torneo = "B";
        if (btnTA.getStyleClass().size() == 3){
            btnTA.getStyleClass().remove("mantener");
        }
        if (btnTB.getStyleClass().size() == 2){
            btnTB.getStyleClass().add("mantener");
        }
    }

    private void setEmptyValues(){
        tfId.setText("");
        tfNombre.setText("");
        tfOrigen.setText("");
        cbParticipa.setValue("");
        cbAlojado.setValue("");
        lvOpta.getItems().removeAll();
    }

    private void setVisibility(boolean bol) {
        labelOrigen.setVisible(bol);
        tfOrigen.setVisible(bol);
        labelAlojado.setVisible(bol);
        cbAlojado.setVisible(bol);
        labelParticipa.setVisible(bol);
        cbParticipa.setVisible(bol);
        labelOpta.setVisible(bol);
        lvOpta.setVisible(bol);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        torneo = "";
        Ranking = 0;
        Nombre = "";
        Origen = "";
        Alojado = "";
        Participa = "";
        cbAlojado.getItems().addAll(hotel);
        cbParticipa.getItems().addAll(hotel);
        labelId.setVisible(false);
        tfId.setVisible(false);
        tfNombre.setVisible(false);
        tfOrigen.setVisible(false);
        cbAlojado.setVisible(false);
        labelNombre.setVisible(false);
        labelOrigen.setVisible(false);
        labelAlojado.setVisible(false);
        labelParticipa.setVisible(false);
        cbParticipa.setVisible(false);
        labelError.setVisible(false);
        labelOpta.setVisible(false);
        lvOpta.setVisible(false);
    }
}
