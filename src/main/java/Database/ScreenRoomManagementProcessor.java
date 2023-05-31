package Database;

import Utils.Response;

import java.util.HashMap;

public class ScreenRoomManagementProcessor extends Processor {
    public ScreenRoomManagementProcessor() {
        super();
        setDefaultDatabaseTable("SCREEN_ROOMS");
    }
    public Response getData(int from, int quantity, String queryCondition, String sortQuery) {
        if (queryCondition.length() > 0) {
            queryCondition = "SR.CINEMA_ID = C.ID AND " + queryCondition;
        } else {
            queryCondition = "SR.CINEMA_ID = C.ID";
        }
        Response response = select("SR.ID, SR.NAME, SR.CAPACITY, C.NAME AS CINEMA_NAME", from, quantity, queryCondition, sortQuery, String.format("%s, CINEMAS C", getDefaultDatabaseTable() + " SR"));
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
