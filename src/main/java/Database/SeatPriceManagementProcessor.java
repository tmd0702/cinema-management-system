package Database;

import Utils.Response;

import java.util.HashMap;

public class SeatPriceManagementProcessor extends Processor {
    public SeatPriceManagementProcessor(){
        super();
        setDefaultDatabaseTable("SEAT_PRICES");
    }
    public Response getData(int from, int quantity, String queryCondition, String sortQuery) {
        if (queryCondition.length() > 0) {
            queryCondition = queryCondition + " AND SEAT_PRICES.SEAT_CATEGORY_ID = SEAT_CATEGORY.ID";
        } else {
            queryCondition = "SEAT_PRICES.SEAT_CATEGORY_ID = SEAT_CATEGORY.ID";
        }
        Response response = select("SEAT_PRICES.ID AS 'SEAT_PRICES.ID', SEAT_PRICES.PRICE AS 'SEAT_PRICES.PRICE', SEAT_PRICES.DATE AS 'SEAT_PRICES.DATE', SEAT_CATEGORY.CATEGORY AS 'SEAT_CATEGORY.CATEGORY'", from, quantity, queryCondition, sortQuery, "SEAT_PRICES, SEAT_CATEGORY");
        return response;
    }
    public Response updateData(HashMap<String, String> columnValueMap, String queryCondition, boolean isCommit) {
        return update(columnValueMap, queryCondition, getDefaultDatabaseTable(), isCommit);
    }
    public Response insertData(HashMap<String, String> columnValueMap, boolean isCommit) {
        return insert(columnValueMap, getDefaultDatabaseTable(), isCommit);
    }
    public int countData(String queryCondition) {
        return count(queryCondition, getDefaultDatabaseTable());
    }
    public Response deleteData(String queryCondition, boolean isCommit) {
        return delete(queryCondition, getDefaultDatabaseTable(), isCommit);
    }
}
