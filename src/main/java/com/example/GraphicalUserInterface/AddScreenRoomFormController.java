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
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;

public class AddScreenRoomFormController implements Initializable {
    private ManagementMain main;
    @FXML
    private ComboBox cinemaNameField;
    @FXML
    private TextField nameField, capacityField;
    @FXML
    private VBox addScreenRoomForm;
    public AddScreenRoomFormController() throws Exception {
        main = ManagementMain.getInstance();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        categoryFieldInit();
    }
    public void categoryFieldInit() {
        String movieStatus[] = {"Popcorn", "Drink"};
        cinemaNameField.setItems(FXCollections.observableArrayList(movieStatus));
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
        ((AnchorPane)addScreenRoomForm.getParent()).getChildren().get(0).setVisible(true);
        ((AnchorPane)addScreenRoomForm.getParent()).getChildren().remove(2);
    }
    @FXML
    public void saveInsertBtnOnClick() throws IOException {
        System.out.println("save");
        handleInsertRecordRequest();
        disableInsertForm();
    }
    public void handleInsertRecordRequest() {
        HashMap<String, String> screenRoomInfo = new HashMap<String, String>();
        screenRoomInfo.put("NAME", nameField.getText());
        screenRoomInfo.put("CINEMA_ID", cinemaNameField.getValue().toString());
        screenRoomInfo.put("CAPACITY", capacityField.getText());
        Response response = main.getScreenRoomManagementProcessor().add(screenRoomInfo);
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
