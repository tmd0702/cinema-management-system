package Database;

import Utils.Response;

import java.util.HashMap;

public class ItemPriceManagementProcessor extends Processor {
    public ItemPriceManagementProcessor(){
        super();
        setDefaultDatabaseTable("ITEM_PRICES");
    }
    public Response getData(int from, int quantity, String queryCondition, String sortQuery) {
        if (queryCondition.length() > 0) {
            queryCondition = queryCondition + " AND ITEM_PRICES.ITEM_CATEGORY_ID = ITEM_CATEGORY.ID";
        } else {
            queryCondition = "ITEM_PRICES.ITEM_CATEGORY_ID = ITEM_CATEGORY.ID";
        }
        Response response = select("ITEM_PRICES.ID AS 'ITEM_PRICES.ID', ITEM_PRICES.PRICE AS 'ITEM_PRICES.PRICE', ITEM_PRICES.DATE AS 'ITEM_PRICES.DATE', ITEM_CATEGORY.CATEGORY AS 'ITEM_CATEGORY.CATEGORY'", from, quantity, queryCondition, sortQuery, "ITEM_PRICES, ITEM_CATEGORY");
        return response;
    }
    public Response updateData(HashMap<String, String> columnValueMap, String queryCondition, boolean isCommit) {
        return update(columnValueMap, queryCondition, getDefaultDatabaseTable(), isCommit);
    }
    public Response insertData(HashMap<String, String> columnValueMap, boolean isCommit) {
        return insert(columnValueMap, getDefaultDatabaseTable(), isCommit);
    }
    public int countData(String queryCondition) {
        return count(queryCondition, getDefaultDatabaseTable());
    }
    public Response deleteData(String queryCondition, boolean isCommit) {
        return delete(queryCondition, getDefaultDatabaseTable(), isCommit);
    }
}
