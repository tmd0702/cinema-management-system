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
            int counter = 0;
            while (rs.next()) {
                Movie movie = new Movie(rs.getString("ID"), rs.getString("TITLE"), rs.getString("OVERVIEW"), rs.getString("MOVIE_STATUS"), rs.getInt("DURATION"), rs.getInt("VIEW_COUNT"), rs.getDate("RELEASE_DATE"), rs.getString("POSTER_PATH"), rs.getString("BACKDROP_PATH"));
                if (movie.getPosterImage().getProgress() == 1 && !movie.getPosterImage().isError()) {
                    movieManager.addMovie(movie);
                    counter += 1;
                } else {
                    continue;
                }
                if (counter == 30) break;

            }
            rs.close();
            st.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return movieManager;
    }
}
