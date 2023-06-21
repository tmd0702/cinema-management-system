package com.example.GraphicalUserInterface;

import Utils.Response;
import Utils.StatusCode;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;
import com.example.GraphicalUserInterface.SeatMapController;

public class AddSeatFromController implements Initializable {
    @FXML
    private TextField nameInsertField, categoryInsertFiled;
    @FXML
    private ComboBox statusInsertField;
    private ManagementMain main;
    @FXML
    private AnchorPane addSeatForm;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {createStatus();}
    public void createStatus(){
        ArrayList<String> statusList = new ArrayList<String>();
        statusList.add("AVAILABLE");
        statusList.add("SUSPEND");
        statusList.add("CLOSED");
        statusInsertField.setItems(FXCollections.observableArrayList(statusList));
        categoryInsertFiled.setText("NORMAl");
        main.getSeatMapController().getSeatGrid().getChildren().indexOf(main.getSeatBtnSelected().get(0));
    }
    public AddSeatFromController() throws Exception {
        main = ManagementMain.getInstance();
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
        ((AnchorPane)addSeatForm.getParent()).getChildren().get(0).setVisible(true);
        ((AnchorPane)addSeatForm.getParent()).getChildren().remove(2);
    }
    @FXML
    public void saveInsertBtnOnClick() throws IOException {
        System.out.println("save");
        handleInsertRecordRequest();
        disableInsertForm();
    }
    public void handleInsertRecordRequest() {

        String seatId = main.getIdGenerator().generateId(main.getProcessorManager().getSeatManagementProcessor().getDefaultDatabaseTable());
        HashMap<String, String> seatInfo = new HashMap<String, String>();
        seatInfo.put("SEAT_CATEGORY_ID", "SC_00001");
        seatInfo.put("ID", seatId);
        seatInfo.put("SCREEN_ROOM_ID", main.getSeatMapController().getRoomId());
        Response response = main.getProcessorManager().getSeatManagementProcessor().insertData(seatInfo, true);
        StatusCode status = response.getStatusCode();
        if (status == StatusCode.OK) {
            Dialog<String> dialog = new Dialog<String>();
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
        this.main.setSeatBtnSelected(new ArrayList<Button>());
        this.main.setSeatIdSelected(new ArrayList<String>());
    }
}
