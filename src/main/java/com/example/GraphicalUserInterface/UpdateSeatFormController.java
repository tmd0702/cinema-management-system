package com.example.GraphicalUserInterface;

import Utils.Response;
import Utils.StatusCode;
import Utils.Utils;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class UpdateSeatFormController implements Initializable {
    @FXML
    private TextField idField, nameField;
    @FXML
    private ComboBox statusField, seatCategoryCategoryField;
    @FXML
    private AnchorPane updateSeatForm;
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
        seatCategoryCategoryFieldInit();
    }

    private ArrayList<ArrayList<String>> seatCategoryInfo;
    private ArrayList<String> seatCategoryNames;
    private ManagementMain main;
    public UpdateSeatFormController() {
        main = ManagementMain.getInstance();
    }
    public void idFieldInit() {
        idField.setDisable(true);
    }
    public void seatCategoryCategoryFieldInit() {
        seatCategoryInfo = main.getProcessorManager().getSeatCategoryManagementProcessor().getData(0, -1, "", "").getData();
        seatCategoryNames = Utils.getDataValuesByColumnName(seatCategoryInfo, "SEAT_CATEGORY.CATEGORY");
        seatCategoryCategoryField.setItems(FXCollections.observableArrayList(seatCategoryNames));
    }
    public void disableUpdateForm() {
        ((AnchorPane)updateSeatForm.getParent()).getChildren().get(0).setVisible(true);
        ((AnchorPane)updateSeatForm.getParent()).getChildren().remove(2);
    }
    public String getSeatCategoryObjectIDFromComBoBox(Object value) {
        String id = null;
        for (int i=0; i<seatCategoryNames.size();++i) {
            if (seatCategoryNames.get(i).equals(value)) {
                id = Utils.getRowValueByColumnName(2 + i, "SEAT_CATEGORY.ID", seatCategoryInfo);
                break;
            }
        }
        return id;
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
        HashMap<String, String> seatInfo = new HashMap<String, String>();
        seatInfo.put("SEAT_CATEGORY_ID", getSeatCategoryObjectIDFromComBoBox(seatCategoryCategoryField.getValue()));
        seatInfo.put("NAME", nameField.getText());
        seatInfo.put("ID", idField.getText());
        seatInfo.put("STATUS", statusField.getValue().toString());
        seatInfo.put("SCREEN_ROOM_ID", main.getChosenRoomId());
        Response response = main.getProcessorManager().getSeatManagementProcessor().updateData(seatInfo, String.format("ID = '%s'", idField.getText()), true);
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
