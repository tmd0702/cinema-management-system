package Database;

import Utils.Response;

import java.util.ArrayList;

public class ShowTimeManagementProcessor extends Processor{
    public ShowTimeManagementProcessor(){
        super();
        setDefaultDatabaseTable("SHOW_TIMES");
    }
    public Response getData(int from, int quantity, String queryCondition, String sortQuery) {
        if (queryCondition.length() > 0) {
            queryCondition = "SR.ID = ST.SCREEN_ROOM_ID AND SR.CINEMA_ID = C.ID AND " + queryCondition;
        } else {
            queryCondition = "SR.ID = ST.SCREEN_ROOM_ID AND SR.CINEMA_ID = C.ID";
        }
        Response response = select("ST.ID, ST.START_TIME, ST.SHOW_DATE, SR.NAME AS SCREEN_ROOM_NAME, C.NAME AS CINEMA_NAME", from, quantity, queryCondition, sortQuery, String.format("%s, SCREEN_ROOMS SR, CINEMAS C", getDefaultDatabaseTable() + " ST"));
        return response;
    }
}
