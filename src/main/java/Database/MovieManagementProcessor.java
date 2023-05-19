package Database;
import MovieManager.*;
import Utils.*;
import javafx.scene.image.Image;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import Utils.*;

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
    @Override
    public Response select (String values, int from, int quantity, String queryCondition, String sortQuery, String table) {
        String query = String.format("SELECT %s FROM %s", values, table);
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

    public void scheduleMovie(Movie movie) throws Exception {
        System.out.println(movie.getTitle());
        ArrayList<String> cinemas = createList("CINEMAS", " ");
        HashMap<String, String> schedule = new HashMap<String, String>();
        for(int day = 0; day < 7; day++){
            for(String cinema : cinemas){
                ArrayList<String> screen_rooms = createList("SCREEN_ROOMS", String.format("WHERE CINEMA_ID = \"%s\"",cinema));
                for(String room : screen_rooms){
                    ArrayList<String> show_times = createList("SHOW_TIMES", String.format("WHERE SCREEN_ROOM_ID = \"%s\"", room));
                    System.out.println("room1: " + room);
                    int count = 0; //Số lần chiếu phim trong phòng chiếu hiện tại
                    for(String showtime : show_times){
                        System.out.println(showtime);
                        if(count >= 3){ // 3: là số lần phim có thể chiếu trong một phòng
                            break;
                        }
                        int indextime = show_times.indexOf(showtime);
                        if(indextime == 0) {
                            addFakeSchedules(schedule, showtime, movie.getId());
                            count++;
                            continue;
                        }
                        // Kiểm tra xem phòng chiếu khác trong cùng rạp có chiếu phim khác trong khoảng thời gian tương tự
                        boolean hasConflict = false;
                        for (String otherRoom : screen_rooms) {
                            if (otherRoom == room) {
                                continue; // Bỏ qua phòng chiếu hiện tại
                            }
                            String getShowtime = String.format("SELECT ST.* FROM (MOVIES M JOIN SCHEDULES SCH ON M.ID = SCH.MOVIE_ID) JOIN SHOW_TIMES ST ON SCH.SHOW_TIME_ID = ST.ID WHERE ST.ID = \"%s\" AND M.ID = \"%s\" AND ST.SCREEN_ROOM_ID = \"%s\";", showtime, movie.getId(), otherRoom);
                            String scheduledFilm = null;
                            try {
                                Statement st = getConnector().createStatement();
                                ResultSet Showtime = st.executeQuery(getShowtime);

                                while (Showtime.next()) {
                                    scheduledFilm = Showtime.getString("ID");
                                }
                            } catch (Exception e) {
                                System.out.println(e);
                            }
                            if (scheduledFilm != null && scheduledFilm == showtime) {
                                hasConflict = true;
                                break;
                            }
                        }
                        if (hasConflict) {
                            System.out.println("has conflict");
                            continue;
                        }
                        System.out.println("hasn't conflict");
                            // Kiểm tra xem có suất chiếu trước đó trong cùng phòng chiếu trong khoảng thời gian tối thiểu
                            if (indextime > 0) {
                                String queryPreviousId = String.format("SELECT ST.* FROM (MOVIES M JOIN SCHEDULES SCH ON M.ID = SCH.MOVIE_ID) JOIN SHOW_TIMES ST ON SCH.SHOW_TIME_ID = ST.ID WHERE M.ID = \"%s\" AND ST.SCREEN_ROOM_ID = \"%s\";", movie.getId(), room);
                                String previousShowTime = getPreviousID(queryPreviousId, showtime);

                                String time = getOneColumnData("SELECT CAST(ABS(time_to_sec((SUBTIME(ST1.START_TIME, ST2.START_TIME)))/60) AS UNSIGNED) AS SUBTIMES FROM SHOW_TIMES ST1, SHOW_TIMES ST2 WHERE ST1.ID = \"%s\" AND ST2.ID = \"%s\";", showtime, previousShowTime, "SUBTIMES");
                                if(time == null)
                                    time = "0";
                                if(Integer.parseInt(time) < 300) {
                                    continue;
                                }
                                System.out.println("ok1");
                            }
                            // Kiểm tra xem có suất chiếu trước đó trong cùng rạp trong khoảng thời gian tối thiểu
                            int indexroom = screen_rooms.indexOf(room);
                            if (indexroom > 0) {
                                String queryPreviousId = String.format("SELECT ST.* FROM ((MOVIES M JOIN SCHEDULES SCH ON M.ID = SCH.MOVIE_ID) JOIN SHOW_TIMES ST ON SCH.SHOW_TIME_ID = ST.ID) JOIN SCREEN_ROOMS SR ON ST.SCREEN_ROOM_ID = SR.ID WHERE M.ID = \"%s\" AND SR.ID = \"%s\";", movie.getId(), cinema);
                                String previousRoom = getPreviousID(queryPreviousId, room);
                                String time = getOneColumnData("SELECT CAST(ABS(time_to_sec((SUBTIME(ST1.START_TIME, ST2.START_TIME)))/60) AS UNSIGNED) AS SUBTIMES FROM SHOW_TIMES ST1, SHOW_TIMES ST2 WHERE ST1.ID = \"%s\" AND ST2.ID = \"%s\";", showtime, previousRoom, "SUBTIMES");
                                if(time == "")
                                    time = "170";
                                if(Integer.parseInt(time) < 170)
                                    continue;
                                System.out.println("ok2");
                            }
                            addFakeSchedules(schedule, showtime, movie.getId());
                            count++;
                    }
                }
            }
        }
    }
    public void addFakeSchedules(HashMap<String, String> schedule, String showtimeID,String movieId) throws Exception{
        schedule.put("ID", idGenerator.generateId(scheduleManagementProcessor.getDefaultDatabaseTable()));
        schedule.put("SHOW_TIME_ID", showtimeID);
        schedule.put("MOVIE_ID", movieId);
        Response response = scheduleManagementProcessor.add(schedule);
        if (response.getStatusCode() == StatusCode.OK) {
            System.out.println("insert 1 row success " + schedule.get("ID"));
        } else {
            System.out.println(" failed");
        }
        System.out.println(schedule);
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
