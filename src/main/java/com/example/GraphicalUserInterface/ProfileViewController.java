package com.example.GraphicalUserInterface;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class ProfileViewController implements Initializable {
    @FXML
    private TextField firstNameField, lastNameField, usernameField, phoneField, emailField;
    @FXML
    private DatePicker dateOfBirthField;
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
//        profileViewInit();
    }
    public void profileViewInit() {
        firstNameField.setText(main.getSignedInUser().getFirstName());
        lastNameField.setText(main.getSignedInUser().getLastName());
        usernameField.setText(main.getSignedInUser().getUsername());
        phoneField.setText(main.getSignedInUser().getPhone());
        emailField.setText(main.getSignedInUser().getEmail());
        dateOfBirthField.setValue(LocalDate.parse(main.getSignedInUser().getDateOfBirth().toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }
}
