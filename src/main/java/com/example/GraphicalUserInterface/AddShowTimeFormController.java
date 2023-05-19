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
import java.util.Optional;
import java.util.ResourceBundle;

public class AddShowTimeFormController implements Initializable {
    @FXML
    private TextField startTimeField;
    @FXML
    private DatePicker showDateField;
    @FXML
    private ComboBox screenRoomNameField, cinemaNameField;
    @FXML
    private VBox addShowTimeForm;
    private ManagementMain main;
    private ArrayList<ArrayList<String>> cinemaInfo, screenRoomInfo;
    private ArrayList<String> cinemaNames, screenRoomNames;
    public AddShowTimeFormController() throws Exception {
        main = ManagementMain.getInstance();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cinemaNameFieldInit();
    }

    public ArrayList<String> getCinemaNames() {
        cinemaNames = new ArrayList<String>();
        for (int i=2; i<cinemaInfo.size();++i) {
            cinemaNames.add(cinemaInfo.get(i).get(1));
        }
        return cinemaNames;
    }
    public ArrayList<String> getScreenRoomNames() {
        screenRoomNames = new ArrayList<String>();
        for (int i=2; i<screenRoomInfo.size();++i) {
            screenRoomNames.add(screenRoomInfo.get(i).get(1));
        }
        return screenRoomNames;
    }
    public void screenRoomNameFieldInit(String currentSelectedCinemaId) {
        screenRoomNameField.setDisable(false);
        screenRoomInfo = main.getScreenRoomManagementProcessor().getData(0, -1, String.format("CINEMA_ID = '%s'", currentSelectedCinemaId), "").getData();
        screenRoomNames = getScreenRoomNames();
        screenRoomNameField.setItems(FXCollections.observableArrayList(screenRoomNames));
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
    @FXML
    public void cancelInsertBtnOnClick() {
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
        ((AnchorPane)addShowTimeForm.getParent()).getChildren().get(0).setVisible(true);
        ((AnchorPane)addShowTimeForm.getParent()).getChildren().remove(2);
    }
    @FXML
    public void saveInsertBtnOnClick() throws IOException {
        handleInsertRecordRequest();
        disableInsertForm();
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
    public String getCinemaObjectIDFromComboBox(Object value) {
        String id = null;
        for (int i=0; i<cinemaNames.size();++i) {
            if (cinemaNames.get(i) == value) {
                id = cinemaInfo.get(2 + i).get(0);
                break;
            }
        }
        return id;
    }
    public void handleInsertRecordRequest() {
        HashMap<String, String> showTimeInfo = new HashMap<String, String>();
        showTimeInfo.put("ID", main.getIdGenerator().generateId(main.getShowTimeManagementProcessor().getDefaultDatabaseTable()));
        showTimeInfo.put("START_TIME", startTimeField.getText());
        showTimeInfo.put("SHOW_DATE", showDateField.getValue().toString());
        showTimeInfo.put("SCREEN_ROOM_ID", getScreenRoomObjectIDFromComboBox(screenRoomNameField.getValue()));
        Response response = main.getShowTimeManagementProcessor().add(showTimeInfo);
        StatusCode signupStatus = response.getStatusCode();
        if (signupStatus == StatusCode.OK) {
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
