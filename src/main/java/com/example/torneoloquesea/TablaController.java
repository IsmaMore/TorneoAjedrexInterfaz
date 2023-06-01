package com.example.torneoloquesea;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class TablaController implements Initializable{

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
    private Label labelErrorT;

    @FXML
    private Button btmVolverT;

    @FXML
    private Button btmModifica;

    @FXML
    private Button btmTablaA;

    @FXML
    private Button btmTablaB;

    @FXML
    private Button btmPremio;

    @FXML
    private TableView<Jugador> tablas = new TableView<>();

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
        FXMLLoader loader= new FXMLLoader(getClass().getResource("InterfazInicio.fxml"));
        Parent root= loader.load();
        Scene scene= new Scene(root);
        stage=(Stage) btmVolverT.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Inicio");
        stage.show();
    }




    @FXML
    protected void mostrarTabla(){
        colRank.setCellValueFactory(new PropertyValueFactory<>("Ranking"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("Nombre"));
        colFide.setCellValueFactory(new PropertyValueFactory<>("Fide"));
        colFide_Id.setCellValueFactory(new PropertyValueFactory<>("Id_Fide"));
        colOrigen.setCellValueFactory(new PropertyValueFactory<>("Origen"));
        colAlojado.setCellValueFactory(new PropertyValueFactory<>("Alojado"));
        colParti.setCellValueFactory(new PropertyValueFactory<>("Participa"));
        if (btmTablaA.isFocused() || label_premio.getText().equals("Torneo A")) {
            if (btmTablaB.getStyleClass().size() == 3) {
                btmTablaB.getStyleClass().remove("mantener");
            }
            if (btmTablaA.getStyleClass().size() == 2) {
                btmTablaA.getStyleClass().add("mantener");
            }
            ArrayList<Jugador> jugadores = Jugador.obtenerJugadores(cnxA);
            if (jugadores.size() != 0) {
                ObservableList<Jugador> ob = FXCollections.observableArrayList();
                ob.addAll(jugadores);
                label_premio = new Label("Torneo A");
                label_premio.setFont(new Font("Arial", 35));
                label_premio.setLayoutX(200);
                label_premio.setLayoutY(25);
                tablas.setItems(ob);
                hideError();
            }else {
                showError("¡LOS JUGADORES NO HAN SIDO IMPORTADOS TODAVÍA!");
            }
        }

        if (btmTablaB.isFocused() || label_premio.getText().equals("Torneo B")){
            if (btmTablaA.getStyleClass().size() == 3) {
                btmTablaA.getStyleClass().remove("mantener");
            }
            if (btmTablaB.getStyleClass().size() == 2) {
                btmTablaB.getStyleClass().add("mantener");
            }
            ArrayList<Jugador> jugadores = Jugador.obtenerJugadores(cnxB);
            if (jugadores.size() != 0) {
                ObservableList<Jugador> ob = FXCollections.observableArrayList();
                ob.addAll(jugadores);
                label_premio = new Label("Torneo B");
                label_premio.setFont(new Font("Arial", 35));
                label_premio.setLayoutX(180);
                label_premio.setLayoutY(25);
                tablas.setItems(ob);
            }else {
                showError("¡LOS JUGADORES NO HAN SIDO IMPORTADOS TODAVÍA!");
            }
        }

    }

    @FXML
    protected void modificar() throws IOException{
        FXMLLoader loader= new FXMLLoader(getClass().getResource("InterfazModificar.fxml"));
        Parent root= loader.load();
        Scene scene= new Scene(root);
        stage=(Stage) btmModifica.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Modificar jugador");
        stage.show();
    }

    @FXML
    protected void verPremios() throws IOException, SQLException{
        if (!(label_premio.getText().equals(""))) {
            FXMLLoader loader = null;
            if (label_premio.getText().equals("Torneo A")){
                if (cnxA.createStatement().executeQuery("select * from clasificacion where Posicion = 1").first()){
                    loader = new FXMLLoader(getClass().getResource("InterfazPremiosA.fxml"));
                }
            }else {
                if (cnxB.createStatement().executeQuery("select * from clasificacion where Posicion = 1").first()){
                    loader = new FXMLLoader(getClass().getResource("InterfazPremiosB.fxml"));
                }
            }
            if (loader != null){
                Parent root = loader.load();
                Scene scene = new Scene(root);
                stage = (Stage) btmPremio.getScene().getWindow();
                stage.setScene(scene);
                stage.setTitle(label_premio.getText());
                stage.show();
            }else {
                showError("¡LA CLASIFICACION NO HA SIDO IMPORTADA TODAVÍA!");
            }
        }else {
            showError("¡SELECCIONA TORNEO PARA MOSTRAR LOS PREMIOS!");
        }
    }

    @FXML
    protected void resetearTabla(){
        Torneo.ejecutarGenerarJugadoresA();
        Torneo.ejecutarGenerarJugadoresB();
        Torneo.ejecutarGenerarOptaA();
        Torneo.ejecutarGenerarOptaB();
        showError("¡SE HAN IMPORTADO LOS DATOS JUGADOR!");
    }

    @FXML
    protected void importarClasificacion(){
        Torneo.ejecutarImportarClasificacionA();
        Torneo.ejecutarImportarClasificacionB();
        showError("¡SE HAN IMPORTADO LOS DATOS CLASIFICACIÓN!");
    }

    public void showError(String str){
        labelErrorT.setText(str);
        labelErrorT.setVisible(true);
    }

    public void hideError(){
        labelErrorT.setVisible(false);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        label_premio = new Label("");
        labelErrorT.setVisible(false);
    }
}
