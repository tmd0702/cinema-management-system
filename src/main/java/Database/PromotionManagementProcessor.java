package Database;

import Utils.Response;

import java.util.HashMap;

public class PromotionManagementProcessor extends Processor {
    public PromotionManagementProcessor() {
        super();
        setDefaultDatabaseTable("PROMOTIONS");
    }
    public Response getData(int from, int quantity, String queryCondition, String sortQuery) {
        if (queryCondition.length() > 0) {
            queryCondition = queryCondition + " AND UC.ID = P.USER_CATEGORY_ID";
        } else {
            queryCondition = "UC.ID = P.USER_CATEGORY_ID";
        }
        Response response = select("P.*, UC.CATEGORY AS USER_CATEGORY_CATEGORY", from, quantity, queryCondition, sortQuery, "PROMOTIONS P, USER_CATEGORY UC");
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
