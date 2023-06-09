package com.example.GraphicalUserInterface;

import Utils.Response;
import Utils.StatusCode;
import Utils.Utils;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class UpdatePromotionFormController implements Initializable {
    private ManagementMain main;
    ArrayList<ArrayList<String>> userCategoryInfo;
    ArrayList<String> userCategoryCategories;
    @FXML
    private TextField idField, nameField, descriptionField, discountField;
    @FXML
    private DatePicker startDateField, endDateField;
    @FXML
    private VBox updatePromotionForm;
    @FXML
    private ComboBox userCategoryCategoryField;
    @FXML
    private ComboBox statusField;
    public void statusFieldInit() {
        ArrayList<String> statusList = new ArrayList<String>();
        statusList.add("AVAILABLE");
        statusList.add("SUSPEND");
        statusList.add("CLOSED");
        statusField.setItems(FXCollections.observableArrayList(statusList));
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idFieldInit();
        userCategoryNameFieldInit();
        statusFieldInit();
    }
    public void userCategoryNameFieldInit() {
        userCategoryInfo = main.getProcessorManager().getUserCategoryManagementProcessor().getData(0, -1, "", "").getData();
        userCategoryCategories = Utils.getDataValuesByColumnName(userCategoryInfo, "USER_CATEGORY.CATEGORY");
        userCategoryCategoryField.setItems(FXCollections.observableArrayList(userCategoryCategories));
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
    public String getUserCategoryObjectIDFromComboBox(Object value) {
        String id = null;
        for (int i=0; i<userCategoryCategories.size();++i) {
            if (userCategoryCategories.get(i).equals(value)) {
                id = Utils.getRowValueByColumnName(i + 2, "ID", userCategoryInfo);
                break;
            }
        }
        return id;
    }
    public void handleUpdateRecordRequest() {
        HashMap<String, String> promotionInfo = new HashMap<String, String>();
        promotionInfo.put("NAME", nameField.getText());
        promotionInfo.put("START_DATE", DateTimeFormatter.ofPattern("yyyy-MM-dd").format(startDateField.getValue()));
        promotionInfo.put("END_DATE", DateTimeFormatter.ofPattern("yyyy-MM-dd").format(endDateField.getValue()));
        promotionInfo.put("DISCOUNT", discountField.getText());
        promotionInfo.put("DESCRIPTION", descriptionField.getText());
        promotionInfo.put("USER_CATEGORY_ID", getUserCategoryObjectIDFromComboBox(userCategoryCategoryField.getValue()));
        promotionInfo.put("STATUS", statusField.getValue().toString());
        Response response = main.getProcessorManager().getPromotionManagementProcessor().updateData(promotionInfo, String.format("ID = '%s'", idField.getText()), true);
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
