package Utils;
import Database.Database;

import java.sql.*;
import java.util.*;


public class IdGenerator {
    private int sequence;
    private HashMap<String, String> dataIdFormatMap;
    private Database database;
    private Connection con;
    public IdGenerator() {
        this.sequence = -1;
        this.database = new Database();
        this.con = this.database.getConnection();
        this.dataIdFormatMap = new HashMap<String, String>();
    }
    public IdGenerator(int sequence, Database database, HashMap<String, String> dataIdFormatMap) {
        this.sequence = sequence;
        this.database = database;
        this.con = this.database.getConnection();
        this.dataIdFormatMap = dataIdFormatMap;

    }
    public String generateId(String table) {
        String id = "";
        if (this.sequence > -1) {
            String query = String.format("SELECT COUNT(ID) FROM %s", table);
            try {
                Statement st = this.con.createStatement();
                ResultSet rs = st.executeQuery(query);
                this.sequence = rs.getInt("total");
                st.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        id = this.dataIdFormatMap.get(table) + ++this.sequence;
        return id;
    }

}
