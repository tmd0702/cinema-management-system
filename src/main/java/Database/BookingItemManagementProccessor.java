package Database;

import Utils.Response;

public class BookingItemManagementProccessor extends Processor{
    public BookingItemManagementProccessor(){
        super();
        setDefaultDatabaseTable("BOOKING_ITEMS");
    }
    public Response getData(int from, int quantity, String queryCondition, String sortQuery) {
        Response response = select("*", from, quantity, queryCondition, sortQuery, getDefaultDatabaseTable());
        return response;
    }
}
