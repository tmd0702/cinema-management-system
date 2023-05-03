package com.example.GraphicalUserInterface;

import Database.AccountManagementProcessor;
import Database.Processor;
import Database.SignupProcessor;
import ImageManager.ImageManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ManagementMain extends Application {
    private static Stage stage;
    private Processor accountManagementProcessor;
    private SignupProcessor signupProcessor;
    private ImageManager imageManager;
    private static ManagementMain managementMain;
    public ManagementMain() throws Exception {
        managementMain = this;
        signupProcessor = new SignupProcessor();
        accountManagementProcessor = new AccountManagementProcessor();
        imageManager = new ImageManager();
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
    @Override
    public void start(Stage primaryStage) throws IOException {
        stage = primaryStage;
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("admin-login-form.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 920, 600);
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
    public static void main(String[] args) {
        launch();
    }
}
