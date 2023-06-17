package com.example.GraphicalUserInterface;

import UserManager.Customer;
import Utils.Response;
import Utils.StatusCode;
import Utils.Utils;
import com.example.GraphicalUserInterface.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;

public class ProfileViewController implements Initializable {
    @FXML
    private AnchorPane profileViewContainer;
    @FXML
    private TextField firstNameField, lastNameField, addressField, phoneField, emailField;
    @FXML
    private DatePicker dateOfBirthField;
    @FXML
    private ToggleGroup gender;
    @FXML
    public AnchorPane containerAnchor;
    @FXML
    private HBox genderContainerField;
    @FXML
    private RadioButton maleRadioBtn, femaleRadioBtn;
    private Main main;
    public ProfileViewController() {
        main = Main.getInstance();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        profileViewInit();
    }
    public void profileViewInit() {
        firstNameField.setText(main.getSignedInUser().getFirstName());
        lastNameField.setText(main.getSignedInUser().getLastName());
        addressField.setText(main.getSignedInUser().getAddress());
        phoneField.setText(main.getSignedInUser().getPhone());
        emailField.setText(main.getSignedInUser().getEmail());
        dateOfBirthField.setValue(LocalDate.parse(main.getSignedInUser().getDateOfBirth().toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        if (main.getSignedInUser().getGender().equals("M")) {
            maleRadioBtn.setSelected(true);
        } else {
            femaleRadioBtn.setSelected(true);
        }
    }
    @FXML
    public void changePasswordBtnOnClick() throws IOException {
        profileViewContainer.getChildren().add(FXMLLoader.load(getClass().getResource("change-password-form.fxml")));
        containerAnchor.setVisible(true);
    }
    @FXML
    public void saveProfileBtnOnClick() throws ParseException {
        HashMap<String, String> userInfo = new HashMap<String, String>();
        userInfo.put("FIRST_NAME", firstNameField.getText());
        userInfo.put("LAST_NAME", lastNameField.getText());
        userInfo.put("EMAIL", emailField.getText());
        userInfo.put("PHONE", phoneField.getText());
        userInfo.put("DOB", DateTimeFormatter.ofPattern("yyyy-MM-dd").format(dateOfBirthField.getValue()));
        userInfo.put("ADDRESS", addressField.getText());
        userInfo.put("GENDER", ((RadioButton)gender.getSelectedToggle()).getText().substring(0, 1));
        Response response = main.getProcessorManager().getAccountManagementProcessor().updateData(userInfo, String.format("ID = '%s'", main.getSignedInUser().getId()), true);
        if (response.getStatusCode() == StatusCode.OK) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setContentText("User information updated!");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                System.out.println("ok");
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
         response = main.getProcessorManager().getAccountManagementProcessor().getData(0, -1, String.format("USERS.ID = '%s'", main.getSignedInUser().getId()), "");
        ArrayList<ArrayList<String>> userInfor = response.getData();
        main.setSignedInUser(new Customer(Utils.getRowValueByColumnName(2, "USERS.USERNAME", userInfor), Utils.getRowValueByColumnName(2, "USERS.ID", userInfor), Utils.getRowValueByColumnName(2, "USERS.FIRST_NAME", userInfor), Utils.getRowValueByColumnName(2, "USERS.LAST_NAME", userInfor), new Date(new SimpleDateFormat("yyyy-MM-dd").parse(Utils.getRowValueByColumnName(2, "USERS.DOB", userInfor)).getTime()), Utils.getRowValueByColumnName(2, "USERS.PHONE", userInfor), Utils.getRowValueByColumnName(2, "USERS.EMAIL", userInfor), Utils.getRowValueByColumnName(2, "USERS.GENDER", userInfor), Utils.getRowValueByColumnName(2, "USERS.ADDRESS", userInfor), Integer.parseInt(Utils.getRowValueByColumnName(2, "USERS.SCORE", userInfor)), Utils.getRowValueByColumnName(2, "USERS.USER_CATEGORY_CATEGORY", userInfor)));

    }
}
