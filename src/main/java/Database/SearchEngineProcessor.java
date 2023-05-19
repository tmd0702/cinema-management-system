package Database;

import Utils.Response;

public class SearchEngineProcessor extends Processor {
    public SearchEngineProcessor() {
        super();
    }
    public Response getData(int from, int quantity, String queryCondition, String sortQuery) {
        Response response = select("*", from, quantity, queryCondition, sortQuery, getDefaultDatabaseTable());
        return response;
    }
}
