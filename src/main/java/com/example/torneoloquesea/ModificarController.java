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
    private Button btmVolverA;
    @FXML
    private Button btmVolverB;
    @FXML
    private Button btmVolverM;

    private void volver(Button btn) throws IOException {
        FXMLLoader loader= new FXMLLoader(getClass().getResource("Interfaz2.fxml"));
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
            if (Ranking == Integer.parseInt(tfId.getText())){
                if (!tfNombre.getText().equals("")) {
                    if (!Nombre.equals(tfNombre.getText()) || !(Origen == null || Origen.equals(tfOrigen.getText())) || !Alojado.equals(cbAlojado.getValue()) || !Participa.equals(cbParticipa.getValue())) {
                        boolean cambio;
                        if (torneo.equals("A")) {
                            cambio = Jugador.modificarJugador(Ranking, tfNombre.getText(), tfOrigen.getText(), cbAlojado.getValue(), cbParticipa.getValue(), cnxA);
                        } else {
                            cambio = Jugador.modificarJugador(Ranking, tfNombre.getText(), tfOrigen.getText(), cbAlojado.getValue(), cbParticipa.getValue(), cnxB);
                        }
                        if (cambio) {
                            labelError.setText("¡SE HAN APLICADO LOS CAMBIOS!");
                            labelError.setVisible(true);
                        } else {
                            labelError.setText("¡NO SE HAN APLICADO LOS CAMBIOS!");
                            labelError.setVisible(true);
                        }
                    } else {
                        labelError.setText("¡NO HAS MODIFICADO NINGÚN CAMPO!");
                        labelError.setVisible(true);
                    }
                }else {
                    labelError.setText("¡NOMBRE NO PUEDE ESTAR VACÍO!");
                    labelError.setVisible(true);
                }
            }else {
                labelError.setText("¡NO SE PUEDE MODIFICAR RANKING!");
                labelError.setVisible(true);
            }
        }else {
            labelError.setText("¡NO SE PUDO APLICAR CAMBIOS!");
            labelError.setVisible(true);
        }
    }

    @FXML
    protected void buscarJugador(){
        if (!torneo.equals("")) {
            if (!tfId.getText().isEmpty()) {
                ResultSet rs;
                try {
                    if (torneo.equals("A")) {
                        rs = obtenerJugadorUnico(cnxA);
                    } else {
                        rs = obtenerJugadorUnico(cnxB);
                    }
                    if (rs.first()) {
                        Ranking = Integer.parseInt(tfId.getText());
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
                        setVisibility(true);
                        labelError.setVisible(false);
                    } else {
                        Ranking = 0;
                        Nombre = "";
                        Origen = "";
                        Alojado = "";
                        Participa = "";
                        tfNombre.setText("");
                        tfOrigen.setText("");
                        cbAlojado.setValue("");
                        cbParticipa.setValue("");
                        labelError.setText("¡JUGADOR NO ENCONTRADO!");
                        labelError.setVisible(true);
                        setVisibility(false);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                labelError.setText("¡INTRODUCIR RANKING(ID)!");
                labelError.setVisible(true);
            }
        }else {
            labelError.setText("¡SELECCIONA UN TORNEO!");
            labelError.setVisible(true);
        }
    }

    private ResultSet obtenerJugadorUnico(Connection cnxB) throws SQLException {
        return Jugador.buscarJugadorUnico(Integer.parseInt(tfId.getText()), cnxB);
    }

    private void mostrarID(){
        setEmptyValues();
        tfId.setVisible(true);
        labelId.setVisible(true);
        setVisibility(false);
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
    }

    private void setVisibility(boolean bol) {
        labelNombre.setVisible(bol);
        tfNombre.setVisible(bol);
        labelOrigen.setVisible(bol);
        tfOrigen.setVisible(bol);
        labelAlojado.setVisible(bol);
        cbAlojado.setVisible(bol);
        labelParticipa.setVisible(bol);
        cbParticipa.setVisible(bol);
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
    }
}
