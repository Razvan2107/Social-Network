module com.example.laborator {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires java.sql;

    opens com.example.laborator to javafx.fxml;
    opens domain to javafx.base;
    opens service to javafx.fxml;
    exports com.example.laborator;
}