package Utils;
import Database.Database;
import java.sql.*;
import java.util.*;

public class IdGenerator {
    private int sequence;
    private HashMap<String, String> knowledgeIdFormatMap;
    private Database Database;
    private Connection con;
    public IdGenerator() {
        this.sequence = -1;
        this.Database = new Database();
        this.Database.connect();
        this.con = this.Database.getConnection();

    }
    public String generateId() {
        String id = "";
        if (this.sequence > -1) {
            //
        } else {

        }
        return id;
    }

}
