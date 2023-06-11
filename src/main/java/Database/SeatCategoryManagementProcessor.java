package Database;

import Utils.Response;

import java.util.HashMap;

public class SeatCategoryManagementProcessor extends Processor {
    public SeatCategoryManagementProcessor() {
        super();
        setDefaultDatabaseTable("SEAT_CATEGORY");
    }
    public Response insertData(HashMap<String, String> columnValueMap, boolean isCommit) {
        return insert(columnValueMap, getDefaultDatabaseTable(), isCommit);
    }
    public Response deleteData(String queryCondition, boolean isCommit) {
        return delete(queryCondition, getDefaultDatabaseTable(), isCommit);
    }
    public Response updateData(HashMap<String, String> columnValueMap, String queryCondition, boolean isCommit) {
        return update(columnValueMap, queryCondition, getDefaultDatabaseTable(), isCommit);
    }
    public int countData(String queryCondition) {
        return count(queryCondition, getDefaultDatabaseTable());
    }
    @Override
    public Response getData(int from, int quantity, String queryCondition, String sortQuery) {
        if (queryCondition.length() > 0) {
            queryCondition = queryCondition + " AND SEAT_PRICES.SEAT_CATEGORY_ID = SEAT_CATEGORY.ID AND SEAT_PRICES.DATE = (SELECT MAX(DATE) FROM SEAT_PRICES WHERE SEAT_CATEGORY_ID = SEAT_CATEGORY.ID)";
        } else {
            queryCondition = "SEAT_PRICES.SEAT_CATEGORY_ID = SEAT_CATEGORY.ID AND SEAT_PRICES.DATE = (SELECT MAX(DATE) FROM SEAT_PRICES WHERE SEAT_CATEGORY_ID = SEAT_CATEGORY.ID)";
        }
        return select("SEAT_CATEGORY.ID AS 'SEAT_CATEGORY.ID', SEAT_CATEGORY.CATEGORY AS 'SEAT_CATEGORY.CATEGORY', SEAT_PRICES.PRICE AS 'SEAT_PRICES.PRICE'", from, quantity, queryCondition, sortQuery, "SEAT_CATEGORY, SEAT_PRICES");
    }
}
