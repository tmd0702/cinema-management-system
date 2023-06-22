package Database;

import Utils.Response;

import java.util.HashMap;

public class TicketManagementProcessor extends Processor{
    public TicketManagementProcessor(){
        super();
        setDefaultDatabaseTable("TICKETS");
    }
    @Override
    public Response getData(int from, int quantity, String queryCondition, String sortQuery) {
        if (queryCondition.length() == 0) {
            queryCondition = "T.SEAT_ID = S.ID";
        } else {
            queryCondition = queryCondition + " AND T.SEAT_ID = S.ID";
        }
        Response response = select("T.ID AS 'T.ID',S.ID AS 'S.ID'", from ,quantity, queryCondition, sortQuery, "TICKETS T, SEATS S");
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
