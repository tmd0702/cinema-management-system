package com.example.GraphicalUserInterface;

import Utils.Utils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;

public class AnalyticsDashboardController implements Initializable {
    @FXML
    private Label todayRevenueField, thisWeekRevenueField, thisMonthRevenueField, thisYearRevenueField, customerCountField;
    private ManagementMain main;
    public AnalyticsDashboardController() {
        main = ManagementMain.getInstance();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        String todayDate = dtf.format(now);
        String []todayDateSplitted = todayDate.split("-");
        String year = todayDateSplitted[0];
        String month = todayDateSplitted[1];
        String day = todayDateSplitted[2];
        String thisYearStartDate = String.format("01-01-%s", year);
        String thisMonthStartDate = String.format("01-01-%s", month);
        todayRevenueField.setText(Utils.getRowValueByColumnName(2, "REVENUE", main.getProcessorManager().getPaymentManagementProcessor().select("SUM(PAYMENTS.TOTAL_AMOUNT) AS REVENUE", 0, 1, "", String.format("PAYMENT_DATE = '%s'", todayDate), "PAYMENTS").getData()));
        thisMonthRevenueField.setText(Utils.getRowValueByColumnName(2, "REVENUE", main.getProcessorManager().getPaymentManagementProcessor().select("SUM(PAYMENTS.TOTAL_AMOUNT) AS REVENUE", 0, 1, "", String.format("PAYMENT_DATE >= '%s' AND PAYMENT_DATE <= '%s'", thisMonthStartDate, todayDate), "PAYMENTS").getData()));
        thisYearRevenueField.setText(Utils.getRowValueByColumnName(2, "REVENUE", main.getProcessorManager().getPaymentManagementProcessor().select("SUM(PAYMENTS.TOTAL_AMOUNT) AS REVENUE", 0, 1, "", String.format("PAYMENT_DATE >= '%s' AND PAYMENT_DATE <= '%s'", thisYearStartDate, todayDate), "PAYMENTS").getData()));
        customerCountField.setText(Integer.toString(main.getProcessorManager().getAccountManagementProcessor().countData("USERS.USER_ROLE = 'admin'")));
    }
}
