package Database;

import Utils.HttpStatusCode;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
public abstract class Processor {
    private Database database;
    private Connection connector;

    public Processor() {
        this.database = new Database();
        this.connector = database.getConnection();
    }
    public Database getDatabase() {
        return this.database;
    }
    public Connection getConnector() {
        return this.connector;
    }

    public HttpStatusCode add(String table, HashMap <String, String> columnValueMap) {
        ArrayList<ArrayList<String>> columnsValuesList = Utils.Utils.getKeysValuesFromMap(columnValueMap);

        ArrayList<String> columns = columnsValuesList.get(0);
        ArrayList<String> values = columnsValuesList.get(1);

        String insertColumns = String.join(", ", columns);
        String insertValues = String.join(", ", values);

        String query = String.format("INSERT INTO %s (%s) VALUES (%s)", table, insertColumns, insertValues);

        try {
            Statement st = getConnector().createStatement();
            st.executeQuery(query);
            st.close();
            return HttpStatusCode.OK;
        } catch (Exception e) {
            System.out.println(e);
            return HttpStatusCode.BAD_REQUEST;
        }
    }
    public HttpStatusCode update(String table, HashMap <String, String> columnValueMap, HashMap <String, String> conditionColumnValueMap) {
        ArrayList<ArrayList<String>> columnsValuesList = Utils.Utils.getKeysValuesFromMap(columnValueMap);

        ArrayList<String> columns = columnsValuesList.get(0);
        ArrayList<String> values = columnsValuesList.get(1);



        String query = String.format("UPDATE %s SET %s WHERE %s", table);

        try {
            Statement st = getConnector().createStatement();
            st.executeQuery(query);
            st.close();
            return HttpStatusCode.OK;
        } catch (Exception e) {
            System.out.println(e);
            return HttpStatusCode.BAD_REQUEST;
        }

    }
    public HttpStatusCode delete(String table, HashMap <String, String> conditionColumnValueMap) {
        String query = String.format("DELETE FROM %s WHERE %s", table);
        try {
            Statement st = getConnector().createStatement();
            st.executeQuery(query);
            st.close();
            return HttpStatusCode.OK;
        } catch (Exception e) {
            System.out.println(e);
            return HttpStatusCode.BAD_REQUEST;
        }
    }
}
