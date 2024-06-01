package Database;

import Utils.ColumnType;
import Utils.Response;
import Utils.StatusCode;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

public class RatingProcessor extends Processor {
    public RatingProcessor() {
        super();
        setDefaultDatabaseTable("REVIEW");
    }
    public Response getCommunities() {
        String query = String.format("SELECT GROUP_CONCAT(USER_ID) AS USERS FROM %s GROUP BY MOVIE_ID", "REVIEW");

        ArrayList<ArrayList<String>> result = new ArrayList<>();

        try {
            Statement st = getConnector().createStatement();
            ResultSet rs = st.executeQuery(query);
            ResultSetMetaData rsmd = rs.getMetaData();
            ArrayList<String> columnNames = new ArrayList<>();
            ArrayList<String> columnTypes = new ArrayList<>();
            for (int i = 1; i <= rsmd.getColumnCount(); ++i) {
                columnNames.add(rsmd.getColumnLabel(i));
                columnTypes.add(ColumnType.getByValue(rsmd.getColumnType(i)).getDescription());
            }
            result.add(columnNames);
            result.add(columnTypes);
            while (rs.next()) {
                ArrayList<String> val = new ArrayList<>();
                val.add(rs.getString("USERS"));
                result.add(val);
            }
            st.close();
        } catch (Exception e) {
            System.out.println(e);
            return new Response(e.toString(), StatusCode.BAD_REQUEST);
        }
        return new Response("OK", StatusCode.OK, result);
    }
    public Response select (String values, int from, int quantity, String queryCondition, String sortQuery, String table) {
        String query = String.format("SELECT %s FROM %s", values, table);
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
                columnNames.add(rsmd.getColumnLabel(i));
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
            return new Response(e.toString(), StatusCode.BAD_REQUEST);
        }
        return new Response("OK", StatusCode.OK, result);
    }
    public Response getData(int from, int quantity, String queryCondition, String sortQuery) {
        Response response = select("USER_ID, MOVIE_ID, RATING", from, quantity, queryCondition, sortQuery, getDefaultDatabaseTable());
        return response;
    }
    public int countData(String queryCondition) {
        return count(queryCondition, getDefaultDatabaseTable());
    }
    public Response updateData(HashMap<String, String> columnValueMap, String queryCondition, boolean isCommit) {
        return update(columnValueMap, queryCondition, getDefaultDatabaseTable(), isCommit);
    }
    public Response insertData(HashMap<String, String> columnValueMap, boolean isCommit) {
        return insert(columnValueMap, getDefaultDatabaseTable(), isCommit);
    }
    public Response deleteData(String queryCondition, boolean isCommit) {
        return delete(queryCondition, getDefaultDatabaseTable(), isCommit);
    }
}
