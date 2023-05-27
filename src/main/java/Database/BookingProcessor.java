package Database;
import java.sql.*;
import java.util.ArrayList;

import Utils.Response;
import com.example.GraphicalUserInterface.BookingController;
import BookingManager.BookingInfor;
public class BookingProcessor extends Processor{
    private BookingInfor bookingInfor;
    public BookingProcessor(){
        super();
        this.bookingInfor = new BookingInfor();
    }
    public Response getData(int from, int quantity, String queryCondition, String sortQuery) {
        Response response = select("*", from, quantity, queryCondition, sortQuery, getDefaultDatabaseTable());
        return response;
    }
    public BookingInfor getBookingInfor(){
        return this.bookingInfor;
    }
    public String getScreen() {
        String query = String.format("SELECT SR.* FROM SCREEN_ROOMS SR, SCHEDULES SCH WHERE SCH.SCREEN_ROOM_ID = SR.ID AND SCH.MOVIE_ID = \"%s\" AND SCH.SHOW_DATE = \"%s\" AND SCH.SHOW_TIME_ID = \"%s\" AND CINEMA_ID = \"%s\";", bookingInfor.getIdMovie(), bookingInfor.getDate(),bookingInfor.getTime(),bookingInfor.getNameCinema());
        String nameScreen = null;
        try {
            Statement stmt = getConnector().createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                 nameScreen = rs.getString("NAME");
                bookingInfor.setScreen(rs.getString("ID"));

            }
        } catch (SQLException sqle) {
            System.out.println(sqle);
        }
        return nameScreen;
    }
    public void getSeat(){
        ArrayList<String> seatlist = new ArrayList<String>();
        String query = String.format("SELECT * FROM SEATS S JOIN SCREEN_ROOMS SR ON S.SCREEN_ROOM_ID = SR.ID WHERE SCREEN_ROOM_ID = \"%s\"  AND CINEMA_ID = \"%s\" ;", bookingInfor.getScreen(), bookingInfor.getNameCinema());
        try {
            Statement stmt = getConnector().createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String seatName = rs.getString("NAME");
                seatlist.add(seatName);
                bookingInfor.setSeats(seatlist);
            }
        } catch (SQLException sqle) {
            System.out.println(sqle);
        }
    }
    public ArrayList<ArrayList<String>> getTimeSlotList(){
        ArrayList<ArrayList<String>> timeSlotList = new ArrayList<ArrayList<String>>();
        String query = "SELECT DISTINCT(DATE_FORMAT(START_TIME, \'%h:%i\'))" + String.format(" AS TIME_SLOT, ST.ID FROM SCHEDULES SCH, SHOW_TIMES ST, SCREEN_ROOMS SR WHERE SCH.SHOW_TIME_ID = ST.ID AND SCH.SCREEN_ROOM_ID = SR.ID AND SCH.MOVIE_ID = \"%s\" AND SCH.SHOW_DATE = \"%s\" AND SR.CINEMA_ID = \"%s\" ORDER BY TIME_SLOT ASC;", bookingInfor.getIdMovie(), bookingInfor.getDate(), bookingInfor.getNameCinema());
        try{
            Statement stmt = getConnector().createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()){
                ArrayList<String> timeSlot = new ArrayList<String>();
                String id = rs.getString("ID");
                String startTimeSlot = rs.getString("TIME_SLOT");
                timeSlot.add(id);
                timeSlot.add(startTimeSlot);
                timeSlotList.add(timeSlot);

            }
        }catch (SQLException sqle) {
            System.out.println(sqle);
        }
        return timeSlotList;
    }
    public ArrayList<ArrayList<String>> getTheaterList(){
        ArrayList<ArrayList<String>> theaterlist = new ArrayList<ArrayList<String>>();
        System.out.println(bookingInfor.getDate());
        String query = String.format("SELECT DISTINCT(CIN.NAME), CIN.ID FROM SCHEDULES SCH, SCREEN_ROOMS SR, CINEMAS CIN WHERE SCH.SCREEN_ROOM_ID = SR.ID AND SR.CINEMA_ID = CIN.ID AND MOVIE_ID = \"%s\" AND SCH.SHOW_DATE = \"%s\";", bookingInfor.getIdMovie(), bookingInfor.getDate());
        try{
            Statement stmt = getConnector().createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()){
                ArrayList<String> theaterInfor = new ArrayList<String>();
                String idTheater = rs.getString("ID");
                String nameTheater = rs.getString("NAME");
                theaterInfor.add(idTheater);
                theaterInfor.add(nameTheater);
                theaterlist.add(theaterInfor);
            }

        }catch (SQLException sqle) {
            System.out.println(sqle);
        }
        return theaterlist;
    }
}
