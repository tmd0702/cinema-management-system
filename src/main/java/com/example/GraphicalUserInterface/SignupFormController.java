package com.example.GraphicalUserInterface;
import Utils.Response;
import Utils.StatusCode;
import Utils.Utils;
import com.example.GraphicalUserInterface.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
public class SignupFormController implements Initializable {
    private Main main;
    @FXML
    ToggleGroup gender;
    @FXML
    private AnchorPane signupFormRoot;
    @FXML
    private TextField firstNameField, lastNameField, emailField, usernameField, passwordField, phoneField, addressField;
    @FXML
    private DatePicker dateOfBirthField;
    public SignupFormController() throws Exception {
        main = Main.getInstance();
//        dateOfBirthField = new DatePicker();
//        dateOfBirthField.setDayCellFactory(Utils.getDatePicker(true));
        Utils.setDatePickerConstraint(dateOfBirthField, true);
    }


    public void onSubmitSignUpBtn() {
        HashMap<String, String> signUpInfo = new HashMap<String, String>();
        signUpInfo.put("firstName", firstNameField.getText());
        signUpInfo.put("lastName", lastNameField.getText());
        signUpInfo.put("email", emailField.getText());
        signUpInfo.put("phone", phoneField.getText());
        signUpInfo.put("address", addressField.getText());
        signUpInfo.put("username", usernameField.getText());
        signUpInfo.put("password", passwordField.getText());
        Set key = signUpInfo.keySet();
        for(Object s : key){
            if(signUpInfo.get(s) == null ||dateOfBirthField.getValue() == null || gender.getSelectedToggle() == null) {
                Dialog<String> dialog = new Dialog<String>();
                //Setting the title
                dialog.setTitle("Dialog");
                ButtonType type = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
                dialog.setContentText("Have not been enough information for signing up");
                dialog.getDialogPane().getButtonTypes().add(type);
                dialog.showAndWait();
                return;
            }

        }
        signUpInfo.put("gender", ((RadioButton)gender.getSelectedToggle()).getText().substring(0, 1));
        signUpInfo.put("dateOfBirth", DateTimeFormatter.ofPattern("yyyy-MM-dd").format(dateOfBirthField.getValue()));
        signUpInfo.put("regisDate", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        Response response = main.getProcessorManager().getAccountManagementProcessor().handleSignupAction(signUpInfo);
        StatusCode signupStatus = response.getStatusCode();
        if (signupStatus == StatusCode.OK) {
            Dialog<String> dialog = new Dialog<String>();
            //Setting the title
            dialog.setTitle("ERROR");
            ButtonType type = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
            dialog.setContentText("Sign up success");
            dialog.getDialogPane().getButtonTypes().add(type);
            dialog.showAndWait();
            disableForm();
        } else {
            Dialog<String> dialog = new Dialog<String>();
            //Setting the title
            dialog.setTitle("Dialog");
            ButtonType type = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
            Label errorMessage = new Label(response.getMessage());
            errorMessage.setWrapText(true);
            dialog.getDialogPane().setContent(errorMessage);
//            dialog.setContentText();
            dialog.getDialogPane().getButtonTypes().add(type);
            dialog.showAndWait();
        }
    }
    public void disableForm() {
        main.getNodeById("#mainOutlineContentView").setDisable(false);
        ((AnchorPane)signupFormRoot.getParent()).getChildren().remove(signupFormRoot);
    }
    public void onSignInBtnClick() throws Exception {
        ((AnchorPane)signupFormRoot.getParent()).getChildren().add(FXMLLoader.load(getClass().getResource("login-form.fxml")));
        ((AnchorPane)signupFormRoot.getParent()).getChildren().remove(signupFormRoot);
    }
    @FXML
    public void onXmarkBtnClick(MouseEvent event){
        main.getNodeById("#mainOutlineContentView").setDisable(false);
        ((AnchorPane)signupFormRoot.getParent()).getChildren().remove(signupFormRoot);

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Utils.setDatePickerConstraint(dateOfBirthField, true);
    }
}
