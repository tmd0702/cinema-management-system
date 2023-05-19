package Database;

import Utils.Response;

public class ScheduleManagementProcessor extends Processor{
    public ScheduleManagementProcessor(){
        super();
        setDefaultDatabaseTable("SCHEDULES");
    }
    public Response getData(int from, int quantity, String queryCondition, String sortQuery) {
        Response response = select("*", from, quantity, queryCondition, sortQuery, getDefaultDatabaseTable());
        return response;
    }
}
