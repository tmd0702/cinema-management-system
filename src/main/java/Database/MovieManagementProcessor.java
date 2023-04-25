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

    }
    public MovieManager getMovieManager() {
        return this.movieManager;
    }
    public void getMovies() {
        String query = "SELECT * FROM MOVIES LIMIT 30";// LIMIT 100";
        ArrayList<Movie> tmpList = new ArrayList<Movie>();
        try {
            Statement st = getConnector().createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                Movie movie = new Movie(rs.getString("ID"), rs.getString("TITLE"), rs.getString("OVERVIEW"), rs.getString("MOVIE_STATUS"), rs.getInt("DURATION"), rs.getInt("VIEW_COUNT"), rs.getDate("RELEASE_DATE"), rs.getString("POSTER_PATH"), rs.getString("BACKDROP_PATH"), rs.getString("LANGUAGE"));
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
            System.out.println("Start loading genres");
            for (Movie movie : tmpList) {
                String getGenresSQL = String.format("SELECT G.NAME FROM GENRES G JOIN MOVIE_GENRES MG WHERE MG.MOVIE_ID = %s AND MG.GENRE_ID = G.ID", movie.getId());
                ResultSet genres = st.executeQuery(getGenresSQL);
                while (genres.next()) {
//                    System.out.println(genres.getString("NAME"));
                    movie.addGenre(genres.getString("NAME"));
                }
                System.out.println(movie.getTitle() + " load genres done");
                genres.close();
            }
            this.movieManager.setMovieList(tmpList);
//            for (Movie movie : tmpList) {
//                this.movieManager.addMovie(movie);
////                if (movie.getPosterImage().getProgress() == 1 && !movie.getPosterImage().isError()) {
////                    this.movieManager.addMovie(movie);
////                } else {
////                    continue;
////                }
//            }
            rs.close();
            st.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
