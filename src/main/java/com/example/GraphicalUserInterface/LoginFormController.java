package com.example.GraphicalUserInterface;
import UserManager.Customer;
import UserManager.Manager;
import Utils.Response;
import Utils.Utils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;
import Utils.StatusCode;
import javafx.scene.layout.AnchorPane;

public class LoginFormController {
    @FXML
    private AnchorPane loginFormRoot;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    private Main main;
    public LoginFormController() {
        main = Main.getInstance();
    }
    public void disableForm() {
        ((AnchorPane)loginFormRoot.getParent()).getChildren().get(0).setDisable(false);
        ((AnchorPane)loginFormRoot.getParent()).getChildren().remove(loginFormRoot);
    }
    public void onLoginSubmitBtnClick() throws ParseException {
        HashMap<String, String> signinInfo = new HashMap<String, String>();
        signinInfo.put("username", this.usernameField.getText());
        signinInfo.put("password", this.passwordField.getText());
        Response response =  ManagementMain.getInstance().getAccountManagementProcessor().handleSigninAction(signinInfo);
        StatusCode signinStatus = response.getStatusCode();
        if (signinStatus == StatusCode.OK) {
            ArrayList<ArrayList<String>> userInfo = response.getData();
            System.out.println("Sign in success");
            Main.getInstance().setSignedInUser(new Manager(Utils.getRowValueByColumnName(2, "USERNAME", userInfo), Utils.getRowValueByColumnName(2, "ID", userInfo), Utils.getRowValueByColumnName(2, "FIRST_NAME", userInfo), Utils.getRowValueByColumnName(2, "LAST_NAME", userInfo), new Date(new SimpleDateFormat("yyyy-MM-dd").parse(Utils.getRowValueByColumnName(2, "DOB", userInfo)).getTime()), Utils.getRowValueByColumnName(2, "PHONE", userInfo), Utils.getRowValueByColumnName(2, "EMAIL", userInfo)));
            disableForm();
        } else {
            System.out.println("Sign in failed");
        }
    }
    public void onSignUpBtnClick() throws Exception {
        ((AnchorPane)loginFormRoot.getParent()).getChildren().add(FXMLLoader.load(getClass().getResource("signup-form.fxml")));
        ((AnchorPane)loginFormRoot.getParent()).getChildren().remove(loginFormRoot);
    }
}