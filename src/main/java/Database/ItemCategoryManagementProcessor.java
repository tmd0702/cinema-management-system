package Database;

import Utils.Response;

import java.util.HashMap;

public class ItemCategoryManagementProcessor extends Processor {
    public ItemCategoryManagementProcessor() {
        super();
        setDefaultDatabaseTable("ITEM_CATEGORY");
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
    public Response updateData(HashMap<String, String> columnValueMap, String queryCondition, boolean isCommit) {
        return update(columnValueMap, queryCondition, getDefaultDatabaseTable(), isCommit);
    }
    @Override
    public Response getData(int from, int quantity, String queryCondition, String sortQuery) {
        if (queryCondition.length() > 0) {
            queryCondition = queryCondition + " AND ITEM_PRICES.ITEM_CATEGORY_ID = ITEM_CATEGORY.ID AND ITEM_PRICES.DATE = (SELECT MAX(DATE) FROM ITEM_PRICES WHERE ITEM_CATEGORY_ID = ITEM_CATEGORY.ID)";
        } else {
            queryCondition = "ITEM_PRICES.ITEM_CATEGORY_ID = ITEM_CATEGORY.ID AND ITEM_PRICES.DATE = (SELECT MAX(DATE) FROM ITEM_PRICES WHERE ITEM_CATEGORY_ID = ITEM_CATEGORY.ID)";
        }
        return select("ITEM_CATEGORY.ID AS 'ITEM_CATEGORY.ID', ITEM_CATEGORY.CATEGORY AS 'ITEM_CATEGORY.CATEGORY', ITEM_PRICES.PRICE AS 'ITEM_PRICES.PRICE'", from, quantity, queryCondition, sortQuery, "ITEM_CATEGORY, ITEM_PRICES");
    }
}
