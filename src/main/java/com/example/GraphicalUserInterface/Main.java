package com.example.GraphicalUserInterface;
import Config.Config;
import Database.FiltererProcessor;
import Database.MovieManagementProcessor;
import MovieManager.Movie;
import SearchEngine.SearchEngine;
import UserManager.User;
import javafx.application.Application;
import javafx.application.Preloader;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.*;


import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Filter;

public class Main extends Application {
    private static Main main;
    private static Stage stage;
    private User signedInUser;
    private boolean nowShowingMoviesTabActive, comingSoonMoviesTabActive;
    private Movie movieOnDetail;
    private String queryOnSearching;
    private FiltererProcessor filtererProcessor;
//    private Config config;
    private SearchEngine searchEngine;
    private MovieManagementProcessor movieManagementProcessor;
    public Main() {
        super();
        main = this;
        this.movieManagementProcessor = new MovieManagementProcessor();
        this.movieManagementProcessor.getMovies();
        this.filtererProcessor = new FiltererProcessor();
        this.queryOnSearching = "";
//        this.config = new Config();
        nowShowingMoviesTabActive = false;
        comingSoonMoviesTabActive = false;
        searchEngine = new SearchEngine();
    }
    public FiltererProcessor getFiltererProcessor() {
        return this.filtererProcessor;
    }
//    public Config getConfig() throws Exception {
//        return new Config();
//    }
    public void setQueryOnSearching(String queryOnSearching) {
        this.queryOnSearching = queryOnSearching;
    }
    public SearchEngine getSearchEngine() {
        return this.searchEngine;
    }
    public String getQueryOnSearching() {
        return this.queryOnSearching;
    }
    public void setNowShowingMoviesTabActive(boolean isActive) {
        this.nowShowingMoviesTabActive = isActive;
    }
    public void setComingSoonMoviesTabActive(boolean isActive) {
        this.comingSoonMoviesTabActive = isActive;
    }
    public boolean getNowShowingMoviesTabActive() {
        return this.nowShowingMoviesTabActive;
    }
    public boolean getComingSoonMoviesTabActive() {
        return this.comingSoonMoviesTabActive;
    }
    public void setMovieOnDetail(Movie movie) {
        movieOnDetail = movie;
    }
    public MovieManagementProcessor getMovieManagementProcessor() {
        return this.movieManagementProcessor;
    }
    public void setSignedInUser(User user) {
        this.signedInUser = user;
    }
    public  Movie getMovieOnDetail() {
        return movieOnDetail;
    }
    public static synchronized Main getInstance() {
        if (main == null) {
            try {
                main = new Main();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        return main;
    }
    @Override
    public void start(Stage primaryStage) throws IOException {
        stage = primaryStage;
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("index-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 920, 600);
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