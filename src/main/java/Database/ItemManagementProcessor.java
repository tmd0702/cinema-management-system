package Database;

import Utils.Response;

import java.util.HashMap;

public class ItemManagementProcessor extends Processor {
    public ItemManagementProcessor() {
        super();
        setDefaultDatabaseTable("ITEMS");
    }
    public Response getData(int from, int quantity, String queryCondition, String sortQuery) {
        Response response = select("ITEMS.ID AS 'ITEMS.ID', ITEMS.NAME AS 'ITEMS.NAME', ITEMS.CATEGORY AS 'ITEMS.CATEGORY', ITEMS.PRICE AS 'ITEMS.PRICE', ITEMS.REVENUE AS 'ITEMS.REVENUE'", from, quantity, queryCondition, sortQuery, getDefaultDatabaseTable());
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
