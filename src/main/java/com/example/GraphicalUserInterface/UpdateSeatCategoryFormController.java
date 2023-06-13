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

public class UpdateSeatCategoryFormController implements Initializable {
    private ManagementMain main;
    private String oldPrice;
    @FXML
    private TextField idField, categoryField, seatPricePriceField;
    @FXML
    private VBox updateSeatCategoryForm;

    public UpdateSeatCategoryFormController() {
        main = ManagementMain.getInstance();

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idFieldInit();
        priceFieldInit();
    }
    public void priceFieldInit() {
        CompletableFuture.delayedExecutor(300, TimeUnit.MILLISECONDS).execute(() -> {
            oldPrice = seatPricePriceField.getText();
        });
    }
    public void idFieldInit() {
        idField.setDisable(true);
    }
    public void disableUpdateForm() {
        ((AnchorPane)updateSeatCategoryForm.getParent()).getChildren().get(0).setVisible(true);
        ((AnchorPane)updateSeatCategoryForm.getParent()).getChildren().remove(2);
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
    public void handleUpdateRecordRequest() {
        HashMap<String, String> seatInfo = new HashMap<String, String>();
        seatInfo.put("CATEGORY", categoryField.getText());
        Response response = main.getProcessorManager().getSeatCategoryManagementProcessor().updateData(seatInfo, String.format("ID = '%s'", idField.getText()), true);
        StatusCode status = response.getStatusCode();
        if (seatPricePriceField.getText() != oldPrice) {
            HashMap<String, String> seatCategoryPriceInfo = new HashMap<String, String>();
            seatCategoryPriceInfo.put("SEAT_CATEGORY_ID", idField.getText());
            seatCategoryPriceInfo.put("ID", main.getIdGenerator().generateId(main.getProcessorManager().getSeatPriceManagementProcessor().getDefaultDatabaseTable()));
            seatCategoryPriceInfo.put("PRICE", seatPricePriceField.getText());
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formatDateTime = now.format(formatter);
            seatCategoryPriceInfo.put("DATE", formatDateTime);
            main.getProcessorManager().getSeatPriceManagementProcessor().insertData(seatCategoryPriceInfo, true);
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
