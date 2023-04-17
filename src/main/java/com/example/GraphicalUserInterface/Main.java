package com.example.GraphicalUserInterface;
import Database.MovieManagementProcessor;
import MovieManager.Movie;
import UserManager.User;
import javafx.application.Application;
import javafx.application.Preloader;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.*;


import java.io.IOException;
import java.util.ArrayList;

public class Main extends Application {
    private static Main main;
    private static Stage stage;
    private User signedInUser;
    private Movie movieOnDetail;
    private MovieManagementProcessor movieManagementProcessor;
    public Main() {
        super();
        main = this;
        System.out.println("main ok");
        this.movieManagementProcessor = new MovieManagementProcessor();
    }
    public void setMovieOnDetail(Movie movie) {
        movieOnDetail = movie;
    }
    public void setSignedInUser(User user) {
        this.signedInUser = user;
    }
    public  Movie getMovieOnDetail() {
        return movieOnDetail;
    }
    public static synchronized Main getInstance() {
        if (main == null) {
            main = new Main();
        }
        return main;
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