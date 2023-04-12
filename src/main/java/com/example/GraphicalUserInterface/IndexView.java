package com.example.GraphicalUserInterface;
import javafx.fxml.FXML;
import javafx.scene.layout.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import java.util.*;
import javafx.geometry.*;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.*;
import java.io.IOException;
import javafx.scene.shape.*;
import javafx.animation.*;
import javafx.util.*;
public class IndexView extends Application {
    private static Stage stage;
    @Override
    public void start(Stage primaryStage) throws IOException {
        stage = primaryStage;
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
    public void changeScene(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(IndexView.class.getResource(fxml));
        stage.getScene().setRoot(fxmlLoader.load());
    }
    public void popUp(String fxml) throws IOException {

        Popup popup = new Popup();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxml));
//        loader.setController(controller);
        popup.getContent().add((Parent) fxmlLoader.load());
        if (!popup.isShowing())
            popup.show(stage);
    }

    public static void main(String[] args) {
        launch();
    }
}