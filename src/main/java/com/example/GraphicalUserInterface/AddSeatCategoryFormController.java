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
import java.util.Optional;
import java.util.ResourceBundle;

public class AddSeatCategoryFormController implements Initializable {
    private ManagementMain main;
    @FXML
    private TextField categoryField, seatPricePriceField;
    @FXML
    private VBox addSeatCategoryForm;
    public AddSeatCategoryFormController() throws Exception {
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
        ((AnchorPane)addSeatCategoryForm.getParent()).getChildren().get(0).setVisible(true);
        ((AnchorPane)addSeatCategoryForm.getParent()).getChildren().remove(2);
    }
    @FXML
    public void saveInsertBtnOnClick() throws IOException {
        System.out.println("save");
        handleInsertRecordRequest();
        disableInsertForm();
    }
    public void handleInsertRecordRequest() {
        String SeatCategoryId = main.getIdGenerator().generateId(main.getProcessorManager().getSeatCategoryManagementProcessor().getDefaultDatabaseTable());
        HashMap<String, String> seatCategoryInfo = new HashMap<String, String>();
        seatCategoryInfo.put("CATEGORY", categoryField.getText());
        seatCategoryInfo.put("ID", SeatCategoryId);
        Response response = main.getProcessorManager().getSeatCategoryManagementProcessor().insertData(seatCategoryInfo, true);
        HashMap<String, String> seatPriceInfo = new HashMap<String, String>();
        seatPriceInfo.put("SEAT_CATEGORY_ID", SeatCategoryId);
        seatPriceInfo.put("ID", main.getIdGenerator().generateId(main.getProcessorManager().getSeatPriceManagementProcessor().getDefaultDatabaseTable()));
        seatPriceInfo.put("PRICE", seatPricePriceField.getText());
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formatDateTime = now.format(formatter);
        seatPriceInfo.put("DATE", formatDateTime);
        main.getProcessorManager().getSeatCategoryManagementProcessor().insertData(seatCategoryInfo, true);
        main.getProcessorManager().getSeatPriceManagementProcessor().insertData(seatPriceInfo, true);
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
