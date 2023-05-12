package Database;
import MovieManager.*;
import Utils.ColumnType;
import Utils.Response;
import Utils.StatusCode;
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
        setDefaultDatabaseTable("MOVIES");
        movieManager = new MovieManager();

    }
    public MovieManager getMovieManager() {
        return this.movieManager;
    }
    public String getMovieGenres(String queryCondition) {
        String movieGenres = "";
        String query =String.format("SELECT NAME FROM GENRES G JOIN MOVIE_GENRES MG ON G.ID = MG.GENRE_ID WHERE %s", queryCondition);
        try {
            Statement st = getConnector().createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                if (movieGenres == "") {
                    movieGenres += rs.getString("NAME");
                } else {
                    movieGenres += ", " + rs.getString("NAME");
                }
            }
            st.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return movieGenres;
    }
    @Override
    public Response select (int from, int quantity, String queryCondition, String sortQuery) {
        String query = String.format("SELECT * FROM %s", getDefaultDatabaseTable());
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
            Statement st = getConnector().createStatement();
            ResultSet rs = st.executeQuery(query);
            System.out.println("okkkkk");
            System.out.println(rs);
            ResultSetMetaData rsmd = rs.getMetaData();
            ArrayList<String> columnNames = new ArrayList<String>();
            ArrayList<String> columnTypes = new ArrayList<String>();
            for (int i=1; i <= rsmd.getColumnCount(); ++i) {
                columnNames.add(rsmd.getColumnName(i));
                columnTypes.add(ColumnType.getByValue(rsmd.getColumnType(i)).getDescription());
            }
//            columnNames.add("GENRES");
//            columnTypes.add("Varchar");
            result.add(columnNames);
            result.add(columnTypes);
            while (rs.next()) {
                ArrayList<String> val = new ArrayList<String>();
                for (String columnName : columnNames) {
                    val.add(rs.getString(columnName));
                }
//                val.add(getMovieGenres("MOVIE_ID = '" + val.get(0) + "';"));
                result.add(val);
            }
            st.close();
            return new Response("OK", StatusCode.OK, result);

        } catch (Exception e) {
            System.out.println(e);
            return new Response(e.getMessage(), StatusCode.BAD_REQUEST);
        }
    }
    public void getMovies() {
        String query = "SELECT * FROM MOVIES LIMIT 30";// LIMIT 30";
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
