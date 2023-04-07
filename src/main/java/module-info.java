module com.example.cinemamanagementsystem {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    requires java.sql;

    opens com.example.cinemamanagementsystem to javafx.fxml;
    exports com.example.cinemamanagementsystem;
}