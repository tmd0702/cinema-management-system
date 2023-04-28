package com.example.GraphicalUserInterface;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class AddMovieFormController {
    private ComboBox<String> comboBox_mstatus;
    private Label label_mstatus;
    ObservableList<String> list_mstatus = FXCollections.observableArrayList("Đã chiếu", "Đang chiếu", "Sắp chiếu");

    public void init_mstatus(URL Location, ResourceBundle resource){
        comboBox_mstatus.setItems(list_mstatus);
    }
    public void comboBoxChanged_mstatus(ActionEvent event){
        label_mstatus.setText(comboBox_mstatus.getValue());
    }

}
