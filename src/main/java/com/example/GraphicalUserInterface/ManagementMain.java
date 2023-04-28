package com.example.GraphicalUserInterface;

import Database.AccountManagementProcessor;
import Database.Processor;
import ImageManager.ImageManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ManagementMain extends Application {
    private static Stage stage;
    private Processor accountManagementProcessor;
    private ImageManager imageManager;
    private static ManagementMain managementMain;
    public ManagementMain() {
        managementMain = this;
        accountManagementProcessor = new AccountManagementProcessor();
        imageManager = new ImageManager();
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
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("management-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 920, 600);
//        scene.getStylesheets().add(getClass().getResource("assets/css/index-style.css").toExternalForm());
        stage.setTitle("4HB Cinema Management System");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
    public static void main(String[] args) {
        launch();
    }
}
