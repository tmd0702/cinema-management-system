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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class UpdateScreenRoomFormController implements Initializable {
    private ManagementMain main;
    private ArrayList<ArrayList<String>> cinemaInfo;
    private ArrayList<String> cinemaNames;
    @FXML
    private TextField idField, nameField, capacityField;
    @FXML
    private ComboBox cinemaNameField;
    @FXML
    private VBox updateScreenRoomForm;
    @FXML
    private ComboBox statusField;
    public void statusFieldInit() {
        ArrayList<String> statusList = new ArrayList<String>();
        statusList.add("AVAILABLE");
        statusList.add("SUSPEND");
        statusList.add("CLOSED");
        statusField.setItems(FXCollections.observableArrayList(statusList));
    }
    public UpdateScreenRoomFormController() {
        main = ManagementMain.getInstance();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idFieldInit();
        cinemaNameFieldInit();
        statusFieldInit();
    }
    public void idFieldInit() {
        idField.setDisable(true);
    }
    public void disableUpdateForm() {
        ((AnchorPane)updateScreenRoomForm.getParent()).getChildren().get(0).setVisible(true);
        ((AnchorPane)updateScreenRoomForm.getParent()).getChildren().remove(2);
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
    public ArrayList<String> getCinemaNames() {
        cinemaNames = new ArrayList<String>();
        for (int i=2; i<cinemaInfo.size();++i) {
            cinemaNames.add(cinemaInfo.get(i).get(1));
        }
        return cinemaNames;
    }
    public void cinemaNameFieldInit() {
        cinemaInfo = main.getProcessorManager().getCinemaManagementProcessor().getData(0, -1, "", "").getData();
        cinemaNames = getCinemaNames();
        cinemaNameField.setItems(FXCollections.observableArrayList(cinemaNames));
    }
    public String getCinemaObjectIDFromComboBox(Object value) {
        String id = null;
        for (int i=0; i<cinemaNames.size();++i) {
            if (cinemaNames.get(i) == value) {
                System.out.println(i + "sdsdsd");
                id = Utils.getRowValueByColumnName(2 + i, "ID", cinemaInfo);
                break;
            }
        }
        return id;
    }
    public void handleUpdateRecordRequest() {
        HashMap<String, String> screenRoomInfo = new HashMap<String, String>();
        screenRoomInfo.put("NAME", nameField.getText());
        screenRoomInfo.put("CAPACITY", capacityField.getText());
        screenRoomInfo.put("CINEMA_ID", getCinemaObjectIDFromComboBox(cinemaNameField.getValue()));
        screenRoomInfo.put("STATUS", statusField.getValue().toString());
        Response response = main.getProcessorManager().getScreenRoomManagementProcessor().updateData(screenRoomInfo, String.format("ID = '%s'", idField.getText()), true);
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
