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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class UpdateItemCategoryPriceFormController implements Initializable {
    private ManagementMain main;
    private String oldPrice;
    private ArrayList<ArrayList<String>> itemCategoryInfo;
    private ArrayList<String> itemCategoryNames;
    @FXML
    private ComboBox itemCategoryCategoryField;
    @FXML
    private TextField idField, priceField;
    @FXML
    private VBox updateItemCategoryPriceForm;

    public UpdateItemCategoryPriceFormController() {
        main = ManagementMain.getInstance();

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        itemCategoryCategoryFieldInit();
        idFieldInit();
        priceFieldInit();

    }
    public void priceFieldInit() {
        CompletableFuture.delayedExecutor(300, TimeUnit.MILLISECONDS).execute(() -> {
            oldPrice = priceField.getText();
        });
    }
    public void idFieldInit() {
        idField.setDisable(true);
    }
    public void disableUpdateForm() {
        ((AnchorPane)updateItemCategoryPriceForm.getParent()).getChildren().get(0).setVisible(true);
        ((AnchorPane)updateItemCategoryPriceForm.getParent()).getChildren().remove(2);
    }
    @FXML
    public void saveUpdateBtnOnClick() throws IOException {
        handleUpdateRecordRequest();
        disableUpdateForm();
    }
    @FXML
    public void cancelUpdateBtnOnClick() {
//        DialogPane cancelConfirmation = new DialogPane();
        disableUpdateForm();
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
    public void handleUpdateRecordRequest() {
        if (!priceField.getText().equals(oldPrice)) {
            HashMap<String, String> itemCategoryPriceInfo = new HashMap<String, String>();
            itemCategoryPriceInfo.put("ITEM_CATEGORY_ID", getItemCategoryObjectIDFromComBoBox(itemCategoryCategoryField.getValue()));
            itemCategoryPriceInfo.put("ID", main.getIdGenerator().generateId(main.getProcessorManager().getItemPriceManagementProcessor().getDefaultDatabaseTable()));
            itemCategoryPriceInfo.put("PRICE", priceField.getText());
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formatDateTime = now.format(formatter);
            itemCategoryPriceInfo.put("DATE", formatDateTime);
            Response response = main.getProcessorManager().getItemPriceManagementProcessor().insertData(itemCategoryPriceInfo, true);
            if (response.getStatusCode() == StatusCode.OK) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation");
                alert.setContentText("Update Item category price success!");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    System.out.println("ok");
                } else {
                    System.out.println("cancel");
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation");
                alert.setContentText(response.getMessage());
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    System.out.println("ok");
                } else {
                    System.out.println("cancel");
                }
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setContentText("Error: New price must be different from old price");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                System.out.println("ok");
            } else {
                System.out.println("cancel");
            }
        }
    }
}
