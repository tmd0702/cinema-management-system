package com.example.GraphicalUserInterface;

import MovieManager.Movie;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MovieDetailViewController implements Initializable {
    @FXML
    private ImageView moviePosterSection;
    private Movie movieOnDetail;
    private Main main;
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
        this.main.popup("login-form.fxml");
    }
    @FXML
    public void onSignUpBtnClick() throws IOException {
        this.main.popup("signup-form.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        logoImageViewInit();
        movieDetailSectionInit();
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
    }
}
