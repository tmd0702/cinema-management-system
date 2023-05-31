package Database;

import Utils.Response;

import java.util.HashMap;

public class ItemTicketManagementProcessor extends Processor{
    public ItemTicketManagementProcessor() {
        setDefaultDatabaseTable("PAYMENT_ITEMS");
    }

    @Override
    public Response getData(int from, int quantity, String queryCondition, String sortQuery) {
        if (queryCondition.length() == 0) {
            queryCondition = "BI.ITEM_ID = I.ID AND BI.PAYMENT_ID = P.ID AND P.USER_ID = U.ID";
        } else {
            queryCondition = queryCondition + " AND BI.ITEM_ID = I.ID AND BI.PAYMENT_ID = P.ID AND P.USER_ID = U.ID";
        }
        Response response = select("I.ID AS ITEM_ID, I.NAME AS ITEM_NAME, I.CATEGORY AS ITEM_CATEGORY, I.PRICE AS ITEM_PRICE, BI.QUANTITY AS BOOKING_ITEM_QUANTITY, P.ID AS PAYMENT_ID, U.USERNAME AS USER_USERNAME", from, quantity, queryCondition, sortQuery, "BOOKING_ITEMS BI, ITEMS I, PAYMENTS P, USERS U");
        return response;
    }
    public Response updateData(HashMap<String, String> columnValueMap, String queryCondition, boolean isCommit) {
        return update(columnValueMap, queryCondition, getDefaultDatabaseTable(), isCommit);
    }
    public Response insertData(HashMap<String, String> columnValueMap, boolean isCommit) {
        return insert(columnValueMap, getDefaultDatabaseTable(), isCommit);
    }
    public Response deleteData(String queryCondition, boolean isCommit) {
        return delete(queryCondition, getDefaultDatabaseTable(), isCommit);
    }
}
