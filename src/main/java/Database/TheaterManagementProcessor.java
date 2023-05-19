package Database;

import Utils.Response;

public class TheaterManagementProcessor extends Processor {
    public TheaterManagementProcessor() {
        super();
        setDefaultDatabaseTable("CINEMAS");
    }
    public Response getData(int from, int quantity, String queryCondition, String sortQuery) {
        Response response = select("*", from, quantity, queryCondition, sortQuery, getDefaultDatabaseTable());
        return response;
    }
}
