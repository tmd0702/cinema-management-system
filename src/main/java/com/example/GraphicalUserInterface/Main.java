package com.example.GraphicalUserInterface;
import Database.MovieManagementProcessor;
import MovieManager.*;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.*;

import java.io.IOException;
import Database.MovieManagementProcessor.*;

public class Main extends Application {
    private static Stage stage;
    private MovieManagementProcessor movieManagementProcessor;
    public Main() {
        super();
        this.movieManagementProcessor = new MovieManagementProcessor();
    }
    @Override
    public void start(Stage primaryStage) throws IOException {
        stage = primaryStage;
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("index-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 600);
        scene.getStylesheets().add(getClass().getResource("assets/css/index-style.css").toExternalForm());
        stage.setTitle("4HB Cinema Management");
        stage.setScene(scene);
        stage.show();
    }
    public void changeScene(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(fxml));
        stage.getScene().setRoot(fxmlLoader.load());
    }
    public Popup popup(String fxml) throws IOException {
        Popup popup = new Popup();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxml));
        popup.getContent().add(fxmlLoader.load());
        if (!popup.isShowing())
            popup.show(stage);
        return popup;
    }
    public void hide(Popup popup) {
        popup.hide();
    }
    public static void main(String[] args) {
        launch();
    }
}