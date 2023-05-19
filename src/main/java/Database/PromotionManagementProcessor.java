package Database;

import Utils.Response;

public class PromotionManagementProcessor extends Processor {
    public PromotionManagementProcessor() {
        super();
        setDefaultDatabaseTable("PROMOTIONS");
    }
    public Response getData(int from, int quantity, String queryCondition, String sortQuery) {
        Response response = select("*", from, quantity, queryCondition, sortQuery, getDefaultDatabaseTable());
        return response;
    }
}
