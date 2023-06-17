package com.example.GraphicalUserInterface;

import MovieManager.Movie;
import Utils.Response;
import Utils.StatusCode;
import Utils.Utils;
import com.example.GraphicalUserInterface.Main;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

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
    private AnchorPane movieDetailViewMainContainer;
    @FXML
    private TextArea commentField;
    @FXML
    private Label title;
    @FXML
    private ImageView logoImageView;
    @FXML
    private Line separator;
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
    private HBox ratingSection;
    @FXML
    private VBox movieDetailSection;
    @FXML
    private Label description;
    @FXML
    private Button bookMovieBtn;
    @FXML
    private Button submitCommentBtn;
    @FXML
    private Label announReview;

    public MovieDetailViewController () {
        main = Main.getInstance();
        movieOnDetail = main.getMovieOnDetail();
        isRated = false;
        ratingScore = 0.0;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        movieDetailSectionInit();
        ratingFieldInit();
        commentViewSectionInit();
        oldReviewInit();
    }
    public void oldReviewInit() {

        if (main.getSignedInUser() != null) {
            ArrayList<ArrayList<String>> reviewFetcher = main.getProcessorManager().getReviewManagementProcessor().getData(0, -1, String.format("R.USER_ID = '%s' AND R.MOVIE_ID = '%s'", main.getSignedInUser().getId(), main.getMovieOnDetail().getId()), "").getData();
            if (reviewFetcher.size() > 2) {
                commentField.setText(Utils.getRowValueByColumnName(2, "COMMENT", reviewFetcher));
                Double rating = Double.parseDouble(Utils.getRowValueByColumnName(2, "RATING", reviewFetcher));
                for (int i = 0; i < 5; ++i) {
                    if (i == Math.round(rating) - 1) {
                        if (i == rating - 1) {
                            activateRatingStar(i, "FULL");
                        } else {
                            activateRatingStar(i, "HALF");
                        }
                        isRated = true;
                    }
                }
            }
        }
    }
    public void movieDetailSectionInit() {
        movieDetailSection.setSpacing(15);
        moviePosterSection.setImage(movieOnDetail.getPosterImage());
        title.setText(movieOnDetail.getTitle());
        genre.setText(genre.getText() + movieOnDetail.getGenresString());
        releaseDate.setText(releaseDate.getText() + movieOnDetail.getReleaseDate().toString());
        duration.setText(duration.getText() + Integer.toString(movieOnDetail.getDuration()) + " minutes");
        VBox.setMargin(separator, new Insets(20, 0, 20, 0));
        description.setWrapText(true);
        description.setText(movieOnDetail.getOverview());
        language.setText(language.getText() + movieOnDetail.getLanguage());
        float voteAverage = movieOnDetail.getVoteAverage() / 2;
        for (int j=0; j<5; ++j) {
            if (j < Math.floor(voteAverage)) {
                FontAwesomeIconView fullStar = new FontAwesomeIconView(FontAwesomeIcon.STAR);
                fullStar.setStroke(Color.WHITE);
                fullStar.setStyle("-fx-font-family: FontAwesome; -fx-font-size: 15.0; -fx-fill: #FDC500;");
                ratingSection.getChildren().add(fullStar);
            } else if (j < voteAverage) {
                FontAwesomeIconView halfStar = new FontAwesomeIconView(FontAwesomeIcon.STAR_HALF);
                halfStar.setStroke(Color.WHITE);
                halfStar.setStyle("-fx-font-family: FontAwesome; -fx-font-size: 15.0; -fx-fill: #FDC500;");
                ratingSection.getChildren().add(halfStar);
                break;
            }
        }
        System.out.println("parent: " + bookMovieBtn.getParent());
        if (main.getProcessorManager().getMovieManagementProcessor().getMovieManager().getCurrentlyPlayingMovieList().contains(movieOnDetail)) {
            bookMovieBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (main.getSignedInUser() != null) {
                        try {
                            main.setMovieOnBooking(movieOnDetail);
                            main.changeView("booking-form.fxml");
                        } catch (IOException e) {
                            System.out.println(e);
                        }

                    } else {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Failed");
                        alert.setContentText("Please sign in to book ticket!");
                        Optional<ButtonType> result = alert.showAndWait();
                        if (result.get() == ButtonType.OK) {
                            Event.fireEvent(main.getNodeById("#signInBtn"), new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                                    0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                                    true, true, true, true, true, true, null));
                        } else {

                        }
                    }
                }
            });
        } else {
            movieDetailSection.getChildren().remove(bookMovieBtn);
            ratingField.setDisable(true);
            commentField.setDisable(true);
            submitCommentBtn.setDisable(true);

        }

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
                    ratingScore = 0;
                } else {
                    isRated = true;
                    ratingScore = starHolderIndex + 1 - 0.5 * (type == "HALF" ? 1 : 0);
                }
            }
        });
    }
    public void commentViewSectionInit() {
        if (!main.getProcessorManager().getMovieManagementProcessor().getMovieManager().getCurrentlyPlayingMovieList().contains(movieOnDetail)){
            announReview.setVisible(true);
            commentViewSection.getChildren().add(announReview);
            return;
        }
        commentViewSection.getChildren().clear();
        ArrayList<ArrayList<String>> reviewFetcher = main.getProcessorManager().getReviewManagementProcessor().getData(0, -1, String.format("M.ID = '%s'", main.getMovieOnDetail().getId()), "R.DATE DESC").getData();
        for (int i=2; i < reviewFetcher.size(); ++i) {
            Double rating = Double.parseDouble(Utils.getRowValueByColumnName(i, "RATING", reviewFetcher));
            Label userCategoryView = new Label(Utils.getRowValueByColumnName(i, "CATEGORY", reviewFetcher));
            userCategoryView.setStyle("-fx-background-color: #8A8A8A; -fx-text-fill: #ffffff;");
            userCategoryView.setFont(Font.font("Georgia", FontWeight.BOLD, 11));
            HBox ratingView = new HBox();
            for (int j=0; j<5; ++j) {
                if (j < Math.floor(rating)) {
                    FontAwesomeIconView fullStar = new FontAwesomeIconView(FontAwesomeIcon.STAR);
                    fullStar.setStroke(Color.WHITE);
                    fullStar.setStyle("-fx-font-family: FontAwesome; -fx-font-size: 20.0; -fx-fill: #FDC500;");
                    ratingView.getChildren().add(fullStar);
                } else if (j < rating){
                    FontAwesomeIconView halfStar = new FontAwesomeIconView(FontAwesomeIcon.STAR_HALF);
                    halfStar.setStroke(Color.WHITE);
                    halfStar.setStyle("-fx-font-family: FontAwesome; -fx-font-size: 20.0; -fx-fill: #FDC500;");
                    ratingView.getChildren().add(halfStar);
                    break;
                }
            }
            Label usernameView = new Label(Utils.getRowValueByColumnName(i, "USERNAME", reviewFetcher));
            usernameView.setFont(Font.font("Georgia", FontWeight.BOLD, 11));
            usernameView.setStyle("-fx-background-color: #8A8A8A; -fx-text-fill: #ffffff");
            HBox commentViewHeader = new HBox(userCategoryView, ratingView, usernameView);
            commentViewHeader.setSpacing(5);
            Line separator = new Line();
            separator.setStroke(Color.WHITE);
            separator.setStartX(-120.5);
            separator.setEndX(735);
            Label commentView = new Label(Utils.getRowValueByColumnName(i, "COMMENT", reviewFetcher));
            commentView.setStyle("-fx-background-color: #000000; -fx-text-fill: #ffffff;");
            commentView.setFont(Font.font("Georgia", FontWeight.BOLD,13));
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
            Response response = main.getProcessorManager().getReviewManagementProcessor().insertData(reviewInfo, true);
            StatusCode status = response.getStatusCode();
            if (status == status.OK) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation");
                alert.setContentText("Your review is uploaded!");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    commentViewSectionInit();
                } else {

                }
            } else {
                response = main.getProcessorManager().getReviewManagementProcessor().updateData(reviewInfo, String.format("MOVIE_ID = '%s' AND USER_ID = '%s'", movieOnDetail.getId(), main.getSignedInUser().getId()), true);
                if (response.getStatusCode() == status.OK) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation");
                    alert.setContentText("Your review is updated!");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK) {
                        commentViewSectionInit();
                    } else {

                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation");
                    alert.setContentText(response.getMessage());
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK) {

                    } else {

                    }
                }
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Failed");
            alert.setContentText("Please sign in to comment!");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                Event.fireEvent(main.getNodeById("#signInBtn"), new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                        0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                        true, true, true, true, true, true, null));
                oldReviewInit();
            } else {

            }
        }
    }
}
