package Database;

import Utils.Response;

public class PaymentMethodManagementProcessor  extends Processor{
    public PaymentMethodManagementProcessor(){
        super();
        setDefaultDatabaseTable("PAYMENT_METHODS");
    }
    public Response getData(int from, int quantity, String queryCondition, String sortQuery) {
        Response response = select("*", from, quantity, queryCondition, sortQuery, getDefaultDatabaseTable());
        return response;
    }
}