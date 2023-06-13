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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class UpdateAccountFormController implements Initializable {
    private ManagementMain main;
    @FXML
    private ComboBox genderField;
    @FXML
    private VBox updateAccountForm;
    @FXML
    private TextField idField, firstNameField, lastNameField, emailField, usernameField, phoneField, addressField;
    @FXML
    private DatePicker dobField;
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
        statusFieldInit();
        genderInputFieldInit();
        idFieldInit();
    }
    public void genderInputFieldInit() {
        String genders[] = {"M - Male", "F - Female"};
        genderField.setItems(FXCollections.observableArrayList(genders));
    }
    public void idFieldInit() {
        idField.setDisable(true);
    }
    public UpdateAccountFormController() {
        main = ManagementMain.getInstance();
    }
    public void disableUpdateForm() {
        ((AnchorPane)updateAccountForm.getParent()).getChildren().get(0).setVisible(true);
        ((AnchorPane)updateAccountForm.getParent()).getChildren().remove(2);
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
        HashMap<String, String> signUpInfo = new HashMap<String, String>();
        signUpInfo.put("FIRST_NAME", firstNameField.getText());
        signUpInfo.put("LAST_NAME", lastNameField.getText());
        signUpInfo.put("EMAIL", emailField.getText());
        signUpInfo.put("PHONE", phoneField.getText());
        signUpInfo.put("DOB", DateTimeFormatter.ofPattern("yyyy-MM-dd").format(dobField.getValue()));
        signUpInfo.put("ADDRESS", addressField.getText());
        signUpInfo.put("USERNAME", usernameField.getText());
        signUpInfo.put("GENDER", genderField.getValue().toString().substring(0, 1));
        signUpInfo.put("STATUS", statusField.getValue().toString());
        Response response = main.getProcessorManager().getAccountManagementProcessor().updateData(signUpInfo, String.format("ID = '%s'", idField.getText()), true);
        StatusCode signupStatus = response.getStatusCode();
        if (signupStatus == StatusCode.OK) {
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
