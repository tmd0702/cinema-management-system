package Database;

import Utils.Response;

public class PaymentManagementProcessor extends Processor {
    public PaymentManagementProcessor() {
        super();
        setDefaultDatabaseTable("PAYMENTS");
    }

    @Override
    public Response getData(int from, int quantity, String queryCondition, String sortQuery) {
        if (queryCondition.length() == 0) {
            queryCondition = "U.ID = P.USER_ID AND PM.ID = P.PAYMENT_METHOD_ID";
        } else {
            queryCondition = queryCondition + " AND U.ID = P.USER_ID AND PM.ID = P.PAYMENT_METHOD_ID";
        }
        Response response = select("P.ID AS PAYMENT_ID, P.PAYMENT_DATE  AS PAYMENT_PAYMENT_DATE, PM.NAME AS PAYMENT_METHOD_NAME, P.TOTAL_AMOUNT AS PAYMENT_TOTAL_AMOUNT, U.USERNAME AS USER_USERNAME", from, quantity, queryCondition, sortQuery, "PAYMENTS P, USERS U, PAYMENT_METHODS PM");
        return response;
    }
}
