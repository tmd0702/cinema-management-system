package com.example.GraphicalUserInterface;
import MovieManager.MovieManager;
import Utils.Utils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import MovieManager.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.*;
import javafx.scene.input.MouseEvent;

public class IndexViewController implements Initializable {
    private MovieManager movieManager;
    private Main main;
    @FXML
    private ImageView logoImageView;
    @FXML
    private Button signUpBtn;
    @FXML
    private ScrollPane moviePreviewSectionScrollPane;
    @FXML
    private Button signInBtn;
    @FXML ImageView backdropImageSection;
    @FXML
    private Rectangle addMovieBtn;
    @FXML
    private HBox currentlyPlayingList;
    @FXML
    private HBox comingSoonList;
    @FXML
    private HBox moviePreviewSection;

    public void initialize(URL url, ResourceBundle rb) {
        moviePreviewSectionInit();
        currentlyPlayingListInit();
        comingSoonListInit();
        backDropImageSectionInit();
        logoImageViewInit();
    }
    public void logoImageViewInit() {
        String imageSource = "https://docs.google.com/uc?id=1F2pXOLfvuynr9JcURTR5Syg7N1YdPJXK";
        Image logo = new Image(imageSource);
        if (logo.isError()) {
            System.out.println("Error loading image from " + imageSource);
        } else {
            logoImageView.setImage(logo);
        }
    }
    @FXML
    public void logoImageViewOnClick() throws IOException {
        main.changeScene("index-view.fxml");
    }
    public void movieListViewSectionInit(HBox listView, ArrayList<Movie> movieList) {
        double listSpacing = 10;
        int counter = 1;
        listView.setSpacing(listSpacing);
        for (Movie movie : movieList) {
            StackPane movieView = getMovieView(listView, listSpacing, movie);
            listView.getChildren().add(movieView);
            counter += 1;
            if (counter > 4) break;
        }
    }
    public static StackPane getMovieView(HBox listView, double listSpacing, Movie movie) {
        StackPane movieView = new StackPane();
//            movieView.setPadding(new Insets(20, 20, 20, 20));
//            movieView.setId(movie.getId() + "CurrentlyPlayingList");
        movieView.setPrefWidth((listView.getPrefWidth() - listSpacing * 3) / 4);
        movieView.setPrefHeight(listView.getPrefHeight());
        System.out.println(movieView.getPrefHeight() + " " + movieView.getPrefWidth());
        VBox movieInfoSection = new VBox();
        Label movieTitle = new Label(movie.getTitle());
        movieTitle.setStyle("-fx-font-weight: bold;-fx-font-size: 14px;-fx-text-fill:white;");
        Label movieReleaseDate = new Label(Utils.getDateStringWithFormat("dd MMMM", movie.getReleaseDate()));
        movieReleaseDate.setStyle("-fx-font-size:11px;-fx-text-fill:white;");
        movieInfoSection.getChildren().add(movieTitle);
        movieInfoSection.getChildren().add(movieReleaseDate);
        movieInfoSection.setPadding(new Insets(20, 20, 20, 20));
        ImageView poster = new ImageView(movie.getPosterImage());
        poster.setFitWidth((listView.getPrefWidth() - listSpacing * 3) / 4);
        poster.setFitHeight(listView.getPrefHeight());
        poster.setBlendMode(BlendMode.MULTIPLY);
//            poster.setOpacity(0.5);

        Rectangle blend = new Rectangle(poster.getFitWidth(), poster.getFitHeight(), new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, new Stop[] {new Stop(0, Color.WHITE), new Stop(1, Color.BLACK)}));
//            movieView.setCenter(poster);
//            movieView.setBottom(movieInfoSection);
        movieInfoSection.setTranslateY(listView.getPrefHeight() * 2/3);

        movieView.getChildren().add(blend);
        movieView.getChildren().add(poster);
        movieView.getChildren().add(movieInfoSection);
        movieView.setOnMouseClicked(new EventHandler<MouseEvent>()  {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Main.getInstance().setMovieOnDetail(movie);
                //main.setMovieOnDetail(new Movie("MOV1000", "Toy Story", "Led by Woody, Andy's toys live happily in his room until Andy's birthday brings Buzz Lightyear onto the scene. Afraid of losing his place in Andy's heart, Woody plots against Buzz. But when circumstances separate Buzz and Woody from their owner, the duo eventually learns to put aside their differences.", "ASDS", 100, 90, new Date(), "https://image.tmdb.org/t/p/original/7G9915LfUQ2lVfwMEEhDsn3kT4B.jpg", "https://image.tmdb.org/t/p/original/9FBwqcd9IRruEDUrTdcaafOMKUq.jpg"));
                try {
                    Main.getInstance().changeScene("movie-detail-view.fxml");
                } catch (IOException e) {
                    System.out.println(e);
                }
            }
        });
        return movieView;
    }
    public void backDropImageSectionInit() {
        backdropImageSection.setFitWidth(903);
//        backdropImageSection.setScaleX(1.3);

    }
    public void currentlyPlayingListInit() {
        movieListViewSectionInit(currentlyPlayingList, movieManager.getCurrentlyPlayingMovieList());
    }
    public void comingSoonListInit() {
        movieListViewSectionInit(comingSoonList, movieManager.getComingSoonMovieList());
    }

    public void moviePreviewSectionInit() {
        moviePreviewSection.setSpacing(20);
        moviePreviewSectionScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        moviePreviewSectionScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        for (Movie movie : movieManager.getMovieList()) {
            // initialize booking button
            Button bookingBtn = new Button();
            bookingBtn.setId(movie.getId() + "BookingBtn");
            bookingBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    try {
                        main.changeScene("booking-form.fxml");
                    } catch (IOException e) {
                        System.out.println(e);
                    }

                }
            });
            // initialize movie view
            AnchorPane movieView = new AnchorPane();
            movieView.setId(movie.getId());
            movieView.setPrefHeight(addMovieBtn.getHeight());
            movieView.setPrefWidth(addMovieBtn.getWidth());
            ImageView poster = new ImageView(movie.getPosterImage());
            poster.setFitHeight(addMovieBtn.getHeight());
            poster.setFitWidth(addMovieBtn.getWidth());
            movieView.setStyle("-fx-background-radius:30%;");
            movieView.getChildren().add(poster);
            movieView.getChildren().add(bookingBtn);
            bookingBtn.setPrefWidth(50);
            bookingBtn.setPrefHeight(20);
            bookingBtn.setStyle("-fx-background-color: #AB0A10;-fx-text-fill: white;-fx-font-weight: bold;-fx-font-size:8px");
            bookingBtn.setText("Book Now");
            bookingBtn.setLayoutX(26);
//            movieView.setAlignment(bookingBtn, Pos.BOTTOM_CENTER);
            bookingBtn.setVisible(false);
            bookingBtn.setLayoutY(135);
            bookingBtn.setScaleY(bookingBtn.getScaleY() * 1.5);
            movieView.setStyle("-fx-background-color: transparent");//(addMovieBtn.getFill());
            changeStyleOnHover(movieView, poster, movie, bookingBtn);

            moviePreviewSection.getChildren().add(0, movieView);
        }
    }
    public IndexViewController() throws Exception {
        main = Main.getInstance();
        movieManager = main.getMovieManagementProcessor().getMovieManager();
    }
    @FXML
    public void onAddMovieBtnClick() {
        addMovieBtn.setHeight(addMovieBtn.getHeight() * 2.5);
        addMovieBtn.setWidth(addMovieBtn.getWidth() * 1.5);
        System.out.println(moviePreviewSection.getChildren());
    }
    @FXML
    public void onSeeMoreCPBtnClick() throws IOException {
        main.setNowShowingMoviesTabActive(true);
        main.changeScene("movie-list-view.fxml");
    }
    @FXML
    public void onSeeMoreCSBtnClick() throws IOException {
        main.setComingSoonMoviesTabActive(true);
        main.changeScene("movie-list-view.fxml");
    }
    public void changeStyleOnHover(Node node, ImageView poster, Movie movie, Button bookingBtn) {
        node.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                bookingBtn.setVisible(true);
                backdropImageSection.setImage(movie.getBackdropImage());
                node.setScaleX(node.getScaleX() * 1.5);
                poster.setScaleY(node.getScaleY() * 1.5);
//                poster.setFitHeight(poster.getFitHeight() * 1.5);
//                poster.setFitWidth(poster.getFitWidth() * 1.5);
//                ScaleTransition transition = new ScaleTransition();
//                transition.setDuration(Duration.seconds(0.1));
//                transition.setNode(node);
//                transition.setToX(1.5);
//                transition.setToY(1.5);
//                transition.play();
                HBox.setMargin(node, new Insets(25.5, 25.5, 0, 25.5));
//                KeyValue startKeyValue = new KeyValue(node.translateXProperty(), 0);
//                KeyValue endKeyValue = new KeyValue(node.translateXProperty(), 30);
//                KeyFrame startKeyFrame = new KeyFrame(Duration.seconds(0), startKeyValue);
//                KeyFrame endKeyFrame = new KeyFrame(Duration.seconds(1), endKeyValue);
//                Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
//                    HBox.setMargin(node, new Insets(30, 30, 0, 30));
//                }));
//                Timeline timeline = new Timeline(startKeyFrame, endKeyFrame);
//                timeline.play();
            }
        });
        node.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                bookingBtn.setVisible(false);
//                node.setStyle("-fx-background-color: #0096C9");
                HBox.setMargin(node, new Insets(0, 0, 0, 0));
                node.setScaleX(node.getScaleX() * 2/3);
                poster.setScaleY(poster.getScaleY() * 2/3);
//                poster.setFitHeight(poster.getFitHeight() * 2/3);
//                poster.setFitWidth(poster.getFitWidth() * 2/3);
//                ScaleTransition transition = new ScaleTransition();
//                transition.setDuration(Duration.seconds(0.1));
//                transition.setNode(node);
//                transition.setToX(1);
//                transition.setToY(1);
//                transition.play();
//                Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.01), e -> {
//                    HBox.setMargin(node, new Insets(0, 0, 0, 0));
//                }));
//                timeline.play();

            }
        });
    }
    public void onSignInBtnClick() throws IOException {
        this.main.popup("login-form.fxml");
    }

    public void onSignUpBtnClick() throws IOException {
        this.main.popup("signup-form.fxml");
    }
}
