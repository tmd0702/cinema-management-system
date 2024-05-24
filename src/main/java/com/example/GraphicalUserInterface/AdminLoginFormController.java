package com.example.GraphicalUserInterface;
import UserManager.*;
import Utils.StatusCode;
import Utils.Utils;
import Utils.Response;
import com.example.GraphicalUserInterface.Main;
import com.example.GraphicalUserInterface.ManagementMain;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.io.InputStream;
import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;

public class AdminLoginFormController implements Initializable {
    private Properties prop;
    @FXML
    private AnchorPane adminLoginFormContainer;
    private ManagementMain managementMain;
    private String filePath;
    @FXML
    private CheckBox rememberAccountCheckBox;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    public AdminLoginFormController() throws Exception {
        this.managementMain = ManagementMain.getInstance();
        this.prop = new Properties();
        InputStream is = ManagementMain.class.getResourceAsStream("/cache/account-cache.properties");
        System.out.println((Main.class.getResource("/cache").getPath() + "account-cache.properties").substring(1));
        filePath = (Main.class.getResource("/cache").getPath() + "account-cache.properties").substring(1);
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
    @FXML
    public void forgotPasswordBtnOnClick() throws Exception {
        adminLoginFormContainer.getChildren().add(FXMLLoader.load(getClass().getResource("admin-forgot-password-form.fxml")));
    }
    public void onLoginSubmitBtnClick() throws Exception {
        HashMap<String, String> signinInfo = new HashMap<String, String>();
        signinInfo.put("username", this.usernameField.getText());
        signinInfo.put("password", this.passwordField.getText());
        Response response = managementMain.getProcessorManager().getAccountManagementProcessor().handleSigninAction(signinInfo);
        StatusCode signinStatus = response.getStatusCode();
        if (signinStatus == StatusCode.OK) {
            System.out.println("Sign in success");
            ArrayList<ArrayList<String>> userInfo = response.getData();
            this.managementMain.setSignedInUser(new Manager(Utils.getRowValueByColumnName(2, "USERS.USERNAME", userInfo), Utils.getRowValueByColumnName(2, "USERS.ID", userInfo), Utils.getRowValueByColumnName(2, "USERS.FIRST_NAME", userInfo), Utils.getRowValueByColumnName(2, "USERS.LAST_NAME", userInfo), new Date(new SimpleDateFormat("yyyy-MM-dd").parse(Utils.getRowValueByColumnName(2, "USERS.DOB", userInfo)).getTime()), Utils.getRowValueByColumnName(2, "USERS.PHONE", userInfo), Utils.getRowValueByColumnName(2, "USERS.EMAIL", userInfo), Utils.getRowValueByColumnName(2, "USERS.GENDER", userInfo), Utils.getRowValueByColumnName(2, "USERS.ADDRESS", userInfo), Integer.parseInt(Utils.getRowValueByColumnName(2, "USERS.SCORE", userInfo)), Utils.getRowValueByColumnName(2, "USERS.USER_CATEGORY_CATEGORY", userInfo)));
            // write username & password vo trong file account-cache.properties
            if (rememberAccountCheckBox.isSelected()) {
                Utils.writeProperties(this.prop, this.usernameField.getText(), this.passwordField.getText(), filePath);
            } else {
                Utils.writeProperties(this.prop, "", "", filePath);
            }
            this.managementMain.changeScene("management-view.fxml");
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Failed");
            alert.setContentText(response.getMessage());
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {

            } else {

            }
        }
    }
}
