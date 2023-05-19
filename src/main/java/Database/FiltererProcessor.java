package Database;

import Utils.Response;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class FiltererProcessor extends Processor {
    public FiltererProcessor() {
        super();
    }
    public ArrayList<String> getGenres() {
        String query = "SELECT NAME FROM GENRES;";
        ArrayList<String> genres = new ArrayList<String>();
        genres.add("All genres");
        try {
            Statement st = getConnector().createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                genres.add(rs.getString("NAME"));
            }
        } catch (SQLException sqle) {
            System.out.println(sqle);
        }
        return genres;
    }
    public Response getData(int from, int quantity, String queryCondition, String sortQuery) {
        Response response = select("*", from, quantity, queryCondition, sortQuery, getDefaultDatabaseTable());
        return response;
    }
    public ArrayList<String> getLanguages() {
        String query = "SELECT DISTINCT LANGUAGE FROM MOVIES;";
        ArrayList<String> languages = new ArrayList<String>();
        languages.add("All languages");
        try {
            Statement st = getConnector().createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                languages.add(rs.getString("LANGUAGE"));
            }
        } catch (SQLException sqle) {
            System.out.println(sqle);
        }
        return languages;
    }
}
