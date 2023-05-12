package com.example.GraphicalUserInterface;

import Utils.IdGenerator;
import Utils.Response;
import Utils.StatusCode;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;

public class AddMovieFormController implements Initializable {
    private ManagementMain main;
    @FXML
    private ComboBox movieStatusField;
    @FXML
    private TextField titleField, overviewField, languageField, durationField, posterPathField, viewCountField, revenueField, taglineField, voteCountField, voteAverageField, backdropPathField;
    @FXML
    private DatePicker releaseDateField;
    @FXML
    private VBox addMovieForm;

    public AddMovieFormController() throws Exception {
        main = new ManagementMain();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        movieStatusFieldInit();
    }
    public void movieStatusFieldInit() {
        String movieStatus[] = {"Planned", "Released"};
        movieStatusField.setItems(FXCollections.observableArrayList(movieStatus));
    }
    @FXML
    public void cancelInsertBtnOnClick() {
//        DialogPane cancelConfirmation = new DialogPane();
        System.out.println("cancel");
        cancelInsertConfirmationAlert("Are you sure to ged rid of this record?");
    }
    public void cancelInsertConfirmationAlert(String contentText) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setContentText(contentText);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            System.out.println("ok");
            disableInsertForm();
        } else {
            System.out.println("cancel");
        }
    }
    public void disableInsertForm() {
        ((AnchorPane)addMovieForm.getParent()).getChildren().get(0).setVisible(true);
        ((AnchorPane)addMovieForm.getParent()).getChildren().remove(2);
    }
    @FXML
    public void saveInsertBtnOnClick() throws IOException {
        System.out.println("save");
        handleInsertRecordRequest();
        disableInsertForm();
    }
    public void handleInsertRecordRequest() {
        HashMap<String, String> movieInfo = new HashMap<String, String>();
        movieInfo.put("ID", main.getIdGenerator().generateId("MOVIES"));
        movieInfo.put("TITLE", titleField.getText());
        movieInfo.put("OVERVIEW", overviewField.getText());
        movieInfo.put("LANGUAGE", languageField.getText());
        movieInfo.put("DURATION", durationField.getText());
        movieInfo.put("RELEASE_DATE", DateTimeFormatter.ofPattern("yyyy-MM-dd").format(releaseDateField.getValue()));
        movieInfo.put("POSTER_PATH", posterPathField.getText());
        movieInfo.put("BACKDROP_PATH", backdropPathField.getText());
        movieInfo.put("VIEW_COUNT", viewCountField.getText());
        movieInfo.put("REVENUE", revenueField.getText());
        movieInfo.put("TAGLINE", taglineField.getText());
        movieInfo.put("VOTE_COUNT", voteCountField.getText());
        movieInfo.put("VOTE_AVERAGE", voteAverageField.getText());
        movieInfo.put("MOVIE_STATUS", movieStatusField.getValue().toString());
        Response response = main.getMovieManagementProcessor().add(movieInfo);
        StatusCode status = response.getStatusCode();
        if (status == StatusCode.OK) {
            Dialog<String> dialog = new Dialog<String>();
            //Setting the title
            dialog.setTitle("Success");
            ButtonType type = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
            dialog.setContentText("The record has been added successfully");
            dialog.getDialogPane().getButtonTypes().add(type);
            dialog.showAndWait();
        } else {
            Dialog<String> dialog = new Dialog<String>();
            //Setting the title
            dialog.setTitle("Failed");
            ButtonType type = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
            dialog.setContentText(response.getMessage());
            dialog.getDialogPane().getButtonTypes().add(type);
            dialog.showAndWait();
        }
    }

}
