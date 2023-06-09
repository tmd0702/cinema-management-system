package Database;

import Utils.Response;

import java.util.HashMap;

public class PaymentMethodManagementProcessor  extends Processor{
    public PaymentMethodManagementProcessor(){
        super();
        setDefaultDatabaseTable("PAYMENT_METHODS");
    }
    public Response getData(int from, int quantity, String queryCondition, String sortQuery) {
        Response response = select("PAYMENT_METHODS.ID AS 'PAYMENT_METHODS.ID', PAYMENT_METHODS.STATUS AS 'PAYMENT_METHODS.STATUS', PAYMENT_METHODS.NAME AS 'PAYMENT_METHODS.NAME', PAYMENT_METHODS.STATUS AS 'PAYMENT_METHODS.STATUS'", from, quantity, queryCondition, sortQuery, getDefaultDatabaseTable());
        return response;
    }
    public Response updateData(HashMap<String, String> columnValueMap, String queryCondition, boolean isCommit) {
        return update(columnValueMap, queryCondition, getDefaultDatabaseTable(), isCommit);
    }
    public Response insertData(HashMap<String, String> columnValueMap, boolean isCommit) {
        return insert(columnValueMap, getDefaultDatabaseTable(), isCommit);
    }
    public int countData(String queryCondition) {
        return count(queryCondition, getDefaultDatabaseTable());
    }
    public Response deleteData(String queryCondition, boolean isCommit) {
        return delete(queryCondition, getDefaultDatabaseTable(), isCommit);
    }
}