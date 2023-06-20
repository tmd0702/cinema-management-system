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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class UpdateSeatCategoryPriceFormController implements Initializable {
    private ManagementMain main;
    private String oldPrice;
    private ArrayList<ArrayList<String>> seatCategoryInfo;
    private ArrayList<String> seatCategoryNames;
    @FXML
    private ComboBox seatCategoryCategoryField;
    @FXML
    private TextField idField, priceField;
    @FXML
    private VBox updateSeatCategoryPriceForm;

    public UpdateSeatCategoryPriceFormController() {
        main = ManagementMain.getInstance();

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        seatCategoryCategoryFieldInit();
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
        ((AnchorPane)updateSeatCategoryPriceForm.getParent()).getChildren().get(0).setVisible(true);
        ((AnchorPane)updateSeatCategoryPriceForm.getParent()).getChildren().remove(2);
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
    public void seatCategoryCategoryFieldInit() {
        seatCategoryInfo = main.getProcessorManager().getSeatCategoryManagementProcessor().getData(0, -1, "", "").getData();
        seatCategoryNames = Utils.getDataValuesByColumnName(seatCategoryInfo, "SEAT_CATEGORY.CATEGORY");
        seatCategoryCategoryField.setItems(FXCollections.observableArrayList(seatCategoryNames));
    }
    public String getSeatCategoryObjectIDFromComBoBox(Object value) {
        String id = null;
        for (int i=0; i<seatCategoryNames.size();++i) {
            if (seatCategoryNames.get(i).equals(value)) {
                id = Utils.getRowValueByColumnName(2 + i, "SEAT_CATEGORY.ID", seatCategoryInfo);
                break;
            }
        }
        return id;
    }
    public void handleUpdateRecordRequest() {
        if (!priceField.getText().equals(oldPrice)) {
            HashMap<String, String> seatCategoryPriceInfo = new HashMap<String, String>();
            seatCategoryPriceInfo.put("SEAT_CATEGORY_ID", getSeatCategoryObjectIDFromComBoBox(seatCategoryCategoryField.getValue()));
            seatCategoryPriceInfo.put("ID", main.getIdGenerator().generateId(main.getProcessorManager().getSeatPriceManagementProcessor().getDefaultDatabaseTable()));
            seatCategoryPriceInfo.put("PRICE", priceField.getText());
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formatDateTime = now.format(formatter);
            seatCategoryPriceInfo.put("DATE", formatDateTime);
            Response response = main.getProcessorManager().getSeatPriceManagementProcessor().insertData(seatCategoryPriceInfo, true);
            if (response.getStatusCode() == StatusCode.OK) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation");
                alert.setContentText("Update seat category price success!");
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
