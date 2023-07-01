package com.example.GraphicalUserInterface;

import MovieManager.Movie;
import Utils.Response;
import Utils.StatusCode;
import Utils.Utils;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class AddMovieFormController implements Initializable {
    private ManagementMain main;
    @FXML
    private ComboBox movieStatusField;
    @FXML
    private TextField titleField, overviewField, languageField, durationField, posterPathField, taglineField, backdropPathField;
    @FXML
    private DatePicker releaseDateField;
    @FXML
    private VBox addMovieForm;
    @FXML
    private ComboBox genreField;
    private ArrayList<ArrayList<String>> genreInfo = new ArrayList<ArrayList<String>>();
    private ArrayList<String> genreNames = new ArrayList<String>();

    public AddMovieFormController() throws Exception {
        main = ManagementMain.getInstance();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        movieStatusFieldInit();
        Utils.setDatePickerConstraint(releaseDateField, false);
        genreFieldInit();
    }
    public void movieStatusFieldInit() {
//        String movieStatus[] = {"Planned", "Released"};
//        movieStatusField.setItems(FXCollections.observableArrayList(movieStatus));
        movieStatusField.setValue("Planned");

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
    public void genreFieldInit(){
        genreInfo = main.getProcessorManager().getMovieGenreManagementProcessor().getData(0, -1, "","").getData();
        genreNames = Utils.getDataValuesByColumnName(genreInfo, "GENRES.NAME");
        genreField.setItems(FXCollections.observableArrayList(genreNames));
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
    public void saveInsertBtnOnClick() throws Exception {
        System.out.println("save");
        handleInsertRecordRequest();
        disableInsertForm();
    }
    public void handleInsertRecordRequest() throws Exception {
        HashMap<String, String> movieInfo = new HashMap<String, String>();
        String id = main.getIdGenerator().generateId("MOVIES");
        movieInfo.put("ID", id);
        movieInfo.put("TITLE", titleField.getText());
        movieInfo.put("OVERVIEW", overviewField.getText());
        movieInfo.put("LANGUAGE", languageField.getText());
        movieInfo.put("DURATION", durationField.getText());
        movieInfo.put("RELEASE_DATE", DateTimeFormatter.ofPattern("yyyy-MM-dd").format(releaseDateField.getValue()));
        movieInfo.put("POSTER_PATH", posterPathField.getText());
        movieInfo.put("BACKDROP_PATH", backdropPathField.getText());
        movieInfo.put("REVENUE", "0");

        movieInfo.put("TAGLINE", taglineField.getText());
        movieInfo.put("STATUS", movieStatusField.getValue().toString());
        Response response = main.getProcessorManager().getMovieManagementProcessor().insertData(movieInfo, true);
        StatusCode status = response.getStatusCode();
        if (status == StatusCode.OK) {
            HashMap<String, String> genreMovie = new HashMap<String, String>();
            genreMovie.put("MOVIE_ID", id);
            genreMovie.put("GENRE_ID", getMovieGenreObjectIDFromComboBox(genreField.getValue()));
            main.getProcessorManager().getMovieGenreManagementProcessor().insert(genreMovie, "MOVIE_GENRES", true);
            main.getProcessorManager().getMovieManagementProcessor().scheduleMovie(new Movie(id, titleField.getText(), overviewField.getText(), movieStatusField.getValue().toString(), Integer.parseInt(durationField.getText()), 0, new SimpleDateFormat("yyyy-MM-dd").parse(DateTimeFormatter.ofPattern("yyyy-MM-dd").format(releaseDateField.getValue())), posterPathField.getText(), backdropPathField.getText(), languageField.getText(), 0.0f));
            int countSchedule = main.getProcessorManager().getScheduleManagementProcessor().countData(String.format("MOVIES.ID = '%s'",id));
            Dialog<String> dialog = new Dialog<String>();
            //Setting the title
            dialog.setTitle("Success");
            ButtonType type = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
            System.out.println("Count schedule ..........." + countSchedule);
            if(countSchedule == 0){
                dialog.setContentText("The record has been added successfully but have not scheduled for this movie");
            }else {
                dialog.setContentText("The record has been added successfully");
            }
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
