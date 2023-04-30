package Database;
import java.sql.*;

import com.example.GraphicalUserInterface.BookingController;
import BookingManager.BookingInfor;
public class BookingProcessor extends Processor{
    private BookingInfor bookingInfor;
    public BookingProcessor(){
        super();
        this.bookingInfor = new BookingInfor();
    }
    public BookingInfor getBookingInfor(){
        return this.bookingInfor;
    }
    public void getScreen() {
        String query = String.format("SELECT * FROM SCHEDULE WHERE MOVIE_ID = %s AND THEATER_ID = %s AND TIME = %s;",bookingInfor.getIdMovie(), bookingInfor.getNameCinema(),bookingInfor.getTime());
        try {
            Statement stmt = getConnector().createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                String sCreen = rs.getString("SR_NAME");
                bookingInfor.setScreen(sCreen);


            }
        } catch (SQLException sqle) {
            System.out.println(sqle);

        }

    }
}
