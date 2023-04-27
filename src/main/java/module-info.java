module com.example.fuelstationdatacollector {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;


    opens com.example.fuelstationdatacollector to javafx.fxml;
    exports com.example.fuelstationdatacollector;
}