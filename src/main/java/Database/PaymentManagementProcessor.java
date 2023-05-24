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
            queryCondition = "U.ID = P.USER_ID";
        } else {
            queryCondition = queryCondition + " AND U.ID = P.USER_ID";
        }
        Response response = select("P.ID, P.PAYMENT_DATE, P.PAYMENT_METHOD, P.TOTAL_AMOUNT, U.USERNAME", from, quantity, queryCondition, sortQuery, "PAYMENTS P, USERS U");
        return response;
    }
}
