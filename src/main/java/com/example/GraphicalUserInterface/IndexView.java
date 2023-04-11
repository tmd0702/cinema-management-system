package com.example.GraphicalUserInterface;
import javafx.scene.layout.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.util.*;
import javafx.geometry.*;
import javafx.scene.control.*;

import java.io.IOException;
import javafx.scene.shape.*;

public class IndexView extends Application {
    @Override
    public void start(Stage stage) throws IOException {
//        HBox HBox = new HBox();
//        HBox.setSpacing(20);
//
//        ScrollPane scrollPane = new ScrollPane(HBox);
//        scrollPane.setFitToHeight(true);
//        scrollPane.setId("test");
//
//        ArrayList<Rectangle> movieList = new ArrayList<Rectangle>();
//
//        for (int i = 1; i <= 20; i++) {
//            Rectangle movie = new Rectangle();
////            movie.setFill(Color.YELLOW);
////            movie.setId("test-m");
//            movie.setStyle("-fx-fill: red");
//            movie.setWidth(73);
//            movie.setHeight(107);
//            movieList.add(movie);
//            HBox.getChildren().add(movie);
//        }
        System.out.println(IndexView.class.getResource("index-view.fxml"));
        FXMLLoader fxmlLoader = new FXMLLoader(IndexView.class.getResource("index-view.fxml"));

        // test
//        Scene scene2 = new Scene(scrollPane);

        Scene scene = new Scene(fxmlLoader.load(), 900, 460);
//        scene.getStylesheets().add("C:\\Users\\mduc0\\Documents\\CODE\\Java\\4hb-project-master\\src\\main\\resources\\com\\example\\GraphicalUserInterface\\index-style.css");
//        System.out.println(getClass().getResource("index-style.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("index-style.css").toExternalForm());
        stage.setTitle("4HB");
        stage.setScene(scene);
//        System.out.println(HBox);
//        stage.setScene(new Scene(new StackPane( scrollPane), 900, 460));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}