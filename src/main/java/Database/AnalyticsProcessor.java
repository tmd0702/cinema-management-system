package Database;

import Utils.Response;
import com.example.GraphicalUserInterface.ManagementMain;

import java.util.HashMap;

public class AnalyticsProcessor extends Processor {
    private ManagementMain main;
    public AnalyticsProcessor() {
        main = ManagementMain.getInstance();
    }
    public Response getData(int from, int quantity, String queryCondition, String sortQuery) {
        Response response = select("*", from, quantity, queryCondition, sortQuery, String.format("%s", getDefaultDatabaseTable()));
        return response;
    }
    public Response updateData(HashMap<String, String> columnValueMap, String queryCondition, boolean isCommit) {
        return update(columnValueMap, queryCondition, getDefaultDatabaseTable(), isCommit);
    }
    public int countData(String queryCondition) {
        return count(queryCondition, getDefaultDatabaseTable());
    }
    public Response insertData(HashMap<String, String> columnValueMap, boolean isCommit) {
        return insert(columnValueMap, getDefaultDatabaseTable(), isCommit);
    }
    public Response deleteData(String queryCondition, boolean isCommit) {
        return delete(queryCondition, getDefaultDatabaseTable(), isCommit);
    }
    public Response getOverallRevenue(String queryCondition) {
            Response response = select("COALESCE(SUM(PAYMENTS.TOTAL_AMOUNT), 0) AS OVERALL_REVENUE", 0, 1, queryCondition, "", "PAYMENTS");
        return response;

    }
    public Response getTicketRevenue(String queryCondition) {
        if (queryCondition.length() == 0) {
            queryCondition = "PAYMENTS.ID = BOOKING_TICKETS.PAYMENT_ID AND BOOKING_TICKETS.TICKET_ID = TICKETS.ID AND TICKETS.SEAT_ID = SEATS.ID AND SEATS.SEAT_CATEGORY_ID = SEAT_PRICES.SEAT_CATEGORY_ID";
        } else {
            queryCondition += " AND PAYMENTS.ID = BOOKING_TICKETS.PAYMENT_ID AND BOOKING_TICKETS.TICKET_ID = TICKETS.ID AND TICKETS.SEAT_ID = SEATS.ID AND SEATS.SEAT_CATEGORY_ID = SEAT_PRICES.SEAT_CATEGORY_ID";
        }
        Response response = select("COALESCE(SUM(SEAT_PRICES.PRICE), 0) AS TICKET_REVENUE", 0, 1, queryCondition, "", "PAYMENTS, BOOKING_TICKETS, TICKETS, SEATS, SEAT_PRICES");
        return response;
    }
    public Response getItemRevenue(String queryCondition) {
        if (queryCondition.length() == 0) {
            queryCondition = "PAYMENTS.ID = BOOKING_ITEMS.PAYMENT_ID AND BOOKING_ITEMS.ITEM_ID = ITEMS.ID AND ITEMS.ITEM_CATEGORY_ID = ITEM_PRICES.ITEM_CATEGORY_ID";
        } else {
            queryCondition += " AND PAYMENTS.ID = BOOKING_ITEMS.PAYMENT_ID AND BOOKING_ITEMS.ITEM_ID = ITEMS.ID AND ITEMS.ITEM_CATEGORY_ID = ITEM_PRICES.ITEM_CATEGORY_ID";
        }
        Response response = select("COALESCE(SUM(ITEM_PRICES.PRICE), 0) AS ITEM_REVENUE", 0, 1, queryCondition, "", "PAYMENTS, BOOKING_ITEMS, ITEMS, ITEM_PRICES");
        return response;
    }
    public Response getItemCategoryRevenue(String queryCondition) {
        if (queryCondition.length() == 0) {
            queryCondition = "ITEMS.ITEM_CATEGORY_ID = ITEM_CATEGORY.ID GROUP BY ITEM_CATEGORY.ID";
        } else {
            queryCondition += " AND ITEMS.ITEM_CATEGORY_ID = ITEM_CATEGORY.ID GROUP BY ITEM_CATEGORY.ID";
        }
        Response response = select("ITEM_CATEGORY.CATEGORY AS 'ITEM_CATEGORY_NAME', COALESCE(SUM(ITEMS.REVENUE), 0) AS 'ITEM_CATEGORY_REVENUE'", 0, -1, queryCondition, "", "ITEMS RIGHT JOIN ITEM_CATEGORY ON ITEMS.ITEM_CATEGORY_ID = ITEM_CATEGORY.ID");
        return response;
    }
    public Response getMovieGenreRevenue(String queryCondition) {
        if (queryCondition.length() == 0) {
            queryCondition = "M.ID = MG.MOVIE_ID AND G.ID = MG.GENRE_ID GROUP BY G.ID";
        } else {
            queryCondition += " AND M.ID = MG.MOVIE_ID AND G.ID = MG.GENRE_ID GROUP BY G.ID";
        }
        Response response = select("G.NAME AS GENRE_NAME, COALESCE(SUM(M.REVENUE), 0) AS GENRE_REVENUE", 0, -1, queryCondition, "", "MOVIES M, GENRES G, MOVIE_GENRES MG");
        return response;
    }
}
