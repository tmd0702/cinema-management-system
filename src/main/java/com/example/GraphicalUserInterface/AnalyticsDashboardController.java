package com.example.GraphicalUserInterface;

import MovieManager.Movie;
import Utils.Utils;
import com.example.GraphicalUserInterface.ManagementMain;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.Font;

import java.net.URL;
import java.text.DateFormatSymbols;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AnalyticsDashboardController implements Initializable {
    @FXML
    private Label todayRevenueField, thisWeekRevenueField, thisMonthRevenueField, thisYearRevenueField, customerCountField;
    @FXML
    private ScrollPane analyticsDashboardScrollPane;
    @FXML
    private LineChart overallRevenueLineChart;
    @FXML
    private ComboBox movieNamesComboBox, itemNamesComboBox;
    @FXML
    private DatePicker bookingTicketStartDateField, bookingTicketEndDateField, bookingItemStartDateField, bookingItemEndDateField;
    @FXML
    private PieChart ticketItemComparisonPieChart, itemCategoryRevenueComparisonPieChart, movieGenreRevenuePieChart;
    @FXML
    private Label ticketQuantityField, ticketRevenueField, itemRevenueField, itemQuantityField;
    private ManagementMain main;
    public AnalyticsDashboardController() {
        main = ManagementMain.getInstance();
    }
    @FXML
    private BarChart top10MoviesRevenueBarChart, itemRevenueComparisonBarChart;
    public String getTodayDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        String todayDate = dtf.format(now);

        return todayDate;
    }
    public void searchFieldInit() {
        ArrayList<ArrayList<String>> movieInfoFetcher = main.getProcessorManager().getMovieManagementProcessor().getData(0, -1, "", "").getData();
        ArrayList<String> movieNames = Utils.getDataValuesByColumnName(movieInfoFetcher, "TITLE");
        for (Movie movie : main.getProcessorManager().getMovieManagementProcessor().getMovieManager().getMovieList()) {
            movieNames.add(movie.getTitle());
        }
        movieNamesComboBox.setItems(FXCollections.observableArrayList(movieNames));
        ArrayList<String> itemNames = Utils.getDataValuesByColumnName(main.getProcessorManager().getItemManagementProcessor().getData(0, -1, "", "").getData(), "ITEMS.NAME");
        itemNamesComboBox.setItems(FXCollections.observableArrayList(itemNames));
    }
    @FXML
    public void searchTicketAnalysisBtnOnClick() {
        ArrayList<ArrayList<String>> ticketCountFetcher = main.getProcessorManager().getAnalyticsProcessor().getTicketCount(String.format("MOVIES.TITLE = '%s'", movieNamesComboBox.getValue().toString())).getData();
        ArrayList<ArrayList<String>> ticketRevenueFetcher = main.getProcessorManager().getAnalyticsProcessor().getTicketRevenue(String.format("MOVIES.TITLE = '%s'", movieNamesComboBox.getValue().toString())).getData();
        Double ticketRevenue = Double.parseDouble(Utils.getRowValueByColumnName(2, "TICKET_REVENUE", ticketRevenueFetcher));
        int ticketQuantity = Integer.parseInt(Utils.getRowValueByColumnName(2, "TICKET_QUANTITY", ticketCountFetcher));
        ticketQuantityField.setText(String.format("Ticket quantity: %s", ticketQuantity));
        ticketRevenueField.setText(String.format("Ticket revenue: %s", ticketRevenue));

    }
    @FXML
    public void searchItemAnalysisBtnOnClick() {
        ArrayList<ArrayList<String>> itemCountFetcher = main.getProcessorManager().getAnalyticsProcessor().getItemCount(String.format("ITEMS.NAME = '%s'", itemNamesComboBox.getValue().toString())).getData();
        ArrayList<ArrayList<String>> itemRevenueFetcher = main.getProcessorManager().getAnalyticsProcessor().getItemRevenue(String.format("ITEMS.NAME = '%s'", itemNamesComboBox.getValue().toString())).getData();
        Double itemRevenue = Double.parseDouble(Utils.getRowValueByColumnName(2, "ITEM_REVENUE", itemRevenueFetcher));
        int itemQuantity = Integer.parseInt(Utils.getRowValueByColumnName(2, "ITEM_QUANTITY", itemCountFetcher));
        itemQuantityField.setText(String.format("Item quantity: %s", itemQuantity));
        itemRevenueField.setText(String.format("Item revenue: %s", itemRevenue));
    }
    public void headerSectionInit() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        String todayDate = dtf.format(now);
        String[] todayDateSplitted = this.getTodayDate().split("-");
        String year = todayDateSplitted[0];
        String month = todayDateSplitted[1];
        String day = todayDateSplitted[2];
        String thisYearStartDate = String.format("%s-01-01", year);
        String thisMonthStartDate = String.format("%s-%s-01", year, month);

        todayRevenueField.setText(Utils.getRowValueByColumnName(2, "OVERALL_REVENUE", main.getProcessorManager().getAnalyticsProcessor().getOverallRevenue(String.format("PAYMENT_DATE = '%s'", todayDate)).getData()));
        todayRevenueField.setFont(Font.font("Georgia"));
        thisMonthRevenueField.setText(Utils.getRowValueByColumnName(2, "OVERALL_REVENUE", main.getProcessorManager().getAnalyticsProcessor().getOverallRevenue(String.format("PAYMENT_DATE >= '%s' AND PAYMENT_DATE <= '%s'", thisMonthStartDate, getTodayDate())).getData()));
        thisMonthRevenueField.setFont(Font.font("Georgia"));
        thisYearRevenueField.setText(Utils.getRowValueByColumnName(2, "OVERALL_REVENUE", main.getProcessorManager().getAnalyticsProcessor().getOverallRevenue(String.format("PAYMENT_DATE >= '%s' AND PAYMENT_DATE <= '%s'", thisYearStartDate, getTodayDate())).getData()));
        thisYearRevenueField.setFont(Font.font("Georgia"));
        customerCountField.setText(Integer.toString(main.getProcessorManager().getAccountManagementProcessor().countData("USERS.USER_ROLE = 'admin'")));
        customerCountField.setFont(Font.font("Georgia"));
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        headerSectionInit();
        ticketItemComparisonPieChartInit();
        top10MoviesRevenueBarChartInit();
        itemCategoryRevenueComparisonPieChartInit();
        overallRevenueLineChartInit();
        itemRevenueComparisonBarChartInit();
        movieGenreRevenuePieChartInit();
        searchFieldInit();
    }
    public void ticketItemComparisonPieChartInit() {
        double ticketRevenue = Double.valueOf(Utils.getRowValueByColumnName(2, "TICKET_REVENUE", main.getProcessorManager().getAnalyticsProcessor().getTicketRevenue("").getData()));
        double itemRevenue = Double.valueOf(Utils.getRowValueByColumnName(2, "ITEM_REVENUE", main.getProcessorManager().getAnalyticsProcessor().getItemRevenue("").getData()));
        double totalRevenue = ticketRevenue + itemRevenue;
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data("Tickets", ticketRevenue/totalRevenue * 100),
                        new PieChart.Data("Items", itemRevenue/totalRevenue * 100));
        ticketItemComparisonPieChart.setData(pieChartData);
        ticketItemComparisonPieChart.setLegendVisible(true);
        ticketItemComparisonPieChart.setStyle("-fx-font-size: 10px; -fx-font-weight: bold;-fx-font-family: Georgia");
        ticketItemComparisonPieChart.setTitle("Comparison between Tickets/Items Revenue");
    }
    public void itemRevenueComparisonBarChartInit() {
        ArrayList<ArrayList<String>> itemInfoFetcher = main.getProcessorManager().getItemManagementProcessor().getData(0, -1, "", "").getData();
        System.out.println(itemInfoFetcher);
        ArrayList<String> itemNames = Utils.getDataValuesByColumnName(itemInfoFetcher, "ITEMS.NAME");
        ArrayList<String> itemRevenue = Utils.getDataValuesByColumnName(itemInfoFetcher, "ITEMS.REVENUE");
        System.out.println(itemNames.size());
        System.out.println(itemNames);
        XYChart.Series series = new XYChart.Series();
        itemRevenueComparisonBarChart.setStyle("-fx-font-size: 10px; -fx-font-weight: bold;-fx-font-family: Georgia");
        itemRevenueComparisonBarChart.setTitle("Item revenue");
        itemRevenueComparisonBarChart.setLegendVisible(false);
        for (int i=0;i<itemNames.size();++i) {

            series.getData().add(new XYChart.Data(itemNames.get(i), Double.parseDouble(itemRevenue.get(i))));
        }
        itemRevenueComparisonBarChart.getData().addAll(series);

    }

    public void itemCategoryRevenueComparisonPieChartInit() {
        ArrayList<ArrayList<String>> itemCategoryRevenueFetcher = main.getProcessorManager().getAnalyticsProcessor().getItemCategoryRevenue("").getData();
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        ArrayList<String> itemCategoryNames = Utils.getDataValuesByColumnName(itemCategoryRevenueFetcher, "ITEM_CATEGORY_NAME");
        ArrayList<String> itemCategoryRevenue = Utils.getDataValuesByColumnName(itemCategoryRevenueFetcher, "ITEM_CATEGORY_REVENUE");
        for (int i=0;i<itemCategoryRevenue.size();++i){
            pieChartData.add(new PieChart.Data(itemCategoryNames.get(i), Double.parseDouble(itemCategoryRevenue.get(i))));
        }
        itemCategoryRevenueComparisonPieChart.setStyle("-fx-font-size: 10px; -fx-font-weight: bold;-fx-font-family: Georgia");
        itemCategoryRevenueComparisonPieChart.setData(pieChartData);
        itemCategoryRevenueComparisonPieChart.setLegendVisible(true);
        itemCategoryRevenueComparisonPieChart.setTitle("Comparison between item categories revenue");
    }
    public void top10MoviesRevenueBarChartInit() {
        ArrayList<ArrayList<String>> top10MoviesInfoFetcher = main.getProcessorManager().getMovieManagementProcessor().getData(0, 10, "", "REVENUE DESC").getData();
        System.out.println(top10MoviesInfoFetcher);
        ArrayList<String> top10MovieTitles = Utils.getDataValuesByColumnName(top10MoviesInfoFetcher, "TITLE");
        ArrayList<String> top10MovieRevenue = Utils.getDataValuesByColumnName(top10MoviesInfoFetcher, "REVENUE");

        XYChart.Series series = new XYChart.Series();
        top10MoviesRevenueBarChart.setStyle("-fx-font-size: 10px; -fx-font-weight: bold;-fx-font-family: Georgia");
        top10MoviesRevenueBarChart.setTitle("Top 10 movies revenue");
        top10MoviesRevenueBarChart.setLegendVisible(false);
        for (int i=0;i<top10MovieTitles.size();++i) {

            series.getData().add(0, new XYChart.Data(Double.parseDouble(top10MovieRevenue.get(i)), top10MovieTitles.get(i)));
        }
        top10MoviesRevenueBarChart.getData().addAll(series);

    }
    public void movieGenreRevenuePieChartInit() {
        ArrayList<ArrayList<String>> genreRevenueInfoFetcher = main.getProcessorManager().getAnalyticsProcessor().getMovieGenreRevenue("").getData();
        ArrayList<String> genreNames = Utils.getDataValuesByColumnName(genreRevenueInfoFetcher, "GENRE_NAME");
        double totalGenreRevenue = 0;
        ArrayList<String> genreRevenueList = Utils.getDataValuesByColumnName(genreRevenueInfoFetcher, "GENRE_REVENUE");;
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for (String genreRevenue : genreRevenueList) {
            totalGenreRevenue += Double.parseDouble(genreRevenue);
        }
       for (String genreRevenue : genreRevenueList) {
           pieChartData.add(new PieChart.Data(genreNames.get(genreRevenueList.indexOf(genreRevenue)), Double.parseDouble(genreRevenue)/totalGenreRevenue * 100));
       }
        movieGenreRevenuePieChart.setData(pieChartData);
        movieGenreRevenuePieChart.setLegendVisible(true);
        movieGenreRevenuePieChart.setStyle("-fx-font-size: 10px; -fx-font-weight: bold;-fx-font-family: Georgia");
        movieGenreRevenuePieChart.setTitle("Comparison between Movie Genres Revenue");
    }
    public void overallRevenueLineChartInit() {
        int []months = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
        String[] todayDateSplitted = this.getTodayDate().split("-");
        int year = Integer.parseInt(todayDateSplitted[0]);

        XYChart.Series ticketRevenueSeries = new XYChart.Series();
        XYChart.Series itemRevenueSeries = new XYChart.Series();
        XYChart.Series overallRevenueSeries = new XYChart.Series();
        ticketRevenueSeries.setName("Ticket");
        itemRevenueSeries.setName("Item");
        overallRevenueSeries.setName("Total");

        for (int month : months) {
            YearMonth yearMonthObject = YearMonth.of(year, month);
            int daysInMonth = yearMonthObject.lengthOfMonth();
            String startDate = String.format("%s-%02d-01", year, month);
            String endDate = String.format("%s-%02d-%02d", year, month, daysInMonth);
            double ticketRevenue = Double.parseDouble(Utils.getRowValueByColumnName(2, "TICKET_REVENUE", main.getProcessorManager().getAnalyticsProcessor().getTicketRevenue(String.format("PAYMENT_DATE >= '%s' AND PAYMENT_DATE <= '%s'", startDate, endDate)).getData()));
            double itemRevenue = Double.parseDouble(Utils.getRowValueByColumnName(2, "ITEM_REVENUE", main.getProcessorManager().getAnalyticsProcessor().getItemRevenue(String.format("PAYMENT_DATE >= '%s' AND PAYMENT_DATE <= '%s'", startDate, endDate)).getData()));
            double overallRevenue = ticketRevenue + itemRevenue;
            System.out.println(ticketRevenue + " " + itemRevenue + " " + overallRevenue);
            ticketRevenueSeries.getData().add(new XYChart.Data(new DateFormatSymbols().getMonths()[month-1], ticketRevenue));
            itemRevenueSeries.getData().add(new XYChart.Data(new DateFormatSymbols().getMonths()[month-1], itemRevenue));
            overallRevenueSeries.getData().add(new XYChart.Data(new DateFormatSymbols().getMonths()[month-1], overallRevenue));

        }
        overallRevenueLineChart.setStyle("-fx-font-size: 10px; -fx-font-weight: bold;-fx-font-family: Georgia");
        overallRevenueLineChart.getData().addAll(ticketRevenueSeries, itemRevenueSeries, overallRevenueSeries);
        overallRevenueLineChart.setTitle(String.format("%s revenue", year));
    }
}
