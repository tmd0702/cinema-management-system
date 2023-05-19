package Database;

import Utils.Response;

public class SeatManagementProcessor extends Processor {
    public SeatManagementProcessor(){
        super();
        setDefaultDatabaseTable("SEATS");
    }
    public Response getData(int from, int quantity, String queryCondition, String sortQuery) {
        Response response = select("*", from, quantity, queryCondition, sortQuery, getDefaultDatabaseTable());
        return response;
    }
}
