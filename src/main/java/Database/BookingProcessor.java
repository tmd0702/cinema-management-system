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
    public void getScreen() {
        String query = String.format("SELECT * FROM SCREEN_ROOMS");
        try {
            Statement stmt = getConnector().createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                String sCreen = rs.getString("NAME");
                bookingInfor.setScreen(sCreen);

            }
        } catch (SQLException sqle) {
            System.out.println(sqle);
        }

    }
    public void getSeat(){
        ArrayList<String> seatlist = new ArrayList<String>();
        String query = String.format("SELECT * FROM SEATS S JOIN SCREEN_ROOMS SR ON S.SCREEN_ROOM_ID = SR.ID WHERE SCREEN_ROOM_ID = \"SR_00001\"  AND CINEMA_ID = \"CIN_00001\" ;");
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
    public ArrayList<String> getTimeSlotList(){
        ArrayList<String> timeSlotList = new ArrayList<String>();
        String query =("SELECT DISTINCT(DATE_FORMAT(START_TIME, \'%h:%i\')) AS TIME_SLOT FROM SCHEDULES SCH, SHOW_TIMES ST WHERE SCH.SHOW_TIME_ID = ST.ID AND SCH.MOVIE_ID = \"100450\" AND SCH.SHOW_DATE = \"2023-11-01\" ORDER BY TIME_SLOT ASC;");
        try{
            Statement stmt = getConnector().createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()){
                String startTimeSlot = rs.getString("TIME_SLOT");
                timeSlotList.add(startTimeSlot);

            }
        }catch (SQLException sqle) {
            System.out.println(sqle);
        }
        return timeSlotList;
    }
    public ArrayList<String> getTheaterList(){
        ArrayList<String> theaterlist = new ArrayList<String>();
        String query = String.format("SELECT DISTINCT(CIN.NAME) FROM SCHEDULES SCH, SCREEN_ROOMS SR, CINEMAS CIN WHERE SCH.SCREEN_ROOM_ID = SR.ID AND SR.CINEMA_ID = CIN.ID AND MOVIE_ID = \"100450\" AND SCH.SHOW_DATE = \"2023-11-01\";");
        try{
            Statement stmt = getConnector().createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()){
                String theater = rs.getString("NAME");
                theaterlist.add(theater);
            }
        }catch (SQLException sqle) {
            System.out.println(sqle);
        }
        return theaterlist;
    }
}
