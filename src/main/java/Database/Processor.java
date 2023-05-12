package Database;

import Utils.ColumnType;
import Utils.Response;
import Utils.StatusCode;

import java.net.ResponseCache;
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
    public Response add(HashMap <String, String> columnValueMap) {
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
            return new Response("OK", StatusCode.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new Response(e.getMessage(), StatusCode.BAD_REQUEST);
        }
    }
    public String constructUpdateSQLSetStatement(ArrayList<String> columns, ArrayList<String> values) {
        String setStatement = "";
        for (int i = 0 ; i < columns.size(); ++i) {
            setStatement += String.format("%s = '%s'", columns.get(i), values.get(i));
            if (i < columns.size() - 1) {
                setStatement += ", ";
            }
        }
        return setStatement;
    }
    public Response update(HashMap <String, String> columnValueMap, String queryCondition) {
        ArrayList<ArrayList<String>> columnsValuesList = Utils.Utils.getKeysValuesFromMap(columnValueMap);

        ArrayList<String> columns = columnsValuesList.get(0);
        ArrayList<String> values = columnsValuesList.get(1);



        String query = String.format("UPDATE %s SET %s WHERE %s", defaultDatabaseTable, constructUpdateSQLSetStatement(columns, values), queryCondition);

        try {
            Statement st = getConnector().createStatement();
            st.execute(query);
            st.close();
            return new Response("OK", StatusCode.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new Response(e.getMessage(), StatusCode.BAD_REQUEST);
        }

    }
    public Response delete(String queryCondition) {
        String query = String.format("DELETE FROM %s WHERE %s", defaultDatabaseTable, queryCondition);
        try {
            Statement st = getConnector().createStatement();
            st.execute(query);
            st.close();
            return new Response("OK", StatusCode.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new Response(e.getMessage(), StatusCode.BAD_REQUEST);
        }
    }
    public Response select (int from, int quantity, String queryCondition, String sortQuery) {
        String query = String.format("SELECT * FROM %s", defaultDatabaseTable);
        if (queryCondition.length() > 0) {
            query = query + " WHERE " + queryCondition;
        }
        if (sortQuery.length() > 0) {
            query = query + " ORDER BY " + sortQuery;
        }
        if (quantity > -1) {
            query = query + String.format(" LIMIT %d, %d", from, quantity);
        }

        ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();

        try {
            System.out.println(query);
            Statement st = getConnector().createStatement();
            ResultSet rs = st.executeQuery(query);
            ResultSetMetaData rsmd = rs.getMetaData();
            ArrayList<String> columnNames = new ArrayList<String>();
            ArrayList<String> columnTypes = new ArrayList<String>();
            for (int i=1; i <= rsmd.getColumnCount(); ++i) {
                columnNames.add(rsmd.getColumnName(i));
                columnTypes.add(ColumnType.getByValue(rsmd.getColumnType(i)).getDescription());
            }
            result.add(columnNames);
            result.add(columnTypes);
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
            return new Response(e.getMessage(), StatusCode.BAD_REQUEST);
        }
        return new Response("OK", StatusCode.OK, result);
    }
    public int count(String queryCondition) {
        String query = String.format("SELECT COUNT(*) FROM %s", defaultDatabaseTable);
        if (queryCondition.length() > 0) {
            query = query + " WHERE " + queryCondition;
        }
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
