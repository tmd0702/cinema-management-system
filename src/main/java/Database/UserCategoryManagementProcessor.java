package Database;

import UserManager.User;
import Utils.Response;

import java.util.HashMap;

public class UserCategoryManagementProcessor extends Processor {
    public UserCategoryManagementProcessor() {
        super();
        setDefaultDatabaseTable("USER_CATEGORY");
    }
    @Override
    public Response getData(int from, int quantity, String queryCondition, String sortQuery) {
        Response response = select("ID, CATEGORY, POINT_LOWERBOUND", from, quantity, queryCondition, sortQuery, getDefaultDatabaseTable());
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
