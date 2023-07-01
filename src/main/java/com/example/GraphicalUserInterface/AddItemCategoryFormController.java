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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;

public class AddItemCategoryFormController implements Initializable {
    private ManagementMain main;
    @FXML
    private TextField categoryField, itemPricePriceField;
    @FXML
    private VBox addItemCategoryForm;
    public AddItemCategoryFormController() throws Exception {
        main = ManagementMain.getInstance();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
       
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
        ((AnchorPane)addItemCategoryForm.getParent()).getChildren().get(0).setVisible(true);
        ((AnchorPane)addItemCategoryForm.getParent()).getChildren().remove(2);
    }
    @FXML
    public void saveInsertBtnOnClick() throws IOException {
        System.out.println("save");
        handleInsertRecordRequest();
        disableInsertForm();
    }
    public void handleInsertRecordRequest() {
        String itemCategoryId = main.getIdGenerator().generateId(main.getProcessorManager().getItemCategoryManagementProcessor().getDefaultDatabaseTable());
        HashMap<String, String> itemCategoryInfo = new HashMap<String, String>();
        itemCategoryInfo.put("CATEGORY", categoryField.getText());
        itemCategoryInfo.put("ID", itemCategoryId);
        Response response = main.getProcessorManager().getItemCategoryManagementProcessor().insertData(itemCategoryInfo, true);
        HashMap<String, String> itemCategoryPriceInfo = new HashMap<String, String>();
        itemCategoryPriceInfo.put("ITEM_CATEGORY_ID", itemCategoryId);
        itemCategoryPriceInfo.put("ID", main.getIdGenerator().generateId(main.getProcessorManager().getItemPriceManagementProcessor().getDefaultDatabaseTable()));
        itemCategoryPriceInfo.put("PRICE", itemPricePriceField.getText());
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formatDateTime = now.format(formatter);
        itemCategoryPriceInfo.put("DATE", formatDateTime);
        main.getProcessorManager().getItemCategoryManagementProcessor().insertData(itemCategoryInfo, true);
        main.getProcessorManager().getItemPriceManagementProcessor().insertData(itemCategoryPriceInfo, true);
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
