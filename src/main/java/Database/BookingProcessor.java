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
}
