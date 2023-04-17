package com.example.GraphicalUserInterface;
import Database.MovieManagementProcessor;
import MovieManager.MovieManager;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import MovieManager.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.scene.shape.*;
import javafx.scene.input.MouseEvent;

public class IndexViewController implements Initializable {
    private MovieManagementProcessor movieManagementProcessor;
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
            BorderPane movieView = new BorderPane();
            movieView.setPadding(new Insets(20, 20, 20, 20));
//            movieView.setId(movie.getId() + "CurrentlyPlayingList");
            movieView.setPrefWidth((listView.getPrefWidth() - listSpacing * 3) / 4);
            movieView.setStyle("-fx-background-color: green");
            VBox movieInfoSection = new VBox();
            Label movieTitle = new Label(movie.getTitle());
            movieTitle.setStyle("-fx-font-weight: bold");
            movieTitle.setStyle("-fx-font-size: 18px");
            Label movieCategory = new Label(movie.getCategory());

            movieInfoSection.getChildren().add(movieTitle);
            movieInfoSection.getChildren().add(movieCategory);
//            movieView.getChildren().add(movieInfoSection);
            movieView.setBottom(movieInfoSection);
            movieView.setOnMouseClicked(new EventHandler<MouseEvent>()  {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    main.setMovieOnDetail(new Movie("MOV1000", "Toy Story", "Led by Woody, Andy's toys live happily in his room until Andy's birthday brings Buzz Lightyear onto the scene. Afraid of losing his place in Andy's heart, Woody plots against Buzz. But when circumstances separate Buzz and Woody from their owner, the duo eventually learns to put aside their differences.", "ASDS", "SAB", 100, 90, new Date(), "https://image.tmdb.org/t/p/original/7G9915LfUQ2lVfwMEEhDsn3kT4B.jpg", "https://image.tmdb.org/t/p/original/9FBwqcd9IRruEDUrTdcaafOMKUq.jpg"));
                    try {
                        main.changeScene("movie-detail-view.fxml");
                    } catch (IOException e) {
                        System.out.println(e);
                    }
                }
            });
            listView.getChildren().add(movieView);
            counter += 1;
            if (counter > 4) break;
        }
    }
    public void currentlyPlayingListInit() {
        movieListViewSectionInit(currentlyPlayingList, movieManager.getMovieList());
    }
    public void comingSoonListInit() {
        movieListViewSectionInit(comingSoonList, movieManager.getMovieList());
    }
    public void moviePreviewSectionInit() {
        moviePreviewSection.setSpacing(10);
        moviePreviewSectionScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        for (Movie movie : movieManager.getMovieList()) {
            // initialize booking button
            Button bookingBtn = new Button();
            bookingBtn.setId(movie.getId() + "BookingBtn");
            // initialize movie view
            Rectangle movieView = new Rectangle();
            movieView.setId(movie.getId());
            movieView.setHeight(addMovieBtn.getHeight());
            movieView.setWidth(addMovieBtn.getWidth());
            movieView.setStyle("-fx-background-color: black");//(addMovieBtn.getFill());
            changeStyleOnHover(movieView);

            moviePreviewSection.getChildren().add(0, movieView);
        }
//        for (int i=0;i<10;++i) {
//            Rectangle movieView = new Rectangle();
//            movieView.setHeight(addMovieBtn.getHeight());
//            movieView.setWidth(addMovieBtn.getWidth());
//            movieView.setFill(addMovieBtn.getFill());
//            moviePreviewSection.getChildren().add(movieView);
//        }
    }
    public IndexViewController() {
        main = Main.getInstance();
        movieManagementProcessor = new MovieManagementProcessor();
        movieManager = this.movieManagementProcessor.getMovies();
    }
    @FXML
    public void onAddMovieBtnClick() {
        addMovieBtn.setHeight(addMovieBtn.getHeight() * 1.5);
        addMovieBtn.setWidth(addMovieBtn.getWidth() * 1.5);
        System.out.println(moviePreviewSection.getChildren());
    }
    @FXML
    public void onSeeMoreCPBtnClick() throws IOException {
        System.out.println("ok");
        main.changeScene("index-view.fxml");
    }
    @FXML
    public void onSeeMoreCSBtnClick() throws IOException {
        System.out.println("ok");
        main.changeScene("index-view.fxml");
    }
    public void changeStyleOnHover(Node node) {
        node.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                node.setScaleX(node.getScaleX() * 1.5);
                node.setScaleY(node.getScaleY() * 1.5);
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
//                node.setStyle("-fx-background-color: #0096C9");
                HBox.setMargin(node, new Insets(0, 0, 0, 0));
                node.setScaleX(node.getScaleX() * 2/3);
                node.setScaleY(node.getScaleY() * 2/3);
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
    public void onMouseExitMovieElement() {
//        ScaleTransition transition = new ScaleTransition();
//        transition.setDuration(Duration.seconds(1));
//        transition.setNode(addMovieBtn);
//        transition.setToX(1);
//        transition.setToY(1);
//        transition.play();
    }
    public void onSignUpBtnClick() throws IOException {
        this.main.popup("signup-form.fxml");
    }
}
