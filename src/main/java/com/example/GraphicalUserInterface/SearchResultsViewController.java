package com.example.GraphicalUserInterface;

import MovieManager.Movie;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class SearchResultsViewController implements Initializable {
    private Main main;
    private ArrayList<HBox> movieContainerList;
    private HashMap<String, Double> searchResults;
    private String filterLanguage, filterGenre, filterSortMethod;
    @FXML
    private AnchorPane headerSection;
    @FXML
    private AnchorPane pageSubContainer;
    @FXML
    private ChoiceBox sortingChoiceBox;
    @FXML
    private ChoiceBox genreChoiceBox;
    @FXML
    private Button filterSearchResultsBtn;
    @FXML
    private ChoiceBox languageChoiceBox;
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
        searchResultsInit();
        choiceBoxesInit();
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
    public void choiceBoxesInit() {
        genreChoiceBox.setItems(FXCollections.observableArrayList(main.getFiltererProcessor().getGenres()));
        languageChoiceBox.setItems(FXCollections.observableArrayList(main.getFiltererProcessor().getLanguages()));
        ArrayList<String> sortingMethods = new ArrayList<String>();
        sortingMethods.add("Relevant");
//        sortingMethods.add("By view count");
//        sortingMethods.add("By rate");
        sortingChoiceBox.setItems(FXCollections.observableArrayList(sortingMethods));
        sortingChoiceBox.setValue("Relevant");
        genreChoiceBox.setValue("All genres");
        languageChoiceBox.setValue("All languages");
    }
    public SearchResultsViewController() {
        main = Main.getInstance();
        movieContainerList = new ArrayList<HBox>();
        searchResults = new HashMap<String, Double>();
        filterLanguage = "";
        filterGenre = "";
        filterSortMethod = "Relevant";
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
    public void filterSearchResultsBtnOnClick() {
        filterGenre = genreChoiceBox.getValue() == "All genres"? "" : (String)genreChoiceBox.getValue();
        filterLanguage = languageChoiceBox.getValue() == "All languages"? "" : (String)languageChoiceBox.getValue();
        filterSortMethod = (String)sortingChoiceBox.getValue();
        displaySearchResults();
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
        resultsContainer.getChildren().clear();
        for (String key : searchResults.keySet()) {
            Movie movie = main.getMovieManagementProcessor().getMovieManager().getMovieById(key);
            if (movie.getGenres().toString().contains(filterGenre) && movie.getLanguage().contains(filterLanguage)) {
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
                movieContainerList.add(movieContainer);
                resultsContainer.getChildren().add(movieContainer);
            }
        }
        headerSection.setPrefHeight(56);
    }
    public void searchResultsInit() {
        searchResults = main.getSearchEngine().getSearchResults(inputField.getText(), "search_engine");//"semantic_searching");
        System.out.println(searchResults);
        resultsContainer.setSpacing(20);
        displaySearchResults();
//        resultsContainer.setPrefHeight(pageContainer.getPrefHeight());
    }
}
