package com.example.GraphicalUserInterface;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
public class AddAccountController {
    @FXML
    private ComboBox<String> comboBox_gender;
    @FXML
    private Label label_gender;
    @FXML
    private ComboBox<String> comboBox_role;
    @FXML
    private Label label_role;
    ObservableList<String> list_gender = FXCollections.observableArrayList("Female", "Male");
    ObservableList<String> list_role = FXCollections.observableArrayList("admin", "client");


    public void init_gender(URL location, ResourceBundle resource){
        comboBox_gender.setItems(list_gender);
    }
    public void comboBoxChanged_gender(ActionEvent event){
        label_gender.setText(comboBox_gender.getValue());
    }
    public void init_role(URL location, ResourceBundle resource){
        comboBox_role.setItems(list_role);
    }
    public void comboBoxChanged_role(ActionEvent event){
        label_role.setText(comboBox_role.getValue());
    }
}
