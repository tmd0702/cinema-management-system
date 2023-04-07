package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class Database {
    private Connection con;
    private String databaseUsername, databasePassword, databaseDns;

    public Database() {
        this.databaseUsername = "4hb_admin";
        this.databasePassword = "sa123456";
        this.databaseDns = "jdbc:mysql://127.0.0.1:3306/";
        this.connect();
    }
    public void setDatabaseUsername(String databaseUsername) {
        this.databaseUsername = databaseUsername;
    }
    public void setDatabasePassword(String databasePassword) {
        this.databasePassword = databasePassword;
    }
    public void setDatabaseDns(String databaseDns) {
        this.databaseDns = databaseDns;
    }
    public String getDatabaseUsername() {
        return this.databaseUsername;
    }
    public String getDatabasePassword() {
        return this.databasePassword;
    }
    public String getDatabaseDns() {
        return this.databaseDns;
    }
    public Database(String databaseUsername, String databasePassword, String databaseDns) {
        this.databaseUsername = databaseUsername;
        this.databasePassword = databasePassword;
        this.databaseDns = databaseDns;
        this.connect();
    }
    public void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver Loaded Successfully");
            this.con = DriverManager.getConnection(this.databaseDns, this.databaseUsername, this.databasePassword); // not the actual password
            System.out.println("Successful Connection");
        } catch (ClassNotFoundException cnfe) {
            System.err.println(cnfe);
        } catch (SQLException sqle) {
            System.err.println(sqle);
        }
    }
    public Connection getConnection() {
        return this.con;
    }
}
