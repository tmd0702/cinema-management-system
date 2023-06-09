package Database;

import Utils.Response;

import java.util.HashMap;

public class CinemaManagementProcessor extends Processor {
    public CinemaManagementProcessor() {
        super();
        setDefaultDatabaseTable("CINEMAS");
    }
    public Response getData(int from, int quantity, String queryCondition, String sortQuery) {
        Response response = select("CINEMAS.ID AS 'CINEMAS.ID', CINEMAS.STATUS AS 'CINEMAS.STATUS', CINEMAS.NAME AS 'CINEMAS.NAME', CINEMAS.ADDRESS AS 'CINEMAS.ADDRESS'", from, quantity, queryCondition, sortQuery, getDefaultDatabaseTable());
        return response;
    }
    public int countData(String queryCondition) {
        return count(queryCondition, getDefaultDatabaseTable());
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
