package Database;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

import Utils.Response;
import Utils.*;
import com.example.GraphicalUserInterface.BookingController;
import BookingManager.BookingInfor;
import UserManager.User;
import com.example.GraphicalUserInterface.Main;

public class BookingProcessor extends Processor {
    private BookingInfor bookingInfor;
    private IdGenerator idGenerator;
    public BookingProcessor() throws Exception {
        super();
        this.bookingInfor = new BookingInfor();
        this.idGenerator = new IdGenerator();
    }

    public Response getData(int from, int quantity, String queryCondition, String sortQuery) {
        Response response = select("*", from, quantity, queryCondition, sortQuery, getDefaultDatabaseTable());
        return response;
    }

    public BookingInfor getBookingInfor() {
        return this.bookingInfor;
    }

    public String getScreen() {
        String query = String.format("SELECT SR.* FROM SCREEN_ROOMS SR, SCHEDULES SCH WHERE SCH.SCREEN_ROOM_ID = SR.ID AND SCH.MOVIE_ID = \"%s\" AND SCH.SHOW_DATE = \"%s\" AND SCH.SHOW_TIME_ID = \"%s\" AND CINEMA_ID = \"%s\";", bookingInfor.getIdMovie(), bookingInfor.getDate(), bookingInfor.getTime(), bookingInfor.getNameCinema());
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

    public ArrayList<ArrayList<String>> getSeat() {
        return select("S.*", 0, -1, String.format("SCREEN_ROOM_ID = \"%s\"  AND CINEMA_ID = \"%s\" ;", bookingInfor.getScreen(), bookingInfor.getNameCinema()), "", "SEATS S JOIN SCREEN_ROOMS SR ON S.SCREEN_ROOM_ID = SR.ID").getData();
    }

    public ArrayList<ArrayList<String>> getTimeSlotList() {
        return select("DISTINCT(ST.ID), DATE_FORMAT(START_TIME, '%h:%i') AS TIME_SLOT",0, -1, String.format("SCH.SHOW_TIME_ID = ST.ID AND SCH.SCREEN_ROOM_ID = SR.ID AND SCH.MOVIE_ID = \"%s\" AND SCH.SHOW_DATE = \"%s\" AND SR.CINEMA_ID = \"%s\" ORDER BY TIME_SLOT ASC;", bookingInfor.getIdMovie(), bookingInfor.getDate(), bookingInfor.getNameCinema()),"","SCHEDULES SCH, SHOW_TIMES ST, SCREEN_ROOMS SR").getData();
    }

    public ArrayList<ArrayList<String>> getTheaterList() {
        return select("DISTINCT(CIN.ID), CIN.NAME ", 0, -1, String.format("SCH.SCREEN_ROOM_ID = SR.ID AND SR.CINEMA_ID = CIN.ID AND MOVIE_ID = \"%s\" AND SCH.SHOW_DATE = \"%s\";", bookingInfor.getIdMovie(), bookingInfor.getDate()), "","SCHEDULES SCH, SCREEN_ROOMS SR, CINEMAS CIN " ).getData();
    }

    public ArrayList<ArrayList<String>> getPaymentMethod() {
        Response response = select("*", 0, -1, "","","PAYMENT_METHODS");
        ArrayList<ArrayList<String>> data = response.getData();
        return data;
    }
    public void storePaymentInfor(){
        System.out.println(bookingInfor.getIdMovie());
        System.out.println(bookingInfor.getNameCinema());
        System.out.println(bookingInfor.getDate());
        System.out.println(bookingInfor.getTime());
        System.out.println(bookingInfor.getScreen());
        System.out.println(bookingInfor.getSeats());
        System.out.println(bookingInfor.getPromotionCode());
        System.out.println(bookingInfor.getPaymentMethodId());
        System.out.println(bookingInfor.getComboPrice());
        System.out.println(bookingInfor.getTicketPrice());
        System.out.println(bookingInfor.getTotal());
        System.out.println(bookingInfor.getDiscount());
        System.out.println(bookingInfor.getItems());



//        LocalDateTime now = LocalDateTime.now();
//        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        HashMap<String, String> payment = new HashMap<String, String>();
//        payment.put("ID", idGenerator.generateId(Main.getInstance().getPaymentManagementProcessor().getDefaultDatabaseTable()));
//        payment.put("PAYMENT_ITEM_ID", "PI_" + String.format("%05d",  1));
//        payment.put("PAYMENT_TICKET_ID", "PT_" + String.format("%05d",  1));
//        payment.put("USER_ID", Main.getInstance().getSignedInUser().getId());
//        payment.put("PAYMENT_DATE", now.format(dateTimeFormatter));
//        payment.put("PAYMENT_METHOD_ID", bookingInfor.getPaymentMethodId());
//        payment.put("TOTAL_AMOUNT", Integer.toString(bookingInfor.getTotal()));
//        payment.put("SCHEDULE_ID", "SCH_00001");
//        Response response = Main.getInstance().getPaymentManagementProcessor().add(payment);
//        if (response.getStatusCode() == StatusCode.OK) {
//            System.out.println("insert 1 row success" + payment.get("ID"));
//        } else {
//            System.out.println(" failed");
//        }
    }
//    public String storePaymentTicket(){
//
//    }

}