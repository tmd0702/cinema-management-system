package com.example.GraphicalUserInterface;

import Utils.Response;
import Utils.StatusCode;
import Utils.Utils;
import com.example.GraphicalUserInterface.ManagementMain;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class UpdateMovieFormController implements Initializable {
    private ManagementMain main;
    @FXML
    private ComboBox movieStatusField;
    @FXML
    private TextField idField, titleField, overviewField, languageField, durationField, posterPathField, taglineField, backdropPathField;
    @FXML
    private DatePicker releaseDateField;
    @FXML
    private ComboBox genreField;
    private ArrayList<ArrayList<String>> genreInfo = new ArrayList<ArrayList<String>>();
    private ArrayList<String> genreNames = new ArrayList<String>();

    @FXML
    private VBox updateMovieForm;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idFieldInit();
        movieStatusFieldInit();
        Utils.setDatePickerConstraint(releaseDateField, false);
        genreFieldInit();
    }
    public void movieStatusFieldInit() {
        String movieStatus[] = {"Planned", "Released"};
        movieStatusField.setItems(FXCollections.observableArrayList(movieStatus));
    }
    public void idFieldInit() {
        idField.setDisable(true);
    }
    public UpdateMovieFormController() {
        main = ManagementMain.getInstance();
    }
    public void disableUpdateForm() {
        ((AnchorPane)updateMovieForm.getParent()).getChildren().get(0).setVisible(true);
        ((AnchorPane)updateMovieForm.getParent()).getChildren().remove(2);
    }
    @FXML
    public void saveUpdateBtnOnClick() throws IOException {
        System.out.println("save");
        handleUpdateRecordRequest();
        disableUpdateForm();
    }
    @FXML
    public void cancelUpdateBtnOnClick() {
//        DialogPane cancelConfirmation = new DialogPane();
        System.out.println("cancel");
        disableUpdateForm();
    }
    public void handleUpdateRecordRequest() {
        HashMap<String, String> movieInfo = new HashMap<String, String>();
        movieInfo.put("ID", main.getIdGenerator().generateId("MOVIES"));
        movieInfo.put("TITLE", titleField.getText());
        movieInfo.put("OVERVIEW", overviewField.getText());
        movieInfo.put("LANGUAGE", languageField.getText());
        movieInfo.put("DURATION", durationField.getText());
        movieInfo.put("RELEASE_DATE", DateTimeFormatter.ofPattern("yyyy-MM-dd").format(releaseDateField.getValue()));
        movieInfo.put("POSTER_PATH", posterPathField.getText());
        movieInfo.put("BACKDROP_PATH", backdropPathField.getText());
        movieInfo.put("VIEW_COUNT", "0");
        movieInfo.put("REVENUE", "0");
        movieInfo.put("TAGLINE", taglineField.getText());
        movieInfo.put("VOTE_COUNT", "0");
        movieInfo.put("VOTE_AVERAGE", "0");
        movieInfo.put("STATUS", movieStatusField.getValue().toString());
        Response response = main.getProcessorManager().getMovieManagementProcessor().updateData(movieInfo, String.format("ID = '%s'", idField.getText()), true);
        StatusCode status = response.getStatusCode();
        if (status == StatusCode.OK) {
            HashMap<String, String> genreMovie = new HashMap<String, String>();
            genreMovie.put("MOVIE_ID", idField.getText());
            genreMovie.put("GENRE_ID", getMovieGenreObjectIDFromComboBox(genreField.getValue()));
            main.getProcessorManager().getMovieGenreManagementProcessor().insert(genreMovie, "MOVIE_GENRES", true);
            Dialog<String> dialog = new Dialog<String>();
            //Setting the title
            dialog.setTitle("Success");
            ButtonType type = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
            dialog.setContentText("The record has been updated successfully");
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
    public void genreFieldInit(){
        genreInfo = main.getProcessorManager().getMovieGenreManagementProcessor().getData(0, -1, "","").getData();
        genreNames = Utils.getDataValuesByColumnName(genreInfo, "GENRES.NAME");
        genreField.setItems(FXCollections.observableArrayList(genreNames));
    }
    public String getMovieGenreObjectIDFromComboBox(Object value) {
        String id = null;
        for (int i=0; i<genreNames.size();++i) {
            if (genreNames.get(i).equals(value)) {
                id = Utils.getRowValueByColumnName(i + 2, "GENRES.ID", genreInfo);
                break;
            }
        }
        return id;
    }
}
