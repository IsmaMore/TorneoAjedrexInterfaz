module com.example.torneoloquesea {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.torneoloquesea to javafx.fxml;
    exports com.example.torneoloquesea;
}