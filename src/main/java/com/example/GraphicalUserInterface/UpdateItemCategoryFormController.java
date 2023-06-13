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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class UpdateItemCategoryFormController implements Initializable {
    private ManagementMain main;
    private String oldPrice;
    @FXML
    private TextField idField, categoryField, itemPricePriceField;
    @FXML
    private VBox updateItemCategoryForm;

    public UpdateItemCategoryFormController() {
        main = ManagementMain.getInstance();

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idFieldInit();
        priceFieldInit();
    }
    public void priceFieldInit() {
        CompletableFuture.delayedExecutor(300, TimeUnit.MILLISECONDS).execute(() -> {
            oldPrice = itemPricePriceField.getText();
        });
    }
    public void idFieldInit() {
        idField.setDisable(true);
    }
    public void disableUpdateForm() {
        ((AnchorPane)updateItemCategoryForm.getParent()).getChildren().get(0).setVisible(true);
        ((AnchorPane)updateItemCategoryForm.getParent()).getChildren().remove(2);
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
        itemInfo.put("CATEGORY", categoryField.getText());
        Response response = main.getProcessorManager().getItemCategoryManagementProcessor().updateData(itemInfo, String.format("ID = '%s'", idField.getText()), true);
        StatusCode status = response.getStatusCode();
        if (itemPricePriceField.getText() != oldPrice) {
            HashMap<String, String> itemCategoryPriceInfo = new HashMap<String, String>();
            itemCategoryPriceInfo.put("ITEM_CATEGORY_ID", idField.getText());
            itemCategoryPriceInfo.put("ID", main.getIdGenerator().generateId(main.getProcessorManager().getItemPriceManagementProcessor().getDefaultDatabaseTable()));
            itemCategoryPriceInfo.put("PRICE", itemPricePriceField.getText());
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formatDateTime = now.format(formatter);
            itemCategoryPriceInfo.put("DATE", formatDateTime);
            main.getProcessorManager().getItemPriceManagementProcessor().insertData(itemCategoryPriceInfo, true);
        }
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
