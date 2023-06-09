package com.example.GraphicalUserInterface;
import UserManager.Customer;
import Utils.Response;
import Utils.Utils;
import com.example.GraphicalUserInterface.Main;
import com.example.GraphicalUserInterface.ManagementMain;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;
import Utils.StatusCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class LoginFormController implements Initializable {
    private Properties prop;
    private String filePath;
    @FXML
    private AnchorPane loginFormRoot;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private CheckBox rememberAccountCheckBox;
    private Main main;
    public LoginFormController() throws IOException {
        main = Main.getInstance();
        this.prop = new Properties();
        InputStream is = ManagementMain.class.getResourceAsStream("/cache/account-cache.properties");
        System.out.println((Main.class.getResource("/cache").getPath() + "account-cache.properties").substring(1));
        filePath = (Main.class.getResource("/cache").getPath() + "account-cache.properties").substring(1);
        //load a properties file from class path, inside static method
        this.prop.load(is);
    }

    @Override
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

    public void modifyHeaderUI() {
        if (main.getSignedInUser() != null) {
            main.getNodeById("#signInBtn").setVisible(false);
            main.getNodeById("#signUpBtn").setVisible(false);
            main.getNodeById("#userProfileBtn").setVisible(true);
            ((Label) main.getNodeById("#userFullNameDisplayField")).setText(main.getSignedInUser().getFirstName() + " " + main.getSignedInUser().getLastName());
        }
    }
    public void disableForm() {
        modifyHeaderUI();
        main.getNodeById("#mainOutlineContentView").setDisable(false);
        ((AnchorPane)loginFormRoot.getParent()).getChildren().remove(loginFormRoot);
    }
    public void onLoginSubmitBtnClick() throws Exception {
        HashMap<String, String> signinInfo = new HashMap<String, String>();
        signinInfo.put("username", this.usernameField.getText());
        signinInfo.put("password", this.passwordField.getText());
        Response response =  main.getProcessorManager().getAccountManagementProcessor().handleSigninAction(signinInfo);
        StatusCode signinStatus = response.getStatusCode();
        if (signinStatus == StatusCode.OK) {
            ArrayList<ArrayList<String>> userInfo = response.getData();
            if (rememberAccountCheckBox.isSelected()) {
                Utils.writeProperties(this.prop, this.usernameField.getText(), this.passwordField.getText(), filePath);
            } else {
                Utils.writeProperties(this.prop, "", "", filePath);
            }
            String userStatus = Utils.getRowValueByColumnName(2, "USERS.STATUS", userInfo);
            if (userStatus.equals("AVAILABLE")) {
                main.setSignedInUser(new Customer(Utils.getRowValueByColumnName(2, "USERS.USERNAME", userInfo), Utils.getRowValueByColumnName(2, "USERS.ID", userInfo), Utils.getRowValueByColumnName(2, "USERS.FIRST_NAME", userInfo), Utils.getRowValueByColumnName(2, "USERS.LAST_NAME", userInfo), new Date(new SimpleDateFormat("yyyy-MM-dd").parse(Utils.getRowValueByColumnName(2, "USERS.DOB", userInfo)).getTime()), Utils.getRowValueByColumnName(2, "USERS.PHONE", userInfo), Utils.getRowValueByColumnName(2, "USERS.EMAIL", userInfo), Utils.getRowValueByColumnName(2, "USERS.GENDER", userInfo), Utils.getRowValueByColumnName(2, "USERS.ADDRESS", userInfo), Integer.parseInt(Utils.getRowValueByColumnName(2, "USERS.SCORE", userInfo)), Utils.getRowValueByColumnName(2, "USERS.USER_CATEGORY_CATEGORY", userInfo)));
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", ButtonType.OK);
                alert.setTitle("Confirmation");
                alert.setContentText("Sign in success");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    System.out.println("ok");
                    disableForm();
                } else {
                    System.out.println("cancel");
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", ButtonType.OK);
                alert.setTitle("Confirmation");
                alert.setContentText("Error: Your account has been banned!");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    System.out.println("ok");
                    disableForm();
                } else {
                    System.out.println("cancel");
                }
            }

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Sign in");
            alert.setContentText("Error: Wrong username or password!");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                System.out.println("ok");
            }
        }
    }
    public void onSignUpBtnClick() throws Exception {
        ((AnchorPane)loginFormRoot.getParent()).getChildren().add(FXMLLoader.load(getClass().getResource("signup-form.fxml")));
        ((AnchorPane)loginFormRoot.getParent()).getChildren().remove(loginFormRoot);
    }
    public void onForgotPasswordBtnClick() throws Exception {
        ((AnchorPane)loginFormRoot.getParent()).getChildren().add(FXMLLoader.load(getClass().getResource("forgot-password-form.fxml")));
        ((AnchorPane)loginFormRoot.getParent()).getChildren().remove(loginFormRoot);
    }
    @FXML
    public void onXmarkBtnClick(MouseEvent event){
        main.getNodeById("#mainOutlineContentView").setDisable(false);
        ((AnchorPane)loginFormRoot.getParent()).getChildren().remove(loginFormRoot);
    }
}