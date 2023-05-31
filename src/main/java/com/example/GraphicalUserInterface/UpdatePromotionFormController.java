package com.example.GraphicalUserInterface;

import Utils.Response;
import Utils.StatusCode;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.ResourceBundle;

public class UpdatePromotionFormController implements Initializable {
    private ManagementMain main;
    @FXML
    private TextField idField, nameField, descriptionField, discountField;
    @FXML
    private DatePicker startDateField, endDateField;
    @FXML
    private VBox updatePromotionForm;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idFieldInit();
    }
    public void idFieldInit() {
        idField.setDisable(true);
    }
    public UpdatePromotionFormController() {
        main = ManagementMain.getInstance();
    }
    public void disableUpdateForm() {
        ((AnchorPane)updatePromotionForm.getParent()).getChildren().get(0).setVisible(true);
        ((AnchorPane)updatePromotionForm.getParent()).getChildren().remove(2);
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
        HashMap<String, String> promotionInfo = new HashMap<String, String>();
        promotionInfo.put("NAME", nameField.getText());
        promotionInfo.put("START_DATE", DateTimeFormatter.ofPattern("yyyy-MM-dd").format(startDateField.getValue()));
        promotionInfo.put("END_DATE", DateTimeFormatter.ofPattern("yyyy-MM-dd").format(endDateField.getValue()));
        promotionInfo.put("DISCOUNT", discountField.getText());

        Response response = main.getPromotionManagementProcessor().updateData(promotionInfo, String.format("ID = '%s'", idField.getText()), true);
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
