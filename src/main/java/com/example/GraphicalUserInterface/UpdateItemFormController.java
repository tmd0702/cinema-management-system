package com.example.GraphicalUserInterface;

import Utils.Response;
import Utils.StatusCode;
import Utils.Utils;
import com.example.GraphicalUserInterface.ManagementMain;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class UpdateItemFormController implements Initializable {
    private ManagementMain main;
    @FXML
    private TextField idField, nameField, quantityField, unitField, revenueField;
    @FXML
    private ComboBox itemCategoryCategoryField;
    @FXML
    private VBox updateItemForm;
    ArrayList<ArrayList<String>> itemCategoryFetcher;
    ArrayList<String> itemCategoryNames;
    public UpdateItemFormController() {
        main = ManagementMain.getInstance();

    }
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
        categoryFieldInit();
        statusFieldInit();
    }
    public void idFieldInit() {
        idField.setDisable(true);
    }
    public void disableUpdateForm() {
        ((AnchorPane)updateItemForm.getParent()).getChildren().get(0).setVisible(true);
        ((AnchorPane)updateItemForm.getParent()).getChildren().remove(2);
    }
    public void categoryFieldInit() {
        itemCategoryFetcher = main.getProcessorManager().getItemCategoryManagementProcessor().getData(0, -1, "", "").getData();
        itemCategoryNames = Utils.getDataValuesByColumnName(itemCategoryFetcher, "ITEM_CATEGORY.CATEGORY");
        itemCategoryCategoryField.setItems(FXCollections.observableArrayList(itemCategoryNames));
    }
    public String getItemCategoryObjectIDFromComBoBox(Object value) {
        String id = null;
        for (int i=0; i<itemCategoryNames.size();++i) {
            if (itemCategoryNames.get(i).equals(value)) {
                id = Utils.getRowValueByColumnName(2 + i, "ITEM_CATEGORY.ID", itemCategoryFetcher);
                break;
            }
        }
        return id;
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
        HashMap<String, String> itemInfo = new HashMap<String, String>();
        itemInfo.put("NAME", nameField.getText());
        itemInfo.put("REVENUE", revenueField.getText());
        itemInfo.put("UNIT", unitField.getText());
        itemInfo.put("QUANTITY", quantityField.getText());
        itemInfo.put("ITEM_CATEGORY_ID", getItemCategoryObjectIDFromComBoBox(itemCategoryCategoryField.getValue()));
        itemInfo.put("STATUS", statusField.getValue().toString());
        Response response = main.getProcessorManager().getItemManagementProcessor().updateData(itemInfo, String.format("ID = '%s'", idField.getText()), true);
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
