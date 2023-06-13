package com.example.GraphicalUserInterface;

import Utils.Response;
import Utils.StatusCode;
import com.example.GraphicalUserInterface.ManagementMain;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class UpdateUserCategoryFormController implements Initializable {
    private ManagementMain main;
    @FXML
    private TextField idField, categoryField, pointLowerboundField;
    @FXML
    private VBox updateUserCategoryForm;

    public UpdateUserCategoryFormController() {
        main = ManagementMain.getInstance();

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idFieldInit();
    }
    public void idFieldInit() {
        idField.setDisable(true);
    }
    public void disableUpdateForm() {
        ((AnchorPane)updateUserCategoryForm.getParent()).getChildren().get(0).setVisible(true);
        ((AnchorPane)updateUserCategoryForm.getParent()).getChildren().remove(2);
    }
    @FXML
    public void saveUpdateBtnOnClick() throws IOException {
        System.out.println("save");
        handleUpdateRecordRequest();
        disableUpdateForm();
    }
    @FXML
    public void cancelUpdateBtnOnClick() {
//        DialogPane cancelConfirmation = new DialogPane();
        System.out.println("cancel");
        disableUpdateForm();
    }
    public void handleUpdateRecordRequest() {
        HashMap<String, String> userInfo = new HashMap<String, String>();
        userInfo.put("CATEGORY", categoryField.getText());
        userInfo.put("POINT_LOWERBOUND", pointLowerboundField.getText());
        Response response = main.getProcessorManager().getUserCategoryManagementProcessor().updateData(userInfo, String.format("ID = '%s'", idField.getText()), true);
        StatusCode status = response.getStatusCode();
        if (status == StatusCode.OK) {
            Dialog<String> dialog = new Dialog<String>();
            //Setting the title
            dialog.setTitle("Success");
            ButtonType type = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
            dialog.setContentText("The record has been updated successfully");
            dialog.getDialogPane().getButtonTypes().add(type);
            dialog.showAndWait();
        } else {
            Dialog<String> dialog = new Dialog<String>();
            //Setting the title
            dialog.setTitle("Failed");
            ButtonType type = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
            dialog.setContentText(response.getMessage());
            dialog.getDialogPane().getButtonTypes().add(type);
            dialog.showAndWait();
        }
    }
}
