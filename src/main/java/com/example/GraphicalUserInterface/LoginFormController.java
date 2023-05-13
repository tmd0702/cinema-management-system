package com.example.GraphicalUserInterface;
import UserManager.Subscriber;
import Utils.Response;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.util.*;
import Utils.StatusCode;
public class LoginFormController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    public LoginFormController() {
    }
    public void onLoginSubmitBtnClick() {
        HashMap<String, String> signinInfo = new HashMap<String, String>();
        signinInfo.put("username", this.usernameField.getText());
        signinInfo.put("password", this.passwordField.getText());
        Response response =  ManagementMain.getInstance().getAccountManagementProcessor().handleSigninAction(signinInfo);
        StatusCode signinStatus = response.getStatusCode();
        if (signinStatus == StatusCode.OK) {
            System.out.println("Sign in success");
            Main.getInstance().setSignedInUser(new Subscriber());
        } else {
            System.out.println("Sign in failed");
        }
    }
    public void onSignUpBtnClick() {

    }
}