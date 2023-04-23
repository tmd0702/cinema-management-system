package com.example.GraphicalUserInterface;

import MovieManager.Movie;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class SearchResultsViewController implements Initializable {
    private Main main;
    @FXML
    private TextField inputField;
    @FXML
    private ImageView logoImageView;
    @FXML
    private VBox resultsContainer;
    @FXML
    private ScrollPane pageContainer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        inputFieldInit();
        logoImageViewInit();
        displaySearchResults();
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
    public void inputFieldInit() {
        inputField.setText(main.getQueryOnSearching());
    }
    public SearchResultsViewController() {
        main = Main.getInstance();
    }
    @FXML
    public void onSearchFieldEnterKeyPress() throws IOException {
        main.setQueryOnSearching(inputField.getText());
        main.changeScene("search-results-view.fxml");
    }
    @FXML
    public void logoImageViewOnClick() throws IOException {
        main.changeScene("index-view.fxml");
    }
    @FXML
    public void onSignInBtnClick() throws IOException {
        this.main.popup("login-form.fxml");
    }
    @FXML
    public void onSignUpBtnClick() throws IOException {
        this.main.popup("signup-form.fxml");
    }
    public void displaySearchResults() {
        HashMap<String, Object> searchResults = main.getSearchEngine().getSearchResults(inputField.getText(), "semantic_searching");
        resultsContainer.setSpacing(20);
        for (String key : searchResults.keySet()) {
            if (((BigDecimal)searchResults.get(key)).compareTo(BigDecimal.valueOf(0.4)) > 0) {
                Movie movie = main.getMovieManagementProcessor().getMovieManager().getMovieById(key);
                HBox movieContainer = new HBox();
                movieContainer.setPrefHeight(220);
                movieContainer.setPrefWidth(resultsContainer.getPrefWidth());
                movieContainer.setStyle("-fx-background-color: black;");
                ImageView poster = new ImageView();
                poster.setPreserveRatio(true);
                poster.setFitWidth(resultsContainer.getPrefWidth() / 3.2);
                poster.setFitHeight(movieContainer.getPrefHeight());
                if (movie.getPosterImage().getProgress() == 1 && !movie.getPosterImage().isError()) {
                    poster.setImage(movie.getPosterImage());
                } else {
                    poster.setImage(main.getMovieManagementProcessor().getMovieManager().getImageNotFound());
                }
                VBox movieContentInfo = new VBox();
                movieContentInfo.setPadding(new Insets(20, 20, 20, 20));
                movieContentInfo.setPrefHeight(movieContainer.getPrefHeight());
                Label title = new Label(movie.getTitle());
                Label overview = new Label("Overview: " + movie.getOverview());
                Label releaseDate = new Label("Release date: " + movie.getReleaseDate().toString());
                Label duration = new Label("Duration: " + movie.getDuration() + " minutes");
                title.setStyle("-fx-text-fill: white; -fx-font-size: 24px; -fx-font-weight: bold;");
                overview.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
                releaseDate.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
                duration.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

                movieContentInfo.getChildren().add(title);
                movieContentInfo.getChildren().add(releaseDate);
                movieContentInfo.getChildren().add(duration);
                movieContentInfo.getChildren().add(overview);
                movieContainer.getChildren().add(poster);
                movieContainer.getChildren().add(movieContentInfo);
                resultsContainer.getChildren().add(movieContainer);

            }
        }
//        resultsContainer.setPrefHeight(pageContainer.getPrefHeight());
    }
}
