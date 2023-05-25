package Database;

import Utils.Response;

public class ItemTicketManagementProcessor extends Processor{
    public ItemTicketManagementProcessor() {
        setDefaultDatabaseTable("PAYMENT_ITEMS");
    }

    @Override
    public Response getData(int from, int quantity, String queryCondition, String sortQuery) {
        if (queryCondition.length() == 0) {
            queryCondition = "BI.ITEM_ID = I.ID AND BI.TICKET_ITEM_ID = TI.ID";
        } else {
            queryCondition = queryCondition + " AND BI.ITEM_ID = I.ID AND BI.TICKET_ITEM_ID = TI.ID";
        }
        Response response = select("I.NAME, I.CATEGORY, I.PRICE, BI.QUANTITY, TI.ID", from, quantity, queryCondition, sortQuery, "BOOKING_ITEMS BI, TICKET_ITEMS TI, ITEMS I");
        return response;
    }
}
