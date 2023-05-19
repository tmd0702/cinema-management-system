package Database;

import Utils.Response;

public class ItemManagementProcessor extends Processor {
    public ItemManagementProcessor() {
        super();
        setDefaultDatabaseTable("ITEMS");
    }
    public Response getData(int from, int quantity, String queryCondition, String sortQuery) {
        Response response = select("*", from, quantity, queryCondition, sortQuery, getDefaultDatabaseTable());
        return response;
    }
}
