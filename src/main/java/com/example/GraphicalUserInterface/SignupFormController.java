package com.example.GraphicalUserInterface;
import Utils.StatusCode;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import Database.SignupProcessor;

import java.time.format.DateTimeFormatter;
import java.util.*;
public class SignupFormController {
    private SignupProcessor signupProcessor;
    @FXML
    ToggleGroup gender;
    @FXML
    private TextField firstNameField, lastNameField, emailField, usernameField, passwordField, phoneField, addressField;
    @FXML
    private DatePicker dateOfBirthField;
    public SignupFormController() throws Exception {
        this.signupProcessor = new SignupProcessor();
    }
    public void onSubmitSignUpBtn() {
        HashMap<String, String> signUpInfo = new HashMap<String, String>();
        signUpInfo.put("firstName", firstNameField.getText());
        signUpInfo.put("lastName", lastNameField.getText());
        signUpInfo.put("email", emailField.getText());
        signUpInfo.put("phone", phoneField.getText());
        signUpInfo.put("dateOfBirth", DateTimeFormatter.ofPattern("dd-MM-yyyy").format(dateOfBirthField.getValue()));
        signUpInfo.put("address", addressField.getText());
        signUpInfo.put("username", usernameField.getText());
        signUpInfo.put("password", passwordField.getText());
        signUpInfo.put("gender", ((RadioButton)gender.getSelectedToggle()).getText().substring(0, 1));
        StatusCode signupStatus = this.signupProcessor.handleSignupAction(signUpInfo);
        if (signupStatus == StatusCode.OK) {
            Dialog<String> dialog = new Dialog<String>();
            //Setting the title
            dialog.setTitle("Dialog");
            ButtonType type = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
            dialog.setContentText("Sign up done!");
            dialog.getDialogPane().getButtonTypes().add(type);
            dialog.showAndWait();
        } else {

        }
    }
    public void onSignInBtnClick() {

    }
}
