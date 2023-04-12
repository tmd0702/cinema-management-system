package com.example.GraphicalUserInterface;
import Database.SigninProcessor;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.util.*;
public class LoginFormController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    public void onLoginSubmitBtnClick() {
        HashMap<String, String> signinInfo = new HashMap<String, String>();
        signinInfo.put("username", this.usernameField.getText());
        signinInfo.put("password", this.passwordField.getText());

    }
}
