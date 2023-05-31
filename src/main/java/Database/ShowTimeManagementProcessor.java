package Database;

import Utils.Response;

import java.util.ArrayList;
import java.util.HashMap;

public class ShowTimeManagementProcessor extends Processor{
    public ShowTimeManagementProcessor(){
        super();
        setDefaultDatabaseTable("SHOW_TIMES");
    }
    public Response getData(int from, int quantity, String queryCondition, String sortQuery) {
        Response response = select("ID, START_TIME", from, quantity, queryCondition, sortQuery, String.format("%s", getDefaultDatabaseTable()));
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
