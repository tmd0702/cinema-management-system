package Database;
import MovieManager.*;
import Utils.*;
import com.example.GraphicalUserInterface.Main;
import javafx.scene.image.Image;

import java.sql.*;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MovieManagementProcessor extends Processor {
    private MovieManager movieManager;
    IdGenerator idGenerator;
    Processor scheduleManagementProcessor;

    public MovieManagementProcessor() throws Exception {
        super();
        setDefaultDatabaseTable("MOVIES");
        movieManager = new MovieManager();
        this.idGenerator = new IdGenerator();
        this.scheduleManagementProcessor = new ScheduleManagementProcessor();
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
    public Response getData(int from, int quantity, String queryCondition, String sortQuery) {
        Response response = select("*", from, quantity, queryCondition, sortQuery, getDefaultDatabaseTable());
        return response;
    }
    public void getMovies() {
        String query = "SELECT * FROM MOVIES LIMIT 30";// LIMIT 30";
        ArrayList<Movie> tmpList = new ArrayList<Movie>();
        try {
            Statement st = getConnector().createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                Movie movie = new Movie(rs.getString("ID"), rs.getString("TITLE"), rs.getString("OVERVIEW"), rs.getString("STATUS"), rs.getInt("DURATION"), rs.getInt("VIEW_COUNT"), rs.getDate("RELEASE_DATE"), rs.getString("POSTER_PATH"), rs.getString("BACKDROP_PATH"), rs.getString("LANGUAGE"));
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
                    movie.addGenre(genres.getString("NAME"));
                }
                System.out.println(movie.getTitle() + " load genres done");
                genres.close();
            }
            // remake the release date
            String setReleasedate = "UPDATE MOVIES SET RELEASE_DATE = DATE_FORMAT(RELEASE_DATE ,'2023-%m-%d');";
            st.executeUpdate(setReleasedate);
            System.out.println("update successfully");
            System.out.println("Start creating schedule");
            Collections.sort(tmpList, Comparator.comparingInt(Movie::getDuration));
            System.out.println(tmpList.size());
//            for(Movie movie : tmpList) {
//                scheduleMovie(movie);
//            }
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
    public boolean isMovieScheduledTooMuchTimes(Movie movie, String screenRoomId, String movieShowDateString) {
        ArrayList<ArrayList<String>> movieScheduledTimesFetcher = select("COUNT(*) AS COUNT", 0, -1, String.format("SCREEN_ROOM_ID = '%s' AND MOVIE_ID = '%s' AND SHOW_DATE = '%s'", screenRoomId, movie.getId(), movieShowDateString), "", "SCHEDULES").getData();
        int movieScheduledTimes = Integer.parseInt(Utils.getRowValueByColumnName(2, "COUNT", movieScheduledTimesFetcher));
        if (movieScheduledTimes >= Integer.parseInt(main.getConfig().get("MAXIMUM_MOVIE_SHOW_TIMES_IN_ONE_SCREEN_ROOM"))) {
            System.out.println(String.format("Movie id %s has been scheduled 3 times, ignore scheduling", movie.getId()));
            return true;
        } else {
            return false;
        }
    }
    public boolean isMovieScheduledTooSoonInScreenRoom(Movie movie, String showTime, String screenRoomId, String movieShowDateString) {
        ArrayList<ArrayList<String>> previousScheduleShowTimeInScreenRoomFetcher = select("S.SHOW_DATE, ST.START_TIME", 0, 1, String.format("S.MOVIE_ID = '%s' AND ST.ID = S.SHOW_TIME_ID AND ST.START_TIME < '%s' AND S.SCREEN_ROOM_ID = '%s' AND S.SHOW_DATE = '%s'", movie.getId(), showTime, screenRoomId, movieShowDateString), "ST.START_TIME DESC", "SCHEDULES S, SHOW_TIMES ST").getData();
        String previousScheduleShowTimeInScreenRoom = Utils.getRowValueByColumnName(2, "START_TIME", previousScheduleShowTimeInScreenRoomFetcher);
        if (previousScheduleShowTimeInScreenRoom == null) {
            return false;
        } else {
            long timeGap = LocalTime.parse(previousScheduleShowTimeInScreenRoom).until(LocalTime.parse(showTime), ChronoUnit.MINUTES);
            if (timeGap < Integer.parseInt(main.getConfig().get("MINIMUM_MINUTES_BETWEEN_SHOW_TIME_OF_SAME_MOVIE_IN_PARTICULAR_SCREEN_ROOM"))) {
                System.out.println(String.format("Movie id %s has been scheduled in this screen room (id %s) %d minutes ago, ignore scheduling", movie.getId(), screenRoomId, timeGap));
                return true;
            } else {
                System.out.println(String.format("Movie id %s has been scheduled in this screen room (id %s) %d minutes ago, screen room accept scheduling", movie.getId(), screenRoomId, timeGap));
                return false;
            }
        }
    }
    public boolean isMovieScheduledTooSoonInCinema(Movie movie, String showTime, String screenRoomId, String cinemaId, String movieShowDateString) {
        ArrayList<ArrayList<String>> previousScheduleShowTimeInCinemaFetcher = select("S.SHOW_DATE, ST.START_TIME", 0, 1, String.format("S.MOVIE_ID = '%s' AND ST.ID = S.SHOW_TIME_ID AND ST.START_TIME < '%s' AND S.SCREEN_ROOM_ID <> '%s' AND S.SCREEN_ROOM_ID IN (SELECT ID FROM SCREEN_ROOMS WHERE CINEMA_ID = '%s') AND S.SHOW_DATE = '%s'", movie.getId(), showTime, screenRoomId, cinemaId, movieShowDateString), "ST.START_TIME DESC", "SCHEDULES S, SHOW_TIMES ST").getData();
        String previousScheduleShowTimeInCinema = Utils.getRowValueByColumnName(2, "START_TIME", previousScheduleShowTimeInCinemaFetcher);
        if (previousScheduleShowTimeInCinema == null) {
            return false;
        } else {
            long timeGap = LocalTime.parse(previousScheduleShowTimeInCinema).until(LocalTime.parse(showTime), ChronoUnit.MINUTES);
            if (timeGap < Integer.parseInt(main.getConfig().get("MINIMUM_MINUTES_BETWEEN_SHOW_TIME_OF_SAME_MOVIE_IN_PARTICULAR_CINEMA"))) {
                System.out.println(String.format("Movie id %s has been scheduled in this cinema (id %s) %d minutes ago, ignore scheduling", movie.getId(), cinemaId, timeGap));
                return true;
            } else {
                System.out.println(String.format("Movie id %s has been scheduled in this cinema (id %s) %d minutes ago, cinema accept scheduling", movie.getId(), cinemaId, timeGap));
                return false;
            }
        }
    }
    public boolean isMovieScheduledInOtherScreenRooms(Movie movie, String showTime, String screenRoomId, String cinemaId, String movieShowDateString) {
        ArrayList<ArrayList<String>> currentScheduleShowTimeInOtherScreenRoomsFetcher = select("S.SHOW_DATE, ST.START_TIME", 0, 1, String.format("S.MOVIE_ID = '%s' AND ST.ID = S.SHOW_TIME_ID AND ST.START_TIME = '%s' AND S.SCREEN_ROOM_ID <> '%s' AND S.SCREEN_ROOM_ID IN (SELECT ID FROM SCREEN_ROOMS WHERE CINEMA_ID = '%s') AND S.SHOW_DATE = '%s'", movie.getId(), showTime, screenRoomId, cinemaId, movieShowDateString), "", "SCHEDULES S, SHOW_TIMES ST").getData();
        String currentScheduleShowTimeInOtherScreenRooms = Utils.getRowValueByColumnName(2, "START_TIME", currentScheduleShowTimeInOtherScreenRoomsFetcher);
        if (currentScheduleShowTimeInOtherScreenRooms == null) {
            return false;
        } else {
            System.out.println(String.format("Movie id %s has been scheduled in another screen room at %s on %s", movie.getId(), showTime, movieShowDateString));
            return true;
        }
    }
    public boolean isMovieScheduledConflict(Movie movie, String showTime, String screenRoomId, String movieShowDateString) {
        ArrayList<ArrayList<String>> nearestScheduleShowTimeFetcher = select("S.SHOW_DATE, ST.START_TIME", 0, 1, String.format("ST.ID = S.SHOW_TIME_ID AND ST.START_TIME >= '%s' AND S.SCREEN_ROOM_ID = '%s' AND S.SHOW_DATE = '%s'", showTime, screenRoomId, movieShowDateString), "ST.START_TIME ASC", "SCHEDULES S, SHOW_TIMES ST").getData();
        String nearestScheduleShowTime = Utils.getRowValueByColumnName(2, "START_TIME", nearestScheduleShowTimeFetcher);
        if (nearestScheduleShowTime == null) {
            return false;
        } else {
            long maxDurationMinutesAvailable = LocalTime.parse(showTime).until(LocalTime.parse(nearestScheduleShowTime), ChronoUnit.MINUTES);
            if (maxDurationMinutesAvailable < movie.getDuration() + Integer.parseInt(main.getConfig().get("MINIMUM_MINUTES_BETWEEN_MOVIE_PLAYS"))) {
                System.out.println(String.format("Movie id %s cannot be scheduled due to insufficient time range", movie.getId()));
                return true;
            } else {
                return false;
            }
        }
    }
    public boolean isMovieSchedulingAvailable(Movie movie, String showTime, String screenRoomId, String cinemaId, String movieShowDateString) {
        if (isMovieScheduledTooMuchTimes(movie, screenRoomId, movieShowDateString) || isMovieScheduledConflict(movie, showTime, screenRoomId, movieShowDateString) || isMovieScheduledInOtherScreenRooms(movie, showTime, screenRoomId, cinemaId, movieShowDateString) || isMovieScheduledTooSoonInScreenRoom(movie, showTime, screenRoomId, movieShowDateString) || isMovieScheduledTooSoonInCinema(movie, showTime, screenRoomId, cinemaId, movieShowDateString)){
            return false;
        } else {
            return true;
        }
    }
    public void scheduleMovie(Movie movie) throws Exception {
        if (movie.getDuration() == 0) {
            System.out.println("Error: Invalid duration");
            throw new Exception("Error: Invalid duration");
        }
        Date movieShowDate = movie.getReleaseDate();
        ArrayList<ArrayList<String>> showTimesFetcher = main.getShowTimeManagementProcessor().getData(0, -1, "", "START_TIME ASC").getData();
        ArrayList<ArrayList<String>> cinemasFetcher = main.getTheaterManagementProcessor().getData(0, -1, "", "").getData();
        for(int i = 2; i < cinemasFetcher.size();++i) {
            String cinemaId = Utils.getRowValueByColumnName(i, "ID", cinemasFetcher);
            ArrayList<ArrayList<String>> screenRoomsFetcher = main.getScreenRoomManagementProcessor().getData(0, -1, String.format("CINEMA_ID = '%s'", Utils.getRowValueByColumnName(i, "ID", cinemasFetcher )), "").getData();
            for (int j = 2; j < screenRoomsFetcher.size(); ++ j) {
                String screenRoomId = Utils.getRowValueByColumnName(j, "ID", screenRoomsFetcher);
                System.out.print(String.format("\nScheduling movie %s on cinema %s, screen room %s, release date ", movie.getId(), cinemaId, screenRoomId));
                for (int nDay = 0; nDay < Integer.parseInt(main.getConfig().get("MOVIE_SHOW_DURATION_DAYS_FROM_RELEASE_DATE")); ++nDay) {
                    String movieShowDateString = Utils.addDateByNDays(movieShowDate, nDay);
                    System.out.println(movieShowDateString);
                    for (int k = 2; k < showTimesFetcher.size(); ++k) {
                        String showTime = Utils.getRowValueByColumnName(k, "START_TIME", showTimesFetcher);
                        String showTimeId = Utils.getRowValueByColumnName(k, "ID", showTimesFetcher);
                        if (isMovieSchedulingAvailable(movie, showTime, screenRoomId, cinemaId, movieShowDateString)) {
                            addFakeSchedules(showTimeId, movie.getId(), screenRoomId, movieShowDateString);
                        }
                    }
                }
            }
        }
    }
    public void addFakeSchedules(String showtimeID,String movieId, String screenRoomId, String showDate) throws Exception{
        HashMap<String, String> schedule = new HashMap<String, String>();
        schedule.put("ID", idGenerator.generateId(scheduleManagementProcessor.getDefaultDatabaseTable()));
        schedule.put("SHOW_TIME_ID", showtimeID);
        schedule.put("MOVIE_ID", movieId);
        schedule.put("SCREEN_ROOM_ID", screenRoomId);
        schedule.put("SHOW_DATE", showDate);
        Response response = scheduleManagementProcessor.add(schedule);
        if (response.getStatusCode() == StatusCode.OK) {
            System.out.println(String.format("Scheduled movie id %s success on showtime id %s with id %s", movieId, showtimeID, schedule.get("ID")));
        } else {
            System.out.println(response.getMessage());
        }
    }
    public String getPreviousID(String query, String id)
    {
        String previousID = "";
        try {
            Statement st = getConnector().createStatement();
            ResultSet rs = st.executeQuery(query);
            while(rs.next()){
                if(rs.getString("ID") == id)
                    rs.previous();
                previousID = rs.getString("ID");
                break;
            }

        }catch(Exception e){
            System.out.println(e);
        }
        return previousID;

    }
    public String getOneColumnData(String query, String id1, String id2, String column){
        String data = "";
        query = String.format(query, id1, id2);
        try{
            Statement st = getConnector().createStatement();
            ResultSet rs = st.executeQuery(query);
            while(rs.next()){
                data = rs.getString(column);
            }
        }catch (Exception e) {
            System.out.println(e);
        }
        return data;
    }
    public ArrayList<String> createList(String table, String condition){
        String query = String.format("SELECT * FROM %s %s;", table, condition);
        ArrayList<String> data = new ArrayList<String>();
        try{
            Statement st = getConnector().createStatement();
            ResultSet rs = st.executeQuery(query);
            while(rs.next()){
                data.add(rs.getString("ID"));
            }
        }catch (Exception e) {
            System.out.println(e);
        }
        return data;
    }
    public void setOnCurrentShowingStatus(){
        System.out.println(movieManager.getCurrentlyPlayingMovieList());
    }

}
