package Utils;

import Database.*;
import MovieManager.Movie;
import com.example.GraphicalUserInterface.Main;

import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class AddFakeDatabase {
    Processor accountManagementProcessor, promotionManagementProcessor, cinemaManagementProcessor, screenRoomManagementProcessor, userCategoryManagementProcessor;
    Processor seatManagementProcessor, showTimeManagementProcessor, scheduleManagementProcessor, itemManagementProcessor, bookingTicketManagementProcessor, bookingItemMangementProcessor;
    Processor ticketManagementProcessor, paymentManagementProcessor, paymentMethodManagementProcessor;
    Processor seatCategoryManangementProcessor, itemCategoryManagementProcessor, seatPriceManagementProcessor, itemPriceManagementProcessor;
    IdGenerator idGenerator;
    MovieManagementProcessor movieManagementProcessor;
    Main main;
    public AddFakeDatabase() throws Exception {
        cinemaManagementProcessor = new CinemaManagementProcessor();
        this.userCategoryManagementProcessor = new UserCategoryManagementProcessor();
        this.promotionManagementProcessor = new PromotionManagementProcessor();
        this.accountManagementProcessor = new AccountManagementProcessor();
        this.screenRoomManagementProcessor = new ScreenRoomManagementProcessor();
        this.seatManagementProcessor = new SeatManagementProcessor();
        this.showTimeManagementProcessor = new ShowTimeManagementProcessor();
        this.scheduleManagementProcessor = new ScheduleManagementProcessor();
        this.itemManagementProcessor = new ItemManagementProcessor();
        this.bookingTicketManagementProcessor = new BookingTicketManagementProcessor();
        this.bookingItemMangementProcessor = new BookingItemManagementProcessor();
        this.ticketManagementProcessor = new TicketManagementProcessor();
        this.paymentManagementProcessor = new PaymentManagementProcessor();
        this.paymentMethodManagementProcessor = new PaymentMethodManagementProcessor();
        this.seatCategoryManangementProcessor = new SeatCategoryManagementProcessor();
        this.itemCategoryManagementProcessor = new ItemCategoryManagementProcessor();
        this.seatPriceManagementProcessor = new SeatPriceManagementProcessor();
        this.itemPriceManagementProcessor = new ItemPriceManagementProcessor();
        this.movieManagementProcessor = new MovieManagementProcessor();
        this.idGenerator = new IdGenerator();
        this.main = Main.getInstance();
        main.setProcessorManager(new ProcessorManager());
    }
    public void addFakeAccounts() throws Exception {

        HashMap<String, String> account = new HashMap<String, String>();
        account.put("FIRST_NAME", "Duc");
        account.put("LAST_NAME", "Truong");
        account.put("EMAIL", "mduc017@gmail.com");
        account.put("PHONE", "0123456789");
        account.put("DOB", "2000-07-02");
        account.put("REGIS_DATE", "2023-06-09");
        account.put("ADDRESS", "test");
        account.put("GENDER", "M");
        account.put("USER_ROLE", "admin");
        account.put("STATUS", "Active");
        for (int i=0;i<200;++i) {
            account.put("ID", idGenerator.generateId(accountManagementProcessor.getDefaultDatabaseTable()));
            account.put("USERNAME", "admin" + (i + 1));
            Response response = accountManagementProcessor.insertData(account, true);
            if (response.getStatusCode() == StatusCode.OK) {
                System.out.println("insert 1 row success");
            } else {
                System.out.println(i + " failed");
            }
        }
    }
    public void addFakePromotions() throws Exception {
        HashMap<String, String> promotion = new HashMap<String, String>();
        promotion.put("DESCRIPTION", "mai dzo mai dzo, khuyen mai giam 20%");
        promotion.put("DISCOUNT", "0.2");
        for (int i=0;i<200;++i) {
            promotion.put("NAME", "Khuyen mai dot " + i);
            if(i <= 100){
                promotion.put("START_DATE", "2023-05-03");
                promotion.put("END_DATE", "2023-05-15");
                promotion.put("STATUS", "Close");
                promotion.put("USER_CATEGORY_ID", "UC_00001");
            }
            else{
                promotion.put("START_DATE", "2023-06-09");
                promotion.put("END_DATE", "2023-12-01");
                promotion.put("STATUS", "Active");
                if(i < 150)
                    promotion.put("USER_CATEGORY_ID", "UC_00002");
                else
                    promotion.put("USER_CATEGORY_ID", "UC_00001");
            }
            promotion.put("ID", idGenerator.generateId(promotionManagementProcessor.getDefaultDatabaseTable()));
            promotion.put("NAME", "Khuyen mai dot " + i);
            Response response = promotionManagementProcessor.insertData(promotion, true);
            if (response.getStatusCode() == StatusCode.OK) {
                System.out.println("insert 1 row success");
            } else {
                System.out.println(i + " failed");
            }
        }
    }
    public void addFakeTheaters() throws Exception {
        HashMap<String, String> theater = new HashMap<String, String>();
        theater.put("ADDRESS", "TPHCM");
        for (int i=0;i<10;++i) {
            theater.put("NAME", "4HB THU DUC "+ (i+1));
            theater.put("ID", idGenerator.generateId(cinemaManagementProcessor.getDefaultDatabaseTable()));
            if(i<8)
                theater.put("STATUS", "Active");
            else
                theater.put("STATUS", "Close");
            Response response = cinemaManagementProcessor.insertData(theater, true);
            if (response.getStatusCode() == StatusCode.OK) {
                System.out.println("insert 1 row success");
            } else {
                System.out.println(i + " failed");
            }
        }
    }

    public void addFakeScreenRooms() throws Exception{
        HashMap<String, String> room = new HashMap<String, String>();
        room.put("CAPACITY", "84");
        for (int i=0;i<10;++i) {
            for(int j = 1; j <= 6; j++) {
                room.put("ID", idGenerator.generateId(screenRoomManagementProcessor.getDefaultDatabaseTable()));
                room.put("NAME", "ROOM_" + j);
                room.put("CINEMA_ID", "CIN_" + String.format("%05d", (i + 1)));
                if(j < 6)
                    room.put("STATUS" , "Active");
                else
                    room.put("STATUS", "Close");
                Response response = screenRoomManagementProcessor.insertData(room, true);
                if (response.getStatusCode() == StatusCode.OK) {
                    System.out.println("insert 1 row success" + room.get("CINEMA_ID"));
                } else {
                    System.out.println(i + " failed");
                }
            }
        }
    }
    public void addFakeSeatCategory(){
        HashMap<String, String> seatCategory = new HashMap<String, String>();
        ArrayList<String> category = new ArrayList<String>();
        category.add("NORMAL");
        category.add("VIP");
        category.add("COUPLE");
        for(String c : category) {
            seatCategory.put("ID", idGenerator.generateId(seatCategoryManangementProcessor.getDefaultDatabaseTable()));
            seatCategory.put("CATEGORY", c);
            Response response = seatCategoryManangementProcessor.insertData(seatCategory, true);
            if (response.getStatusCode() == StatusCode.OK) {
                System.out.println("insert 1 row success" + seatCategory.get("ID"));
            } else {
                System.out.println(" failed");
            }
        }
    }
    public void addFakeSeats() throws Exception {
        HashMap<String, String> seat = new HashMap<String, String>();
        seat.put("SEAT_CATEGORY_ID", "SC_00001");
        int sr_id = 0;
        for (int i = 0; i < 2; ++i) { // i: cinema counter
            for (int j = 1; j <= 6; j++) { // j: screen room counter per cinema
                sr_id += 1;
                for (int k = 0; k < 7; k++) { // k : rows number of seat per screen room

                    for (int h = 0; h < 12; h++) { // h: columns number of seat per screen room

                        int t = h + 1;
                        seat.put("ID", idGenerator.generateId(seatManagementProcessor.getDefaultDatabaseTable()));
                        seat.put("NAME", String.valueOf((char)('A' + k)) + t);
                        seat.put("SCREEN_ROOM_ID", "SR_" + String.format("%05d", (sr_id)));
                        if(k == 6 && (h == 11 || h == 10))
                            seat.put("STATUS", "Close");
                        else seat.put("STATUS", "Active");
                        Response response = seatManagementProcessor.insertData(seat, true);
                        if (response.getStatusCode() == StatusCode.OK) {
                            System.out.println("insert 1 row success" + seat.get("ID"));
                        } else {
                            System.out.println(i + " failed");
                        }
                    }
                }
            }
        }
    }


    public void addFakeShowTimes() throws Exception{
        HashMap<String, String> time = new HashMap<String, String>();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalTime localTime = LocalTime.of(12,40);
        time.put("STATUS", "Active");
        for (int t = 0; t < 15; t++) {
            time.put("ID", idGenerator.generateId(showTimeManagementProcessor.getDefaultDatabaseTable()));
            time.put("START_TIME", localTime.toString());
            System.out.println(localTime.toString());
            Response response = showTimeManagementProcessor.insertData(time, true);
            if (response.getStatusCode() == StatusCode.OK) {
                System.out.println("insert 1 row success");
            } else {
                System.out.println(response.getMessage());
            }
            localTime =  localTime.plusMinutes(40);
        }
    }
    public void addFakeUserCategory() {
        ArrayList<String> categories = new ArrayList<String>();
        categories.add("Member");
        categories.add("VIP");
        categories.add("VVIP");
        ArrayList<String> pointLowerbounds = new ArrayList<String>();
        pointLowerbounds.add("0");
        pointLowerbounds.add("2000");
        pointLowerbounds.add("4000");
        for (String category : categories) {
            HashMap<String, String> userCategory = new HashMap<String, String>();
            userCategory.put("ID", idGenerator.generateId("USER_CATEGORY"));
            userCategory.put("CATEGORY", category);
            userCategory.put("POINT_LOWERBOUND", pointLowerbounds.get(categories.indexOf(category)));
            this.userCategoryManagementProcessor.insertData(userCategory, true);
        }

    }
    public void addFakeItemCategory(){
        ArrayList<String> categories = new ArrayList<String>();
        categories.add("Drink");
        categories.add("Popcorn");
        categories.add("Combo1");
        categories.add("Combo2");
        for (String category : categories) {
            HashMap<String, String> itemCategory = new HashMap<String, String>();
            itemCategory.put("ID", idGenerator.generateId("ITEM_CATEGORY"));
            itemCategory.put("CATEGORY", category);
            this.itemCategoryManagementProcessor.insertData(itemCategory, true);
        }
    }
    public void addFakeItems(){
        HashMap<String, String> item = new HashMap<String, String>();
        item.put("REVENUE", "0");
        for(int j = 1; j <= 6; j++) {
            item.put("ID", idGenerator.generateId(itemManagementProcessor.getDefaultDatabaseTable()));
            item.put("NAME", "Popcorn" + j);
            item.put("ITEM_CATEGORY_ID", "IC_00002");
            item.put("UNIT", "KG");
            item.put("QUANTITY", "100");
            if(j < 6)
            item.put("STATUS", "Active");
            else item.put("STATUS", "Close");
            Response response = itemManagementProcessor.insertData(item, true);
            if (response.getStatusCode() == StatusCode.OK) {
                System.out.println("insert 1 row success" + item.get("ID"));
            } else {
                System.out.println(" failed");
            }
        }
    }
    public void addFakeTicket(){
        HashMap<String, String> seatTicket = new HashMap<String, String>();
        for (int k = 0; k < 7; k++) { // k : rows number of seat per screen room
            for (int h = 0; h < 12; h++) { // h: columns number of seat per screen room
                int t = h + 1;
                seatTicket.put("ID", idGenerator.generateId(ticketManagementProcessor.getDefaultDatabaseTable()));
                seatTicket.put("SEAT_ID", "SEA_" + String.format("%05d", k * 12 + t));
                seatTicket.put("SCHEDULE_ID", "SCH_" + String.format("%05d", 1));
                Response response = ticketManagementProcessor.insertData(seatTicket, true);
                if (response.getStatusCode() == StatusCode.OK) {
                    System.out.println("insert 1 row success" + seatTicket.get("ID"));
                } else {
                    System.out.println(" failed");
                }
            }
        }
    }
    public void addFakeBookingSeats(){
        HashMap<String, String> seatBooking = new HashMap<String, String>();
        for (int k = 0; k < 7; k++) { // k : rows number of seat per screen room
            for (int h = 0; h < 12; h++) { // h: columns number of seat per screen room
                int t = h + 1;
                seatBooking.put("PAYMENT_ID", "PAY_" + String.format("%05d", k*12+t));
                seatBooking.put("TICKET_ID", "TIC_" + String.format("%05d", k*12+t));
                seatBooking.put("SEAT_PRICE_ID", "SP_00001");
                Response response = bookingTicketManagementProcessor.insertData(seatBooking, true);
                if (response.getStatusCode() == StatusCode.OK) {
                    System.out.println("insert 1 row success" + seatBooking.get("ID"));
                } else {
                    System.out.println(" failed");
                }
            }
        }
    }
    public void addFakeBookingItems(){
        HashMap<String, String> itemBooking = new HashMap<String, String>();
        for (int k = 0; k < 7; k++) { // k : rows number of seat per screen room
            for (int h = 0; h < 12; h++) { // h: columns number of seat per screen room
                int t = h + 1;
                itemBooking.put("PAYMENT_ID", "PAY_" + String.format("%05d",  k * 12 + t));
                itemBooking.put("ITEM_ID", "ITE_" + String.format("%05d", 1));
                itemBooking.put("QUANTITY", "1");
                itemBooking.put("ITEM_PRICE_ID", "IP_00001");
                Response response = bookingItemMangementProcessor.insertData(itemBooking, true);
                if (response.getStatusCode() == StatusCode.OK) {
                    System.out.println("insert 1 row success" + itemBooking.get("ID"));
                } else {
                    System.out.println(" failed");
                }
            }
        }
    }
    public void addFakePayments(){
        HashMap<String, String> payment = new HashMap<String, String>();
        for (int k = 0; k < 7; k++) { // k : rows number of seat per screen room
            for (int h = 0; h < 12; h++) { // h: columns number of seat per screen room
                int t = h + 1;
                payment.put("ID", idGenerator.generateId(paymentManagementProcessor.getDefaultDatabaseTable()));
                payment.put("USER_ID", "USE_00001");
                payment.put("PAYMENT_DATE", "2023-05-30 00:00:00");
                payment.put("PAYMENT_METHOD_ID", "PM_00001");
                payment.put("TOTAL_AMOUNT", "299000");
                payment.put("SCHEDULE_ID", "SCH_00001");
                Response response = paymentManagementProcessor.insertData(payment, true);
                if (response.getStatusCode() == StatusCode.OK) {
                    System.out.println("insert 1 row success" + payment.get("ID"));
                } else {
                    System.out.println(" failed");
                }
            }
        }
    }
    public void addFakePaymentMethod(){
        HashMap<String, String> paymentMethor = new HashMap<String, String>();
        ArrayList<String> method = new ArrayList<String>();
        method.add("MOMO");
        method.add("ZALOPAY");
        method.add("VISA_CARD");
        method.add("CRASH_MONEY");
        for(int i = 0; i < 3; i++){
                paymentMethor.put("ID", idGenerator.generateId(paymentMethodManagementProcessor.getDefaultDatabaseTable()));
                paymentMethor.put("NAME",method.get(i));
                Response response = paymentMethodManagementProcessor.insertData(paymentMethor, true);
                if (response.getStatusCode() == StatusCode.OK) {
                    System.out.println("insert 1 row success" + paymentMethor.get("ID"));
                } else {
                    System.out.println(" failed");
                }
        }
    }
    public void addFakePrice(){
        HashMap<String, String> seat_price = new HashMap<String, String>();
        HashMap<String, String> item_price = new HashMap<String, String>();
        for(int i = 1; i < 5; i++){
            item_price.put("ID", idGenerator.generateId(itemPriceManagementProcessor.getDefaultDatabaseTable()));
            if(i < 4) {
                item_price.put("ITEM_CATEGORY_ID", "IC_" + String.format("%05d", i));
                item_price.put("PRICE", "45000");
            }else {
                item_price.put("ITEM_CATEGORY_ID", "IC_" + String.format("%05d", i));
                item_price.put("PRICE", Integer.toString(70000 + 10000));
            }
            item_price.put("DATE", "2023-06-03");
            Response response = itemPriceManagementProcessor.insertData(item_price, true);
            if (response.getStatusCode() == StatusCode.OK) {
                System.out.println("insert 1 row success" + item_price.get("ID"));
            } else {
                System.out.println(" failed");
            }
        }
        for(int i = 1; i < 4; i++){
            seat_price.put("ID", idGenerator.generateId(seatPriceManagementProcessor.getDefaultDatabaseTable()));
            seat_price.put("SEAT_CATEGORY_ID", "SC_" + String.format("%05d", i));
            seat_price.put("PRICE", Integer.toString(70000 + i * 20000));
            seat_price.put("DATE", "2023-06-03");
            Response response = seatPriceManagementProcessor.insertData(seat_price, true);
            if (response.getStatusCode() == StatusCode.OK) {
                System.out.println("insert 1 row success" + seat_price.get("ID"));
            } else {
                System.out.println(" failed");
            }
        }
    }
    public void addFakeSchedule(){
        String query = "SELECT * FROM MOVIES LIMIT 100";// LIMIT 100";
        ArrayList<Movie> tmpList = new ArrayList<Movie>();
        try {
            Statement st = movieManagementProcessor.getConnector().createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                Movie movie = new Movie(rs.getString("ID"), rs.getString("TITLE"), rs.getString("OVERVIEW"), rs.getString("STATUS"), rs.getInt("DURATION"), rs.getInt("VIEW_COUNT"), rs.getDate("RELEASE_DATE"), rs.getString("POSTER_PATH"), rs.getString("BACKDROP_PATH"), rs.getString("LANGUAGE"));
                tmpList.add(movie);
            }
            System.out.println("Start creating schedule");
            Collections.sort(tmpList, Comparator.comparingInt(Movie::getDuration));
            System.out.println(tmpList.size());
            for(Movie movie : tmpList) {
                movieManagementProcessor.scheduleMovie(movie);
            }
            rs.close();
            st.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void main(String[] args) throws Exception {
        AddFakeDatabase addFakeDatabase = new AddFakeDatabase();
//        addFakeDatabase.addFakeUserCategory();
//        addFakeDatabase.addFakeAccounts();
//        addFakeDatabase.addFakePromotions();
//        addFakeDatabase.addFakeTheaters();
//        addFakeDatabase.addFakeScreenRooms();
//        addFakeDatabase.addFakeSeatCategory();
//        addFakeDatabase.addFakeSeats();
//        addFakeDatabase.addFakeShowTimes();
        addFakeDatabase.addFakeSchedule();
//        addFakeDatabase.addFakeItemCategory();
//        addFakeDatabase.addFakeItems();
//        addFakeDatabase.addFakeTicket();
//        addFakeDatabase.addFakePaymentMethod();
//        addFakeDatabase.addFakePrice();
//        addFakeDatabase.addFakePayments();
//        addFakeDatabase.addFakeBookingItems();
//        addFakeDatabase.addFakeBookingSeats();
    }
}

