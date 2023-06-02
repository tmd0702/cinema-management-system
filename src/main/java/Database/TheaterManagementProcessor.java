package Database;

import Utils.Response;

import java.util.HashMap;

public class TheaterManagementProcessor extends Processor {
    public TheaterManagementProcessor() {
        super();
        setDefaultDatabaseTable("CINEMAS");
    }
    public Response getData(int from, int quantity, String queryCondition, String sortQuery) {
        Response response = select("CINEMAS.ID AS 'CINEMAS.ID', CINEMAS.NAME AS 'CINEMAS.NAME', CINEMAS.ADDRESS AS 'CINEMAS.ADDRESS', CINEMAS.CINE_AREA AS 'CINEMAS.CINE_AREA'", from, quantity, queryCondition, sortQuery, getDefaultDatabaseTable());
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
