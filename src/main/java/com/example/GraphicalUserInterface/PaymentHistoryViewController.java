package com.example.GraphicalUserInterface;

import Utils.Utils;
import com.example.GraphicalUserInterface.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class PaymentHistoryViewController implements Initializable {
    private Main main;
    @FXML
    private GridPane paymentHistoryViewGrid;
    private ArrayList<ArrayList<String>> paymentHistoryFetcher;
    public PaymentHistoryViewController() {
        main = Main.getInstance();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        paymentHistoryViewGridInit();
    }
    public void paymentHistoryViewGridInit() {
        paymentHistoryViewGrid.setHgap(10);
        paymentHistoryViewGrid.setVgap(10);
        paymentHistoryFetcher = main.getProcessorManager().getPaymentManagementProcessor().getData(0, -1, String.format("USERS.ID = '%s'", main.getSignedInUser().getId()), "PAYMENTS.PAYMENT_DATE DESC").getData();
        for (ColumnConstraints column : paymentHistoryViewGrid.getColumnConstraints()) {
            column.setFillWidth(true);
        }
        for (int i=2; i<paymentHistoryFetcher.size();++i) {
            Label paymentDateLabel = new Label(Utils.getRowValueByColumnName(i, "PAYMENTS.PAYMENT_DATE", paymentHistoryFetcher));
            Label totalAmountLabel = new Label(Utils.getRowValueByColumnName(i, "PAYMENTS.TOTAL_AMOUNT", paymentHistoryFetcher));
            Label movieTitleLabel = new Label(Utils.getRowValueByColumnName(i, "MOVIES.TITLE", paymentHistoryFetcher));
            Label cinemaNameLabel = new Label(Utils.getRowValueByColumnName(i, "CINEMAS.NAME", paymentHistoryFetcher));
            Label screenRoomNameLabel = new Label(Utils.getRowValueByColumnName(i, "SCREEN_ROOMS.NAME", paymentHistoryFetcher));
            Label showDateLabel = new Label(Utils.getRowValueByColumnName(i, "SCHEDULES.SHOW_DATE", paymentHistoryFetcher));
            Label startTimeLabel = new Label(Utils.getRowValueByColumnName(i, "SHOW_TIMES.START_TIME", paymentHistoryFetcher));
            setStyleForGridViewLabel(paymentDateLabel);
            setStyleForGridViewLabel(totalAmountLabel);
            setStyleForGridViewLabel(movieTitleLabel);
            setStyleForGridViewLabel(cinemaNameLabel);
            setStyleForGridViewLabel(screenRoomNameLabel);
            setStyleForGridViewLabel(showDateLabel);
            setStyleForGridViewLabel(startTimeLabel);
            paymentHistoryViewGrid.addRow(i - 1, movieTitleLabel, cinemaNameLabel, paymentDateLabel, totalAmountLabel, screenRoomNameLabel, showDateLabel, startTimeLabel);
        }
    }
    public void setStyleForGridViewLabel(Label label) {
//        label.setStyle("");
        label.setWrapText(true);
    }
}
