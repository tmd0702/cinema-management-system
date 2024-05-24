package com.example.GraphicalUserInterface;

import Utils.Response;
import Utils.StatusCode;
import Utils.Utils;
import Utils.Validator;
import com.example.GraphicalUserInterface.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import Exception.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;

public class ForgotPasswordFormController implements Initializable {
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField confirmPasswordField, passwordField;
    @FXML
    private AnchorPane enterEmailContainer, changePasswordContainer;
    @FXML
    private StackPane forgotPasswordFormRoot;
    private Main main;
    public ForgotPasswordFormController() {
        main = Main.getInstance();
    }
    private ArrayList<ArrayList<String>> accountInfoFetcher;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    @FXML
    public void continueBtnOnClick() {
        String email = emailField.getText();
        if (email.length() == 0) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setContentText("Please enter your email!");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                System.out.println("ok");
            } else {
                System.out.println("cancel");
            }
        } else {
            accountInfoFetcher = main.getProcessorManager().getAccountManagementProcessor().getData(0, 1, String.format("EMAIL = '%s'", email), "").getData();
            if (accountInfoFetcher.size() > 2) {
                enterEmailContainer.setVisible(false);
            } else {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation");
                alert.setContentText("Error: Email is not existed!");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    System.out.println("ok");
                } else {
                    System.out.println("cancel");
                }
            }


        }
    }
    public void disableForm() {
        main.getNodeById("#mainOutlineContentView").setDisable(false);
        ((AnchorPane)forgotPasswordFormRoot.getParent()).getChildren().remove(forgotPasswordFormRoot);
    }
    @FXML
    public void changePasswordConfirmBtnOnClick() {
        try {
            if (passwordField.getText().equals(confirmPasswordField.getText())) {
                HashMap<String, String> newPasswordInfo = new HashMap<String, String>();
                newPasswordInfo.put("PASS", passwordField.getText());
                if (Validator.validatePassword(passwordField.getText())) {
                    Response response = main.getProcessorManager().getAuthenticationManagementProcessor().updateData(newPasswordInfo, String.format("USER_ID = '%s'", Utils.getRowValueByColumnName(2, "USERS.ID", accountInfoFetcher)), true);
                    if (response.getStatusCode() == StatusCode.OK) {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Confirmation");
                        alert.setContentText("Password changed!");
                        Optional<ButtonType> result = alert.showAndWait();
                        if (result.get() == ButtonType.OK) {
                            System.out.println("ok");
                            disableForm();
                        } else {
                            System.out.println("cancel");
                        }
                    } else {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Confirmation");
                        alert.setContentText(response.getMessage());
                        Optional<ButtonType> result = alert.showAndWait();
                        if (result.get() == ButtonType.OK) {
                            System.out.println("ok");
                        } else {
                            System.out.println("cancel");
                        }
                    }
                } else {
                    throw new InvalidPasswordException("Password: " + newPasswordInfo.get("PASS") + " is invalid!");
                }
            } else {
                throw new Exception("Error: Confirmation password must match new password!");
            }
        }
        catch(Exception e) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setContentText(e.getMessage());
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                System.out.println("ok");
            } else {
                System.out.println("cancel");
            }
        }
    }
    @FXML
    public void onXmarkBtnClick(MouseEvent event){
        main.getNodeById("#mainOutlineContentView").setDisable(false);
        ((AnchorPane)forgotPasswordFormRoot.getParent()).getChildren().remove(forgotPasswordFormRoot);
    }
}
