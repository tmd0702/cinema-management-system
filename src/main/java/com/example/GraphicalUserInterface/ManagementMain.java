package com.example.GraphicalUserInterface;

import Database.*;
import ImageManager.ImageManager;
import Utils.IdGenerator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class ManagementMain extends Application {
    private static Stage stage;
    private Scene scene;
    private Processor cinemaManagementProcessor, theaterManagementProcessor, accountManagementProcessor, movieManagementProcessor, promotionManagementProcessor;
    private IdGenerator idGenerator;
    private SignupProcessor signupProcessor;
    private ImageManager imageManager;
    private static ManagementMain managementMain;
    public ManagementMain() throws Exception {
        managementMain = this;
        idGenerator = new IdGenerator();
        signupProcessor = new SignupProcessor();
        cinemaManagementProcessor = new CinemaManagementProcessor();
        theaterManagementProcessor = new TheaterManagementProcessor();
        promotionManagementProcessor = new PromotionManagementProcessor();
        accountManagementProcessor = new AccountManagementProcessor();
        movieManagementProcessor = new MovieManagementProcessor();
        imageManager = new ImageManager();
    }
    public IdGenerator getIdGenerator() {
        return this.idGenerator;
    }
    public Processor getTheaterManagementProcessor() {
        return this.theaterManagementProcessor;
    }
    public Processor getCinemaManagementProcessor() {
        return this.cinemaManagementProcessor;
    }
    public Processor getMovieManagementProcessor() {
        return this.movieManagementProcessor;
    }
    public Processor getPromotionManagementProcessor() {
        return this.promotionManagementProcessor;
    }
    public SignupProcessor getSignupProcessor() {
        return this.signupProcessor;
    }
    public Node getNodeById(String id) {
        return stage.getScene().lookup(id);
    }
    public static synchronized ManagementMain getInstance() {
        if (managementMain == null) {
            try {
                managementMain = new ManagementMain();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        return managementMain;
    }
    public ImageManager getImageManager() {
        return this.imageManager;
    }
    public Processor getAccountManagementProcessor() {
        return this.accountManagementProcessor;
    }
    public Scene getScene() {
        return this.scene;
    }
    @Override
    public void start(Stage primaryStage) throws IOException {
        stage = primaryStage;
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("admin-login-form.fxml"));
        scene = new Scene(fxmlLoader.load(), 920, 600);

//        scene.getStylesheets().add(getClass().getResource("assets/css/index-style.css").toExternalForm());
        stage.setTitle("4HB Cinema Management System");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public void changeScene(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(fxml));
        stage.getScene().setRoot(fxmlLoader.load());
    }
    public static void ShowErrors(){

    }
    public static void main(String[] args) {
        launch();
    }
}
