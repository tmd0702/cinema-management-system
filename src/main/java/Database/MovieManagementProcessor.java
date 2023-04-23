package Database;
import MovieManager.*;
import javafx.scene.image.Image;

import java.sql.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class MovieManagementProcessor extends Processor {
    private MovieManager movieManager;
    public MovieManagementProcessor() {
        super();
        movieManager = new MovieManager();
        getMovies();
    }
    public MovieManager getMovieManager() {
        return this.movieManager;
    }
    public void getMovies() {
        String query = "SELECT * FROM MOVIES LIMIT 30";
        ArrayList<Movie> tmpList = new ArrayList<Movie>();
        try {
            Statement st = getConnector().createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                Movie movie = new Movie(rs.getString("ID"), rs.getString("TITLE"), rs.getString("OVERVIEW"), rs.getString("MOVIE_STATUS"), rs.getInt("DURATION"), rs.getInt("VIEW_COUNT"), rs.getDate("RELEASE_DATE"), rs.getString("POSTER_PATH"), rs.getString("BACKDROP_PATH"));
                tmpList.add(movie);
            }
            ExecutorService service = Executors.newCachedThreadPool();
            for (Movie movie : tmpList) {
                service.execute(() -> {
                    movie.setBackdropImage(new Image(movie.getBackdropPath()));
                    movie.setPosterImage(new Image(movie.getPosterPath()));
                });

            }
            service.shutdown();
            try {
                service.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            } catch (InterruptedException e) {
                System.out.println("Interrupted: " + e);
            }
            System.out.println("Done");
            for (Movie movie : tmpList) {
                if (movie.getPosterImage().getProgress() == 1 && !movie.getPosterImage().isError()) {
                    this.movieManager.addMovie(movie);
                } else {
                    continue;
                }
            }
            rs.close();
            st.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
