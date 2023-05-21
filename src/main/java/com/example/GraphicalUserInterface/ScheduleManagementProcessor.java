package com.example.GraphicalUserInterface;

import Database.Processor;
import Utils.Response;

public class ScheduleManagementProcessor extends Processor {
    public ScheduleManagementProcessor() {
        super();
        setDefaultDatabaseTable("SCHEDULES");
    }

    @Override
    public Response getData(int from, int quantity, String queryCondition, String sortQuery) {
        return select("*", from, quantity, queryCondition, sortQuery, getDefaultDatabaseTable());
    }
}
