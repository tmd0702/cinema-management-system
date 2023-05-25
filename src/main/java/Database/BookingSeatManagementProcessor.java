package Database;

import Utils.Response;

public class BookingSeatManagementProcessor extends Processor{
    public BookingSeatManagementProcessor(){
        super();
        setDefaultDatabaseTable("BOOKING_TICKETS");
    }
    public Response getData(int from, int quantity, String queryCondition, String sortQuery) {
        Response response = select("*", from, quantity, queryCondition, sortQuery, getDefaultDatabaseTable());
        return response;
    }
}
