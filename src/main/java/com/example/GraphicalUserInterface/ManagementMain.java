package com.example.GraphicalUserInterface;

import Configuration.Config;
import Database.*;
import ImageManager.ImageManager;
import UserManager.User;
import Utils.IdGenerator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ManagementMain extends Application {
    private static Stage stage;
    private User signedInUser;
    private Scene scene;
    private Config config;
    private IdGenerator idGenerator;
    private ImageManager imageManager;
    private ProcessorManager processorManager;
    private static ManagementMain managementMain;
    public ManagementMain() throws Exception {
        managementMain = this;
        config = new Config();
        idGenerator = new IdGenerator();
        processorManager = ProcessorManager.getInstance();
        imageManager = new ImageManager();
    }
    public Config getConfig() {
        return this.config;
    }
    public IdGenerator getIdGenerator() {
        return this.idGenerator;
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
    public Scene getScene() {
        return this.scene;
    }
    @Override
    public void start(Stage primaryStage) throws IOException {
        processorManager.getMovieManagementProcessor().getMovies();
        stage = primaryStage;
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("admin-login-form.fxml"));
        scene = new Scene(fxmlLoader.load(), 920, 660);
        stage.setTitle("4HB Cinema Management System");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
    public void setSignedInUser(User user) {
        this.signedInUser = user;
    }
    public User getSignedInUser() {
        return this.signedInUser;
    }
    public ProcessorManager getProcessorManager() {
        return this.processorManager;
    }

    public void changeScene(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(fxml));
        stage.getScene().setRoot(fxmlLoader.load());
    }
    public static void main(String[] args) {
        launch();
    }
}
