package Database;
import MovieManager.*;
import Utils.StatusCode;

import java.util.*;
import java.sql.*;
public class MovieManagementProcessor extends Processor {
    public MovieManagementProcessor() {

    }
    public MovieManager getMovies() {
        String query = "SELECT * FROM MOVIES";
        MovieManager movieManager = new MovieManager();
        try {
            Statement st = getConnector().createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                Movie movie = new Movie(rs.getString("ID"), rs.getString("TITLE"), rs.getString("CONTENT"), rs.getString("CATEGORY"), rs.getString("MOVIE_STATUS"), rs.getInt("DURATION"), rs.getInt("VIEW_COUNT"), rs.getDate("PRODUCT_DATE"));
                movieManager.addMovie(movie);
            }
            rs.close();
            st.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return movieManager;
    }
}
