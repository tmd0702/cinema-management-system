package com.example.GraphicalUserInterface;

import MovieManager.Movie;
import Utils.Response;
import Utils.StatusCode;
import Utils.Utils;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

public class MovieDetailViewController implements Initializable {
    @FXML
    private ImageView moviePosterSection;
    @FXML
    private VBox commentViewSection;
    private Movie movieOnDetail;
    private Main main;
    private boolean isRated;
    private double ratingScore;
    @FXML
    private AnchorPane movieDetailViewRootContainer, movieDetailViewMainContainer;
    @FXML
    private TextArea commentField;
    @FXML
    private Label title;
    @FXML
    private ImageView logoImageView;
    @FXML
    private Label cast;
    @FXML
    private Line separator;
    @FXML
    private TextField inputField;
    @FXML
    private Label director;
    @FXML
    private HBox ratingField;
    @FXML
    private Label genre;
    @FXML
    private Label releaseDate;
    @FXML
    private Label duration;
    @FXML
    private Label language;
    @FXML
    private VBox movieDetailSection;
    @FXML
    private TextArea description;
    public MovieDetailViewController () {
        main = Main.getInstance();
        movieOnDetail = main.getMovieOnDetail();
        isRated = false;
        ratingScore = 0.0;
    }
    @FXML
    public void onSearchFieldEnterKeyPress() throws IOException {
        main.setQueryOnSearching(inputField.getText());
        main.changeScene("search-results-view.fxml");
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
    @FXML
    public void onSignInBtnClick() throws IOException {
        movieDetailViewMainContainer.setDisable(true);
        movieDetailViewRootContainer.getChildren().add(FXMLLoader.load(getClass().getResource("login-form.fxml")));
    }
    @FXML
    public void onSignUpBtnClick() throws IOException {
        movieDetailViewMainContainer.setDisable(true);
        movieDetailViewRootContainer.getChildren().add(FXMLLoader.load(getClass().getResource("signup-form.fxml")));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        logoImageViewInit();
        movieDetailSectionInit();
        ratingFieldInit();
        commentViewSectionInit();
    }
    public void movieDetailSectionInit() {
        movieDetailSection.setSpacing(10);
        System.out.println(movieOnDetail.getPosterPath());
        moviePosterSection.setImage(movieOnDetail.getPosterImage());
        title.setText(movieOnDetail.getTitle());
        genre.setText(genre.getText() + movieOnDetail.getGenres().toString().replace("[", "").replace("]", ""));
        releaseDate.setText(releaseDate.getText() + movieOnDetail.getReleaseDate().toString());
        duration.setText(duration.getText() + Integer.toString(movieOnDetail.getDuration()));
        VBox.setMargin(separator, new Insets(20, 0, 20, 0));
        description.setWrapText(true);
        description.setText(movieOnDetail.getOverview());
        description.setDisable(true);
    }
    public void ratingFieldInit() {
        for (int i=0; i < ratingField.getChildren().size();++i) {
            StackPane starHolder = (StackPane) ratingField.getChildren().get(i);
            ratingStarMouseEventListener(starHolder.getChildren().get(1), i, "HALF");
            ratingStarMouseEventListener(starHolder.getChildren().get(0), i, "FULL");
        }
    }
    public void activateRatingStar(int starHolderIndex, String type) {
        for (int i=0; i <= starHolderIndex;++i) {
            StackPane starHolder = (StackPane) ratingField.getChildren().get(i);
            if (i < starHolderIndex) {
                starHolder.getChildren().get(0).setStyle("-fx-font-family: FontAwesome; -fx-font-size: 30.0; -fx-fill: #FDC500;");
            } else {
                if (type == "HALF") {
                    starHolder.getChildren().get(1).setStyle("-fx-font-family: FontAwesome; -fx-font-size: 25.0; -fx-fill: #FDC500;");
                } else if (type == "FULL") {
                    starHolder.getChildren().get(0).setStyle("-fx-font-family: FontAwesome; -fx-font-size: 30.0; -fx-fill: #FDC500;");
                }
            }
        }
    }
    public void resetRatingStarStyling() {
        for (int i=0; i < ratingField.getChildren().size();++i) {
            StackPane starHolder = (StackPane) ratingField.getChildren().get(i);
            for (Node star : starHolder.getChildren()) {
                star.setStyle("-fx-font-family: FontAwesome; -fx-font-size: 30.0;");
            }
        }
    }
    public void ratingStarMouseEventListener(Node star, int starHolderIndex, String type) {
        star.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (!isRated) activateRatingStar(starHolderIndex, type);
            }
        });
        star.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (isRated == false) resetRatingStarStyling();
            }
        });
        star.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (isRated) {
                    isRated = false;
                } else {
                    isRated = true;
                    ratingScore = starHolderIndex + 1 - 0.5 * (type == "HALF" ? 1 : 0);
                }
            }
        });
    }
    public void commentViewSectionInit() {
        ArrayList<ArrayList<String>> reviewFetcher = main.getReviewManagementProcessor().getData(0, -1, String.format("M.ID = '%s'", main.getMovieOnDetail().getId()), "R.DATE DESC").getData();
        System.out.println(reviewFetcher);
        for (int i=2; i < reviewFetcher.size(); ++i) {
            StackPane userCategoryView = new StackPane(new Label(Utils.getRowValueByColumnName(i, "CATEGORY", reviewFetcher)));
            HBox ratingView = new HBox();
            for (int j=0; j<5; ++j) {
                FontAwesomeIconView fullStar = new FontAwesomeIconView(FontAwesomeIcon.STAR);
                fullStar.setStyle("-fx-font-family: FontAwesome; -fx-font-size: 20.0;");
                ratingView.getChildren().add(fullStar);
            }
            Label usernameView = new Label(Utils.getRowValueByColumnName(i, "USERNAME", reviewFetcher));
            HBox commentViewHeader = new HBox(userCategoryView, ratingView, usernameView);
            Line separator = new Line();
            TextField commentView = new TextField(Utils.getRowValueByColumnName(i, "COMMENT", reviewFetcher));
            VBox commentViewContainer = new VBox(commentViewHeader, commentView, separator);
            commentViewSection.getChildren().add(commentViewContainer);
        }
    }
    @FXML
    public void commentSubmitBtnOnClick() throws Exception {
        if (main.getSignedInUser() != null) {
            HashMap<String, String> reviewInfo = new HashMap<String, String>();
            reviewInfo.put("USER_ID", main.getSignedInUser().getId());
            reviewInfo.put("MOVIE_ID", main.getMovieOnDetail().getId());
            reviewInfo.put("RATING", String.valueOf(ratingScore));
            reviewInfo.put("COMMENT", String.valueOf(commentField.getText()));
            reviewInfo.put("DATE", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            Response response = main.getReviewManagementProcessor().insertData(reviewInfo, true);
            StatusCode status = response.getStatusCode();
            if (status == status.OK) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation");
                alert.setContentText(response.getMessage());
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {

                } else {

                }
            } else {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Failed");
                alert.setContentText(response.getMessage());
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {

                } else {

                }
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Failed");
            alert.setContentText("Please sign in to comment!");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                movieDetailViewMainContainer.setDisable(true);
                movieDetailViewRootContainer.getChildren().add(FXMLLoader.load(getClass().getResource("login-form.fxml")));
            } else {

            }
        }
    }
}
