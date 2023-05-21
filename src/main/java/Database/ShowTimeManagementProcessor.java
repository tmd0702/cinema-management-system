package Database;

import Utils.Response;

import java.util.ArrayList;

public class ShowTimeManagementProcessor extends Processor{
    public ShowTimeManagementProcessor(){
        super();
        setDefaultDatabaseTable("SHOW_TIMES");
    }
    public Response getData(int from, int quantity, String queryCondition, String sortQuery) {
        Response response = select("ID, START_TIME", from, quantity, queryCondition, sortQuery, String.format("%s", getDefaultDatabaseTable()));
        return response;
    }
}
