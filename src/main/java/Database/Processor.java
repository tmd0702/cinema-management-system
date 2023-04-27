package Database;

import Utils.StatusCode;

import java.sql.*;
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

    public StatusCode add(String table, HashMap <String, String> columnValueMap) {
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
            return StatusCode.OK;
        } catch (Exception e) {
            System.out.println(e);
            return StatusCode.BAD_REQUEST;
        }
    }
    public StatusCode update(String table, HashMap <String, String> columnValueMap, HashMap <String, String> conditionColumnValueMap) {
        ArrayList<ArrayList<String>> columnsValuesList = Utils.Utils.getKeysValuesFromMap(columnValueMap);

        ArrayList<String> columns = columnsValuesList.get(0);
        ArrayList<String> values = columnsValuesList.get(1);



        String query = String.format("UPDATE %s SET %s WHERE %s", table);

        try {
            Statement st = getConnector().createStatement();
            st.executeQuery(query);
            st.close();
            return StatusCode.OK;
        } catch (Exception e) {
            System.out.println(e);
            return StatusCode.BAD_REQUEST;
        }

    }
    public StatusCode delete(String table, HashMap <String, String> conditionColumnValueMap) {
        String query = String.format("DELETE FROM %s WHERE %s", table);
        try {
            Statement st = getConnector().createStatement();
            st.executeQuery(query);
            st.close();
            return StatusCode.OK;
        } catch (Exception e) {
            System.out.println(e);
            return StatusCode.BAD_REQUEST;
        }
    }
    public ArrayList<ArrayList<String>> select (String table) {
        String query = String.format("SELECT * FROM %s", table);
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
}
