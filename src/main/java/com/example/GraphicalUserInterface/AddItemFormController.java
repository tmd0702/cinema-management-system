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
import java.util.Optional;
import java.util.ResourceBundle;

public class AddItemFormController implements Initializable {
    private ManagementMain main;
    @FXML
    private ComboBox itemCategoryCategoryField;
    @FXML
    private TextField nameField, quantityField, unitField, revenueField;
    @FXML
    private ArrayList<ArrayList<String>> itemCategoryFetcher;
    @FXML
    private ArrayList<String> itemCategoryNames;
    @FXML
    private VBox addItemForm;
    public AddItemFormController() throws Exception {
        main = ManagementMain.getInstance();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        categoryFieldInit();
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
    public void cancelInsertBtnOnClick() {
        System.out.println("cancel");
        cancelInsertConfirmationAlert("Are you sure to ged rid of this record?");
    }
    public void cancelInsertConfirmationAlert(String contentText) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setContentText(contentText);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            System.out.println("ok");
            disableInsertForm();
        } else {
            System.out.println("cancel");
        }
    }
    public void disableInsertForm() {
        ((AnchorPane)addItemForm.getParent()).getChildren().get(0).setVisible(true);
        ((AnchorPane)addItemForm.getParent()).getChildren().remove(2);
    }
    @FXML
    public void saveInsertBtnOnClick() throws IOException {
        System.out.println("save");
        handleInsertRecordRequest();
        disableInsertForm();
    }
    public void handleInsertRecordRequest() {
        HashMap<String, String> itemInfo = new HashMap<String, String>();
        itemInfo.put("NAME", nameField.getText());
        itemInfo.put("QUANTITY", quantityField.getText());
        itemInfo.put("REVENUE", revenueField.getText());
        itemInfo.put("UNIT", unitField.getText());
        itemInfo.put("ITEM_CATEGORY_ID", getItemCategoryObjectIDFromComBoBox(itemCategoryCategoryField.getValue()));
        itemInfo.put("ID", main.getIdGenerator().generateId(main.getProcessorManager().getItemManagementProcessor().getDefaultDatabaseTable()));
        Response response = main.getProcessorManager().getItemManagementProcessor().insertData(itemInfo, true);
        StatusCode signupStatus = response.getStatusCode();
        if (signupStatus == StatusCode.OK) {
            Dialog<String> dialog = new Dialog<String>();
            //Setting the title
            dialog.setTitle("Success");
            ButtonType type = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
            dialog.setContentText("The record has been added successfully");
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
