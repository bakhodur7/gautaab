module com.example.gautaabapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires json.simple;


    opens com.example.gautaabapp to javafx.fxml;
    exports com.example.gautaabapp;
}