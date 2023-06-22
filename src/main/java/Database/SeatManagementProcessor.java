package Database;

import Utils.Response;

import java.util.HashMap;

public class SeatManagementProcessor extends Processor {
    public SeatManagementProcessor(){
        super();
        setDefaultDatabaseTable("SEATS");
    }
    public Response getData(int from, int quantity, String queryCondition, String sortQuery) {
        if(queryCondition.length() == 0){
            queryCondition = "SEATS.SEAT_CATEGORY_ID = SEAT_CATEGORY.ID";
        }else{
            queryCondition += " AND SEATS.SEAT_CATEGORY_ID = SEAT_CATEGORY.ID";
        }
        Response response = select("SEATS.ID AS 'SEATS.ID', SEATS.NAME AS 'SEATS.NAME', SEATS.STATUS AS 'SEATS.STATUS', SEAT_CATEGORY.CATEGORY AS 'SEAT_CATEGORY.CATEGORY', SEATS.SCREEN_ROOM_ID AS 'SEATS.SCREEN_ROOM_ID', SEATS.SEAT_CATEGORY_ID AS 'SEATS.SEAT_CATEGORY_ID'", from, quantity, queryCondition, sortQuery, "SEATS, SEAT_CATEGORY");
        return response;
    }
    public Response updateData(HashMap<String, String> columnValueMap, String queryCondition, boolean isCommit) {
        return update(columnValueMap, queryCondition, getDefaultDatabaseTable(), isCommit);
    }
    public int countData(String queryCondition) {
        return count(queryCondition, getDefaultDatabaseTable());
    }
    public Response insertData(HashMap<String, String> columnValueMap, boolean isCommit) {
        return insert(columnValueMap, getDefaultDatabaseTable(), isCommit);
    }
    public Response deleteData(String queryCondition, boolean isCommit) {
        return delete(queryCondition, getDefaultDatabaseTable(), isCommit);
    }
}
