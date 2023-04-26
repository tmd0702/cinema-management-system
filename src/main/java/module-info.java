module com.example.cinemamanagementsystem {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;



    requires java.sql;
    requires org.json;
    opens com.example.GraphicalUserInterface to javafx.fxml;
    exports com.example.GraphicalUserInterface;
}