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
import javafx.scene.layout.AnchorPane;
import Exception.InvalidPasswordException;

import java.net.URL;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;

public class ChangePasswordFormController implements Initializable {
    private Main main;
    @FXML
    private PasswordField oldPasswordField, newPasswordField, confirmPasswordField;
    @FXML
    private AnchorPane profileViewContainer, changePasswordFormContainer;
    public ChangePasswordFormController() {
        main = Main.getInstance();
        profileViewContainer = (AnchorPane) main.getNodeById("#profileViewContainer");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    public void disableForm() {
        profileViewContainer.getChildren().remove(changePasswordFormContainer);
    }
    @FXML
    public void changePasswordConfirmBtnOnClick() {
        try {
            if (oldPasswordField.getText().equals(Utils.getRowValueByColumnName(2, "PASS", main.getProcessorManager().getAuthenticationManagementProcessor().getData(0, 1, String.format("USER_ID = %s", main.getSignedInUser().getId()), "").getData()))) {
                if (newPasswordField.getText().equals(confirmPasswordField.getText())) {
                    HashMap<String, String> newPasswordInfo = new HashMap<String, String>();
                    newPasswordInfo.put("PASS", newPasswordField.getText());
                    if (Validator.validatePassword(newPasswordField.getText())) {
                        Response response = main.getProcessorManager().getAuthenticationManagementProcessor().updateData(newPasswordInfo, String.format("USER_ID = '%s'", main.getSignedInUser().getId()), true);
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
            } else {
                throw new Exception("Error: Old password is not matched!");
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
    public void closeBtnOnClick() {
        profileViewContainer.getChildren().remove(changePasswordFormContainer);
    }
}
