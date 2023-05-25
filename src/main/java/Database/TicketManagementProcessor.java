package Database;

import Utils.Response;

public class TicketManagementProcessor extends Processor{
    public TicketManagementProcessor(){
        super();
        setDefaultDatabaseTable("TICKETS");
    }
    @Override
    public Response getData(int from, int quantity, String queryCondition, String sortQuery) {
        if (queryCondition.length() == 0) {
            queryCondition = "SC.SHOW_TIME_ID = ST.ID AND S.ID = T.SCHEDULE_ID AND T.SEAT_ID = S.ID AND C.ID = SR.CINEMA_ID AND SR.ID = SC.SCREEN_ROOM_ID";
        } else {
            queryCondition = queryCondition + " AND SC.SHOW_TIME_ID = ST.ID AND S.ID = T.SCHEDULE_ID AND T.SEAT_ID = S.ID AND C.ID = SR.CINEMA_ID AND SR.ID = SC.SCREEN_ROOM_ID";
        }
        Response response = select("T.ID, T.AMOUNT, SC.SHOW_DATE, ST.START_TIME, S.NAME", from ,quantity, queryCondition, sortQuery, "TICKETS T, SEATS S, SCHEDULES SC, SHOW_TIMES ST, SCREEN_ROOMS SR, CINEMAS C");
        return response;
    }
}
