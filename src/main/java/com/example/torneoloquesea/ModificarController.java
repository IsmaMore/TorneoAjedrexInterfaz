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

    static String torneo;

    @FXML
    private Button btnTA;

    @FXML
    private Button btnTB;

    @FXML
    private Button btmBuscar;

    @FXML
    private Button btmAplicar;

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

    @FXML
    protected void volverA() throws IOException {
        FXMLLoader loader= new FXMLLoader(getClass().getResource("Interfaz2.fxml"));
        Parent root= loader.load();
        Scene scene= new Scene(root);
        Stage stage=(Stage) btmVolverA.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Torneos");
        stage.show();
    }
    @FXML
    protected void volverB() throws IOException {
        FXMLLoader loader= new FXMLLoader(getClass().getResource("Interfaz2.fxml"));
        Parent root= loader.load();
        Scene scene= new Scene(root);
        Stage stage=(Stage) btmVolverB.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Torneos");
        stage.show();
    }
    @FXML
    protected void volverM() throws IOException {
        FXMLLoader loader= new FXMLLoader(getClass().getResource("Interfaz2.fxml"));
        Parent root= loader.load();
        Scene scene= new Scene(root);
        Stage stage=(Stage) btmVolverM.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Torneos");
        stage.show();
    }

    @FXML
    protected void aplicarCambios() {

    }

    @FXML
    protected void buscarJugador(){
        if (!tfId.getText().isEmpty()) {
            ResultSet rs;
            try {
                if (torneo.equals("A")){
                    rs = buscarJugador(cnxA);
                } else{
                    rs = buscarJugador(cnxB);
                }
                if (rs.first()){
                    tfNombre.setText(rs.getString(1));
                    tfOrigen.setText(rs.getString(2));
                    cbAlojado.setValue(rs.getString(3));
                    String sem;
                    if (rs.getBoolean(4)){
                        sem = "Si";
                    }else {
                        sem = "No";
                    }
                    cbParticipa.setValue(sem);
                    setVisibility(true);

                }else {
                    labelError.setText("Error: Jugador no encontrado");
                    labelError.setVisible(true);
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
    }

    private ResultSet buscarJugador(Connection cnxB) throws SQLException {
        return Jugador.modificarJugador(Integer.parseInt(tfId.getText()), cnxB);
    }

    @FXML
    protected void mostrarIDA(){
        setEmptyValues();
        tfId.setVisible(true);
        labelId.setVisible(true);
        torneo = "A";
        setVisibility(false);
        if (btnTB.getStyleClass().size() == 3){
            btnTB.getStyleClass().remove("mantener");
        }
        if (btnTA.getStyleClass().size() == 2){
            btnTA.getStyleClass().add("mantener");
        }
    }

    @FXML
    protected void mostrarIDB(){
        setEmptyValues();
        tfId.setText("");
        tfId.setVisible(true);
        labelId.setVisible(true);
        torneo = "B";
        setVisibility(false);
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
