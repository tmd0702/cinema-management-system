package com.example.GraphicalUserInterface;

import MovieManager.Movie;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;

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
    private Label cast;
    @FXML
    private Line separator;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        movieDetailSection.setSpacing(10);
        moviePosterSection.setImage(new Image(movieOnDetail.getPosterPath()));
        title.setText(movieOnDetail.getTitle());
        releaseDate.setText(releaseDate.getText() + movieOnDetail.getProductDate().toString());
        duration.setText(duration.getText() + Integer.toString(movieOnDetail.getDuration()));
        VBox.setMargin(separator, new Insets(20, 0, 20, 0));
        description.setWrapText(true);
        description.setText(movieOnDetail.getContent());
    }
}
