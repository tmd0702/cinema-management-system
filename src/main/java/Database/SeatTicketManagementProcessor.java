package Database;

import Utils.Response;

public class SeatTicketManagementProcessor extends Processor {
    public SeatTicketManagementProcessor() {
        super();
        setDefaultDatabaseTable("PAYMENT_TICKETS");
    }

    @Override
    public Response getData(int from, int quantity, String queryCondition, String sortQuery) {
        if (queryCondition.length() == 0) {
            queryCondition = "SC.SHOW_TIME_ID = ST.ID AND SC.ID = T.SCHEDULE_ID AND T.SEAT_ID = S.ID AND C.ID = SR.CINEMA_ID AND SR.ID = SC.SCREEN_ROOM_ID AND M.ID = SC.MOVIE_ID AND BT.TICKET_ID = T.ID AND BT.PAYMENT_TICKET_ID = P.PAYMENT_TICKET_ID AND U.ID = P.USER_ID";
        } else {
            queryCondition = queryCondition + " AND SC.SHOW_TIME_ID = ST.ID AND SC.ID = T.SCHEDULE_ID AND T.SEAT_ID = S.ID AND C.ID = SR.CINEMA_ID AND SR.ID = SC.SCREEN_ROOM_ID AND M.ID = SC.MOVIE_ID AND BT.TICKET_ID = T.ID AND BT.PAYMENT_TICKET_ID = P.PAYMENT_TICKET_ID AND U.ID = P.USER_ID";
        }
        Response response = select("T.ID AS TICKET_ID, T.AMOUNT AS TICKET_AMOUNT, SC.SHOW_DATE AS SCHEDULE_SHOW_DATE, ST.START_TIME AS SHOW_TIME_START_TIME, S.NAME AS SEAT_NAME, C.NAME AS CINEMA_NAME, SR.NAME AS SCREEN_ROOM_NAME, M.TITLE AS MOVIE_TITLE, P.ID AS PAYMENT_ID, U.USERNAME AS USER_USERNAME", from ,quantity, queryCondition, sortQuery, "TICKETS T, SEATS S, SCHEDULES SC, SHOW_TIMES ST, SCREEN_ROOMS SR, CINEMAS C, MOVIES M, PAYMENTS P, BOOKING_TICKETS BT, USERS U");
        return response;
    }
}
