package Database;

import Utils.Response;

public class ItemTicketManagementProcessor extends Processor{
    public ItemTicketManagementProcessor() {
        setDefaultDatabaseTable("PAYMENT_ITEMS");
    }

    @Override
    public Response getData(int from, int quantity, String queryCondition, String sortQuery) {
        if (queryCondition.length() == 0) {
            queryCondition = "BI.ITEM_ID = I.ID AND BI.PAYMENT_ITEM_ID = PI.ID";
        } else {
            queryCondition = queryCondition + " AND BI.ITEM_ID = I.ID AND BI.PAYMENT_ITEM_ID = PI.ID";
        }
        Response response = select("I.ID AS ITEM_ID, I.NAME AS ITEM_NAME, I.CATEGORY AS ITEM_CATEGORY, I.PRICE AS ITEM_PRICE, BI.QUANTITY AS BOOKING_ITEM_QUANTITY, PI.ID AS PAYMENT_ITEM_ID", from, quantity, queryCondition, sortQuery, "BOOKING_ITEMS BI, PAYMENT_ITEMS PI, ITEMS I");
        return response;
    }
}
