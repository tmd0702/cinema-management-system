package com.example.GraphicalUserInterface;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class UpdateShowTimeFormController implements Initializable {
    @FXML
    private TextField startTimeField;
    @FXML
    private TextField idField;
    @FXML
    private DatePicker showDateField;
    @FXML
    private ComboBox screenRoomNameField, cinemaNameField;
    @FXML
    private VBox updateShowTimeForm;
    private ManagementMain main;
    private ArrayList<ArrayList<String>> cinemaInfo, screenRoomInfo;
    private ArrayList<String> cinemaNames, screenRoomNames;
    public UpdateShowTimeFormController() {
        main = ManagementMain.getInstance();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idFieldInit();
        cinemaNameFieldInit();
    }
    public void idFieldInit() {
        idField.setDisable(true);
    }
    public void disableUpdateForm() {
        ((AnchorPane)updateShowTimeForm.getParent()).getChildren().get(0).setVisible(true);
        ((AnchorPane)updateShowTimeForm.getParent()).getChildren().remove(2);
    }
    @FXML
    public void saveUpdateBtnOnClick() throws IOException {
        handleUpdateRecordRequest();
        disableUpdateForm();
    }
    @FXML
    public void cancelUpdateBtnOnClick() {
        disableUpdateForm();
    }
    public ArrayList<String> getScreenRoomNames() {
        screenRoomNames = new ArrayList<String>();
        for (int i=2; i<screenRoomInfo.size();++i) {
            screenRoomNames.add(screenRoomInfo.get(i).get(1));
        }
        return screenRoomNames;
    }
    public ArrayList<String> getCinemaNames() {
        cinemaNames = new ArrayList<String>();
        for (int i=2; i<cinemaInfo.size();++i) {
            cinemaNames.add(cinemaInfo.get(i).get(1));
        }
        return cinemaNames;
    }
    public void cinemaNameFieldInit() {
        screenRoomNameField.setDisable(true);
        cinemaInfo = main.getCinemaManagementProcessor().getData(0, -1, "", "").getData();
        cinemaNames = getCinemaNames();
        cinemaNameField.setItems(FXCollections.observableArrayList(cinemaNames));
        cinemaNameField.valueProperty().addListener((obs, oldItem, newItem) -> {
            if (newItem == null) {

            } else {
                screenRoomNameFieldInit(getCinemaObjectIDFromComboBox(newItem));
            }
        });
    }
    public void screenRoomNameFieldInit(String currentSelectedCinemaId) {
        screenRoomNameField.setDisable(false);
        screenRoomInfo = main.getScreenRoomManagementProcessor().getData(0, -1, String.format("CINEMA_ID = '%s'", currentSelectedCinemaId), "").getData();
        screenRoomNames = getScreenRoomNames();
        screenRoomNameField.setItems(FXCollections.observableArrayList(screenRoomNames));
    }
    public String getCinemaObjectIDFromComboBox(Object value) {
        String id = null;
        for (int i=0; i<cinemaNames.size();++i) {
            if (cinemaNames.get(i) == value) {
                System.out.println(i + "sdsdsd");
                id = cinemaInfo.get(2 + i).get(0);
                break;
            }
        }
        return id;
    }
    public String getScreenRoomObjectIDFromComboBox(Object value) {
        String id = null;
        for (int i=0; i<screenRoomNames.size();++i) {
            if (screenRoomNames.get(i).equals(value)) {
                id = screenRoomInfo.get(2 + i).get(0);
                break;
            }
        }
        return id;
    }
    public void handleUpdateRecordRequest() {
        HashMap<String, String> showTimeInfo = new HashMap<String, String>();
        showTimeInfo.put("START_TIME", startTimeField.getText());
        showTimeInfo.put("SHOW_DATE", showDateField.getValue().toString());
        showTimeInfo.put("SCREEN_ROOM_ID", getScreenRoomObjectIDFromComboBox(screenRoomNameField.getValue()));

        Response response = main.getShowTimeManagementProcessor().update(showTimeInfo, String.format("ID = '%s'", idField.getText()));
        StatusCode status = response.getStatusCode();
        if (status == StatusCode.OK) {
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
}
