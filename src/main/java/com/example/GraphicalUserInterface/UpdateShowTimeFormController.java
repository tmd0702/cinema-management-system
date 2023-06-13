package com.example.GraphicalUserInterface;

import Utils.Response;
import Utils.StatusCode;
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

public class UpdateShowTimeFormController implements Initializable {
    @FXML
    private TextField startTimeField;
    @FXML
    private TextField idField;
    @FXML
    private VBox updateShowTimeForm;
    private ManagementMain main;
    private ArrayList<ArrayList<String>> cinemaInfo, screenRoomInfo;
    private ArrayList<String> cinemaNames, screenRoomNames;
    public UpdateShowTimeFormController() {
        main = ManagementMain.getInstance();
    }
    @FXML
    private ComboBox statusField;
    public void statusFieldInit() {
        ArrayList<String> statusList = new ArrayList<String>();
        statusList.add("AVAILABLE");
        statusList.add("SUSPEND");
        statusList.add("CLOSED");
        statusField.setItems(FXCollections.observableArrayList(statusList));
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idFieldInit();
        statusFieldInit();
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
    public void handleUpdateRecordRequest() {
        HashMap<String, String> showTimeInfo = new HashMap<String, String>();
        showTimeInfo.put("START_TIME", startTimeField.getText());
        showTimeInfo.put("STATUS", statusField.getValue().toString());
        Response response = main.getProcessorManager().getShowTimeManagementProcessor().updateData(showTimeInfo, String.format("ID = '%s'", idField.getText()), true);
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
