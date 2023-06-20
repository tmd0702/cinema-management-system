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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;

public class AddItemCategoryPriceFormController implements Initializable {
    private ManagementMain main;
    @FXML
    private TextField priceField;
    @FXML
    private ComboBox itemCategoryCategoryField;
    private ArrayList<ArrayList<String>> itemCategoryInfo;
    private ArrayList<String> itemCategoryNames;
    @FXML
    private VBox addItemCategoryPriceForm;
    public AddItemCategoryPriceFormController() throws Exception {
        main = ManagementMain.getInstance();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        itemCategoryCategoryFieldInit();
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
        ((AnchorPane)addItemCategoryPriceForm.getParent()).getChildren().get(0).setVisible(true);
        ((AnchorPane)addItemCategoryPriceForm.getParent()).getChildren().remove(2);
    }
    @FXML
    public void saveInsertBtnOnClick() throws IOException {
        System.out.println("save");
        handleInsertRecordRequest();
        disableInsertForm();
    }
    public void itemCategoryCategoryFieldInit() {
        itemCategoryInfo = main.getProcessorManager().getItemCategoryManagementProcessor().getData(0, -1, "", "").getData();
        itemCategoryNames = Utils.getDataValuesByColumnName(itemCategoryInfo, "ITEM_CATEGORY.CATEGORY");
        itemCategoryCategoryField.setItems(FXCollections.observableArrayList(itemCategoryNames));
    }
    public String getItemCategoryObjectIDFromComBoBox(Object value) {
        String id = null;
        for (int i = 0; i< itemCategoryNames.size(); ++i) {
            if (itemCategoryNames.get(i).equals(value)) {
                id = Utils.getRowValueByColumnName(2 + i, "ITEM_CATEGORY.ID", itemCategoryInfo);
                break;
            }
        }
        return id;
    }
    public void handleInsertRecordRequest() {
        HashMap<String, String> itemPriceInfo = new HashMap<String, String>();
        itemPriceInfo.put("ITEM_CATEGORY_ID", getItemCategoryObjectIDFromComBoBox(itemCategoryCategoryField.getValue()));
        itemPriceInfo.put("ID", main.getIdGenerator().generateId(main.getProcessorManager().getItemPriceManagementProcessor().getDefaultDatabaseTable()));
        itemPriceInfo.put("PRICE", priceField.getText());
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formatDateTime = now.format(formatter);
        itemPriceInfo.put("DATE", formatDateTime);
        Response response = main.getProcessorManager().getItemPriceManagementProcessor().insertData(itemPriceInfo, true);
        StatusCode status = response.getStatusCode();
        if (status == StatusCode.OK) {
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
