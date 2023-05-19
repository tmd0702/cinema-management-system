package Database;

import Utils.Response;

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
}
