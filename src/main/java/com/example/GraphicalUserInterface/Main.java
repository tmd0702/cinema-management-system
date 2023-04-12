package com.example.GraphicalUserInterface;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.*;
import java.io.IOException;

public class Main extends Application {
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
        System.out.println(Main.class.getResource("index-view.fxml"));
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("index-view.fxml"));

        // test
//        Scene scene2 = new Scene(scrollPane);

        Scene scene = new Scene(fxmlLoader.load(), 900, 600);
//        scene.getStylesheets().add("C:\\Users\\mduc0\\Documents\\CODE\\Java\\4hb-project-master\\src\\main\\resources\\com\\example\\GraphicalUserInterface\\index-style.css");
//        System.out.println(getClass().getResource("index-style.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("assets/css/index-style.css").toExternalForm());
        stage.setTitle("4HB Cinema Management");
        stage.setScene(scene);



//        System.out.println(HBox);
//        stage.setScene(new Scene(new StackPane( scrollPane), 900, 460));
        stage.show();
    }
    public void changeScene(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(fxml));
        stage.getScene().setRoot(fxmlLoader.load());
    }
    public Popup popup(String fxml) throws IOException {

        Popup popup = new Popup();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxml));
//        loader.setController(controller);
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