package com.example.GraphicalUserInterface;

import Database.SigninProcessor;
import UserManager.Subscriber;
import Utils.StatusCode;
import Utils.Utils;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Properties;
import java.util.ResourceBundle;

public class AdminLoginFormController implements Initializable {
    private Properties prop;

    private ManagementMain managementMain;
    private String filePath;
    private SigninProcessor signinProcessor;
    @FXML
    private CheckBox rememberAccountCheckBox;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    public AdminLoginFormController() throws Exception {
        this.signinProcessor = new SigninProcessor();
        this.managementMain = ManagementMain.getInstance();
        this.prop = new Properties();
        InputStream is = ManagementMain.class.getResourceAsStream("/cache/account-cache.properties");
        System.out.println((Main.class.getResource("/cache").getPath() + "account-cache.properties").substring(1));
        filePath = (Main.class.getResource("/cache").getPath() + "account-cache.properties").substring(1);
//        InputStream is = new FileInputStream(fileName);
        //load a properties file from class path, inside static method
        this.prop.load(is);
    }
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Read properties
        rememberAccountCheckBox.setSelected(true);
        System.out.println(this.prop.getProperty("USERNAME") + " " +  this.prop.getProperty("PASSWORD"));
        //get the property value and print it out
        if (this.prop.getProperty("USERNAME") != "" && this.prop.getProperty("PASSWORD") != "") {
            this.usernameField.setText(this.prop.getProperty("USERNAME"));
            this.passwordField.setText(this.prop.getProperty("PASSWORD"));
        }

    }
    public void onLoginSubmitBtnClick() throws Exception {
        HashMap<String, String> signinInfo = new HashMap<String, String>();
        signinInfo.put("username", this.usernameField.getText());
        signinInfo.put("password", this.passwordField.getText());
        StatusCode signinStatus =  signinProcessor.handleSigninAction(signinInfo);
        System.out.println(signinStatus);
        if (signinStatus == StatusCode.OK) {
            System.out.println("Sign in success");

            // write username & password vo trong file account-cache.properties
            if (rememberAccountCheckBox.isSelected()) {
                Utils.writeProperties(this.prop, this.usernameField.getText(), this.passwordField.getText(), filePath);
            } else {
                Utils.writeProperties(this.prop, "", "", filePath);
            }
            this.managementMain.changeScene("management-view.fxml");
        } else {
            System.out.println("Sign in failed");
        }
    }
}
