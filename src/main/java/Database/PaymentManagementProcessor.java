package Database;

import Utils.Response;

import java.util.HashMap;

public class PaymentManagementProcessor extends Processor {
    public PaymentManagementProcessor() {
        super();
        setDefaultDatabaseTable("PAYMENTS");
    }

    @Override
    public Response getData(int from, int quantity, String queryCondition, String sortQuery) {
        if (queryCondition.length() == 0) {
            queryCondition = "U.ID = P.USER_ID AND PM.ID = P.PAYMENT_METHOD_ID AND S.ID = P.SCHEDULE_ID AND SR.ID = S.SCREEN_ROOM_ID AND C.ID = SR.CINEMA_ID AND S.MOVIE_ID = M.ID";
        } else {
            queryCondition = queryCondition + " AND U.ID = P.USER_ID AND PM.ID = P.PAYMENT_METHOD_ID AND S.ID = P.SCHEDULE_ID AND SR.ID = S.SCREEN_ROOM_ID AND C.ID = SR.CINEMA_ID AND S.MOVIE_ID = M.ID";
        }
        Response response = select("P.ID AS PAYMENT_ID, P.PAYMENT_DATE  AS PAYMENT_PAYMENT_DATE, PM.NAME AS PAYMENT_METHOD_NAME, P.TOTAL_AMOUNT AS PAYMENT_TOTAL_AMOUNT, U.USERNAME AS USER_USERNAME, M.TITLE AS MOVIE_TITLE, C.NAME AS CINEMA_NAME", from, quantity, queryCondition, sortQuery, "PAYMENTS P, USERS U, PAYMENT_METHODS PM, SCHEDULES S, MOVIES M, SCREEN_ROOMS SR, CINEMAS C");
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
