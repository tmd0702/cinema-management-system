package com.example.GraphicalUserInterface;
import Database.SigninProcessor;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.util.*;
import Utils.StatusCode;
public class LoginFormController {
    private SigninProcessor signinProcessor;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    public LoginFormController() {
        this.signinProcessor = new SigninProcessor();
    }
    public void onLoginSubmitBtnClick() {
        HashMap<String, String> signinInfo = new HashMap<String, String>();
        signinInfo.put("username", this.usernameField.getText());
        signinInfo.put("password", this.passwordField.getText());
        StatusCode signinStatus =  signinProcessor.handleSigninAction(signinInfo);
        if (signinStatus == StatusCode.OK) {

        } else {

        }
    }
    public void onSignUpBtnClick() {

    }
}
