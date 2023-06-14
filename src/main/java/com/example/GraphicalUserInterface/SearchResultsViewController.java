package com.example.GraphicalUserInterface;

import MovieManager.Movie;
import com.example.GraphicalUserInterface.Main;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.json.JSONObject;

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
    private ChoiceBox sortingChoiceBox;
    @FXML
    private ChoiceBox genreChoiceBox;
    @FXML
    private Button filterSearchResultsBtn;
    @FXML
    private ChoiceBox languageChoiceBox;
    @FXML
    private VBox resultsContainer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        searchResultsInit();
        choiceBoxesInit();
    }
    public void choiceBoxesInit() {
        genreChoiceBox.setItems(FXCollections.observableArrayList(main.getProcessorManager().getFiltererProcessor().getGenres()));
        languageChoiceBox.setItems(FXCollections.observableArrayList(main.getProcessorManager().getFiltererProcessor().getLanguages()));
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
    public void filterSearchResultsBtnOnClick() {
        filterGenre = genreChoiceBox.getValue() == "All genres"? "" : (String)genreChoiceBox.getValue();
        filterLanguage = languageChoiceBox.getValue() == "All languages"? "" : (String)languageChoiceBox.getValue();
        filterSortMethod = (String)sortingChoiceBox.getValue();
        displaySearchResults();
    }
    public void displaySearchResults() {
        resultsContainer.getChildren().clear();
        for (String key : searchResults.keySet()) {
            Movie movie = main.getProcessorManager().getMovieManagementProcessor().getMovieManager().getMovieById(key);
            if (movie != null && movie.getGenres().toString().contains(filterGenre) && movie.getLanguage().contains(filterLanguage)) {
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
                    poster.setImage(main.getProcessorManager().getMovieManagementProcessor().getMovieManager().getImageNotFound());
                }
                VBox movieContentInfo = new VBox();
                movieContentInfo.setPadding(new Insets(0, 20, 20, 20));
                movieContentInfo.setSpacing(10);
                movieContentInfo.setPrefHeight(movieContainer.getPrefHeight());
                Label title = new Label(movie.getTitle());
                Label overview = new Label("Overview: " + movie.getOverview());
                Label releaseDate = new Label("Release date: " + movie.getReleaseDate().toString());
                Label duration = new Label("Duration: " + movie.getDuration() + " minutes");
                Label rating = new Label("Rate:");
                rating.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
                HBox ratingStarSection = new HBox();
                float voteAverage = movie.getVoteAverage() / 2;
                for (int j=0; j<5; ++j) {
                    if (j < Math.floor(voteAverage)) {
                        FontAwesomeIconView fullStar = new FontAwesomeIconView(FontAwesomeIcon.STAR);
                        fullStar.setStroke(Color.WHITE);
                        fullStar.setStyle("-fx-font-family: FontAwesome; -fx-font-size: 20.0; -fx-fill: #FDC500;");
                        ratingStarSection.getChildren().add(fullStar);
                    } else if (j < voteAverage) {
                        FontAwesomeIconView halfStar = new FontAwesomeIconView(FontAwesomeIcon.STAR_HALF);
                        halfStar.setStroke(Color.WHITE);
                        halfStar.setStyle("-fx-font-family: FontAwesome; -fx-font-size: 20.0; -fx-fill: #FDC500;");
                        ratingStarSection.getChildren().add(halfStar);
                        break;
                    }
                }
                HBox movieRatingView = new HBox(rating, ratingStarSection);
                movieRatingView.setSpacing(10);
                Label language = new Label("Language: " + movie.getLanguage());
                Label genres = new Label("Genres: " + movie.getGenresString());

                movieContentInfo.getChildren().add(title);
                movieContentInfo.getChildren().add(releaseDate);
                movieContentInfo.getChildren().add(duration);
                movieContentInfo.getChildren().add(overview);
                movieContentInfo.getChildren().add(movieRatingView);
                movieContentInfo.getChildren().add(language);
                movieContentInfo.getChildren().add(genres);

                for (Node movieContent : movieContentInfo.getChildren()) {
                    if (movieContent instanceof Label) {
                        movieContent.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
                        ((Label) movieContent).setFont(Font.font("Georgia"));
                    }
                }
                title.setStyle("-fx-text-fill: white; -fx-font-size: 24px; -fx-font-weight: bold;");
                movieContainer.getChildren().add(poster);
                movieContainer.getChildren().add(movieContentInfo);
                poster.setOnMouseClicked(new EventHandler<MouseEvent>()  {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        main.setMovieOnDetail(movie);
                        //main.setMovieOnDetail(new Movie("MOV1000", "Toy Story", "Led by Woody, Andy's toys live happily in his room until Andy's birthday brings Buzz Lightyear onto the scene. Afraid of losing his place in Andy's heart, Woody plots against Buzz. But when circumstances separate Buzz and Woody from their owner, the duo eventually learns to put aside their differences.", "ASDS", 100, 90, new Date(), "https://image.tmdb.org/t/p/original/7G9915LfUQ2lVfwMEEhDsn3kT4B.jpg", "https://image.tmdb.org/t/p/original/9FBwqcd9IRruEDUrTdcaafOMKUq.jpg"));
                        try {
                            main.changeView("movie-detail-view.fxml");
                        } catch (IOException e) {
                            System.out.println(e);
                        }
                    }
                });
                movieContainerList.add(movieContainer);
                resultsContainer.getChildren().add(movieContainer);
            }
        }
    }
    public void searchResultsInit() {
//        searchResults = main.getSearchEngine().getSearchResults(((TextField) main.getNodeById("#inputField")).getText(), "search_engine");//"semantic_searching");
//        System.out.println("results " + searchResults);
//        resultsContainer.setSpacing(20);
//        displaySearchResults();
//        resultsContainer.setPrefHeight(pageContainer.getPrefHeight());
        JSONObject jsonData = new JSONObject();
        jsonData.put("input", ((TextField) main.getNodeById("#inputField")).getText());
        searchResults = main.getConnector().HTTPSearchEngineRequest(jsonData);
        System.out.println("results " + searchResults);
        resultsContainer.setSpacing(20);
        displaySearchResults();
//        resultsContainer.setPrefHeight(pageContainer.getPrefHeight());
    }
}
