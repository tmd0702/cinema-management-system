package Database;

import Utils.Response;

public class PaymentManagementProcessor extends Processor {
    public PaymentManagementProcessor() {
        super();
        setDefaultDatabaseTable("PAYMENTS");
    }

    @Override
    public Response getData(int from, int quantity, String queryCondition, String sortQuery) {
        Response response = select("*", from, quantity, queryCondition, sortQuery, getDefaultDatabaseTable());
        return response;
    }
}
