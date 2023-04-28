package Database;

import Utils.StatusCode;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
public abstract class Processor {
    private Database database;
    private Connection connector;
    private String defaultDatabaseTable;

    public Processor() {
        this.database = new Database();
        this.connector = database.getConnection();
        this.defaultDatabaseTable = "";
    }
    public Database getDatabase() {
        return this.database;
    }
    public Connection getConnector() {
        return this.connector;
    }

    public void setDefaultDatabaseTable(String defaultDatabaseTable) {
        this.defaultDatabaseTable = defaultDatabaseTable;
    }
    public String getDefaultDatabaseTable() {
        return this.defaultDatabaseTable;
    }
    public StatusCode add(HashMap <String, String> columnValueMap) {
        ArrayList<ArrayList<String>> columnsValuesList = Utils.Utils.getKeysValuesFromMap(columnValueMap);

        ArrayList<String> columns = columnsValuesList.get(0);
        ArrayList<String> values = columnsValuesList.get(1);

        String insertColumns = String.join(", ", columns);
        String insertValues = "'" + String.join("', '", values) + "'";

        String query = String.format("INSERT INTO %s (%s) VALUES (%s)", defaultDatabaseTable, insertColumns, insertValues);
        System.out.println(query);
        try {
            Statement st = getConnector().createStatement();
            st.execute(query);
            st.close();
            return StatusCode.OK;
        } catch (Exception e) {
            System.out.println(e);
            return StatusCode.BAD_REQUEST;
        }
    }
    public StatusCode update(HashMap <String, String> columnValueMap, HashMap <String, String> conditionColumnValueMap) {
        ArrayList<ArrayList<String>> columnsValuesList = Utils.Utils.getKeysValuesFromMap(columnValueMap);

        ArrayList<String> columns = columnsValuesList.get(0);
        ArrayList<String> values = columnsValuesList.get(1);



        String query = String.format("UPDATE %s SET %s WHERE %s", defaultDatabaseTable);

        try {
            Statement st = getConnector().createStatement();
            st.execute(query);
            st.close();
            return StatusCode.OK;
        } catch (Exception e) {
            System.out.println(e);
            return StatusCode.BAD_REQUEST;
        }

    }
    public StatusCode delete(String defaultDatabaseTable, HashMap <String, String> conditionColumnValueMap) {
        String query = String.format("DELETE FROM %s WHERE %s", defaultDatabaseTable);
        try {
            Statement st = getConnector().createStatement();
            st.execute(query);
            st.close();
            return StatusCode.OK;
        } catch (Exception e) {
            System.out.println(e);
            return StatusCode.BAD_REQUEST;
        }
    }
    public ArrayList<ArrayList<String>> select (int from, int quantity) {
        String query = String.format("SELECT * FROM %s", defaultDatabaseTable);
        if (quantity > -1) {
            query = query + String.format(" LIMIT %d, %d", from, quantity);
        }
        ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();

        try {
            Statement st = getConnector().createStatement();
            ResultSet rs = st.executeQuery(query);
            ResultSetMetaData rsmd = rs.getMetaData();
            ArrayList<String> columnNames = new ArrayList<String>();
            for (int i=1; i <= rsmd.getColumnCount(); ++i) {
                System.out.println(rsmd.getColumnName(i));
                columnNames.add(rsmd.getColumnName(i));
            }
            result.add(columnNames);
            while (rs.next()) {
                ArrayList<String> val = new ArrayList<String>();
                for (String columnName : columnNames) {
                    val.add(rs.getString(columnName));
                }
                result.add(val);
            }
            st.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return result;
    }
    public int count() {
        String query = String.format("SELECT COUNT(*) FROM %s", defaultDatabaseTable);
        int count = 0;
        try {
            Statement st = getConnector().createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                count = rs.getInt(1);
            }
            st.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return count;
    }
}
