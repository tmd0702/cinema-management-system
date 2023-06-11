package Database;

import Utils.Response;

import java.util.HashMap;

public class MovieGenreManagementProcessor extends Processor {
    public MovieGenreManagementProcessor() {
        super();
        setDefaultDatabaseTable("GENRES");
    }
    public Response getData(int from, int quantity, String queryCondition, String sortQuery) {
        Response response = select("GENRES.ID AS 'GENRES.ID', GENRES.NAME AS 'GENRES.NAME'", from, quantity, queryCondition, sortQuery, getDefaultDatabaseTable());
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
