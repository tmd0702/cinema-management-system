package Database;

import UserManager.User;
import Utils.Response;

public class UserCategoryManagementProcessor extends Processor {
    public UserCategoryManagementProcessor() {
        super();
        setDefaultDatabaseTable("USER_CATEGORY");
    }
    @Override
    public Response getData(int from, int quantity, String queryCondition, String sortQuery) {
        Response response = select("*", from, quantity, queryCondition, sortQuery, getDefaultDatabaseTable());
        return response;
    }
}
