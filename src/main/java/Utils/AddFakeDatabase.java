package Utils;

import Database.*;
import com.example.GraphicalUserInterface.Main;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

public class AddFakeDatabase {
    Processor accountManagementProcessor, promotionManagementProcessor, theaterManagementProcessor, screenRoomManagementProcessor, userCategoryManagementProcessor;
    Processor seatManagementProcessor, showTimeManagementProcessor, scheduleManagementProcessor, itemManagementProcessor, seatTicketManagementProcessor, itemTicketMangementProcessor;
    Processor bookingitemsManagementProcessor, bookingTicketsManagementProcessor, ticketManagementProcessor, paymentManagementProcessor, paymentMethodManagementProcessor;
    IdGenerator idGenerator;
    static MovieManagementProcessor movieManagementProcessor;
    Main main;
    public AddFakeDatabase() throws Exception {
        theaterManagementProcessor = new TheaterManagementProcessor();
        this.userCategoryManagementProcessor = new UserCategoryManagementProcessor();
        this.promotionManagementProcessor = new PromotionManagementProcessor();
        this.accountManagementProcessor = new AccountManagementProcessor();
        this.screenRoomManagementProcessor = new ScreenRoomManagementProcessor();
        this.seatManagementProcessor = new SeatManagementProcessor();
        this.showTimeManagementProcessor = new ShowTimeManagementProcessor();
        this.scheduleManagementProcessor = new ScheduleManagementProcessor();
        this.itemManagementProcessor = new ItemManagementProcessor();
        this.seatTicketManagementProcessor = new SeatTicketManagementProcessor();
        this.itemTicketMangementProcessor = new ItemTicketManagementProcessor();
        this.bookingitemsManagementProcessor = new BookingItemManagementProccessor();
        this.bookingTicketsManagementProcessor = new BookingSeatManagementProcessor();
        this.ticketManagementProcessor = new TicketManagementProcessor();
        this.paymentManagementProcessor = new PaymentManagementProcessor();
        this.paymentMethodManagementProcessor = new PaymentMethodManagementProcessor();
        this.idGenerator = new IdGenerator();
    }
    public void addFakeAccounts() throws Exception {
        HashMap<String, String> account = new HashMap<String, String>();
        account.put("FIRST_NAME", "Duc");
        account.put("LAST_NAME", "Truong");
        account.put("EMAIL", "mduc017@gmail.com");
        account.put("PHONE", "0123456789");
        account.put("DOB", "2000-07-02");
        account.put("ADDRESS", "test");
        account.put("GENDER", "M");
        account.put("USER_ROLE", "admin");
        for (int i=0;i<200;++i) {
            account.put("USER_CATEGORY_ID", "UC_00001");
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

        promotion.put("PROMOTION_NAME", "Khuyen mai dot 1");
        promotion.put("START_DATE", "2023-05-03");
        promotion.put("END_DATE", "2023-05-15");
        promotion.put("PROMOTION_DESCRIPTION", "mai dzo mai dzo, khuyen mai giam 20%");
        promotion.put("DISCOUNT", "0.2");
        promotion.put("USER_CATEGORY_ID", "UC_00001");
        promotion.put("PAYMENT_ID", "PAY_00001");
        for (int i=0;i<200;++i) {
            promotion.put("ID", idGenerator.generateId(promotionManagementProcessor.getDefaultDatabaseTable()));
            promotion.put("PROMOTION_NAME", "Khuyen mai dot " + i);
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


        theater.put("ADDRESS", "test");
        theater.put("CINE_AREA", "TPHCM");
        for (int i=0;i<2;++i) {
            theater.put("NAME", "4HB THU DUC"+ i);
            theater.put("ID", idGenerator.generateId(theaterManagementProcessor.getDefaultDatabaseTable()));
            Response response = theaterManagementProcessor.insertData(theater, true);
            if (response.getStatusCode() == StatusCode.OK) {
                System.out.println("insert 1 row success");
            } else {
                System.out.println(i + " failed");
            }
        }
    }

    public void addFakeScreenRooms() throws Exception{
        HashMap<String, String> room = new HashMap<String, String>();
        room.put("CAPACITY", "80");
        for (int i=0;i<2;++i) {
            for(int j = 1; j <= 6; j++) {
                room.put("ID", idGenerator.generateId(screenRoomManagementProcessor.getDefaultDatabaseTable()));
                room.put("NAME", "ROOM_" + j);
                room.put("CINEMA_ID", "CIN_" + String.format("%05d", (i + 1)));
                Response response = screenRoomManagementProcessor.insertData(room, true);
                if (response.getStatusCode() == StatusCode.OK) {
                    System.out.println("insert 1 row success" + room.get("CINEMA_ID"));
                } else {
                    System.out.println(i + " failed");
                }
            }
        }
    }
    public void addFakeSeats() throws Exception {
        HashMap<String, String> seat = new HashMap<String, String>();
        seat.put("CATEGORY", "NORMAL");
        seat.put("SEAT_STATUS", "AVAILABLE");
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


                        Response response = seatManagementProcessor.insertData(seat, true);
                        if (response.getStatusCode() == StatusCode.OK) {
                            System.out.println("insert 1 row success" + seat.get("CINEMA_ID"));
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
        for (String category : categories) {
            HashMap<String, String> userCategory = new HashMap<String, String>();
            userCategory.put("ID", idGenerator.generateId("USER_CATEGORY"));
            userCategory.put("CATEGORY", category);
            this.userCategoryManagementProcessor.insertData(userCategory, true);
        }

    }
    public void addFakeItems(){
        HashMap<String, String> item = new HashMap<String, String>();
            for(int j = 1; j <= 6; j++) {
                item.put("ID", idGenerator.generateId(itemManagementProcessor.getDefaultDatabaseTable()));
                item.put("NAME", "Popcorn" + j);
                item.put("CATEGORY", "Snack");
                item.put("PRICE", "5.99");
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
                seatTicket.put("AMOUNT", "70000");
                Response response = ticketManagementProcessor.insertData(seatTicket, true);
                if (response.getStatusCode() == StatusCode.OK) {
                    System.out.println("insert 1 row success" + seatTicket.get("ID"));
                } else {
                System.out.println(" failed");
                }
            }
        }
    }
//    public void addFakeItemTicket(){ // PaymentItems
//        HashMap<String, String> itemTicket = new HashMap<String, String>();
//        for (int k = 0; k < 7; k++) { // k : rows number of seat per screen room
//            for (int h = 0; h < 12; h++) { // h: columns number of seat per screen room
//                int t = h + 1;
//                itemTicket.put("ID", idGenerator.generateId(itemTicketMangementProcessor.getDefaultDatabaseTable()));
//                Response response = itemTicketMangementProcessor.insertData(itemTicket, true);
//                if (response.getStatusCode() == StatusCode.OK) {
//                    System.out.println("insert 1 row success" + itemTicket.get("ID"));
//                } else {
//                    System.out.println(" failed");
//                }
//            }
//        }
//    }
//    public void addFakeSeatTicket(){ // PaymentSeats
//        HashMap<String, String> seatTicket = new HashMap<String, String>();
//        for (int k = 0; k < 7; k++) { // k : rows number of seat per screen room
//            for (int h = 0; h < 12; h++) { // h: columns number of seat per screen room
//                int t = h + 1;
//                seatTicket.put("ID", idGenerator.generateId(seatTicketManagementProcessor.getDefaultDatabaseTable()));
//                Response response = seatTicketManagementProcessor.insertData(seatTicket, true);
//                if (response.getStatusCode() == StatusCode.OK) {
//                    System.out.println("insert 1 row success" + seatTicket.get("ID"));
//                } else {
//                    System.out.println(" failed");
//                }
//            }
//        }
//    }
    public void addFakeBookingSeats(){
        HashMap<String, String> seatBooing = new HashMap<String, String>();
        for (int k = 0; k < 7; k++) { // k : rows number of seat per screen room
            for (int h = 0; h < 12; h++) { // h: columns number of seat per screen room
                int t = h + 1;
                seatBooing.put("PAYMENT_TICKET_ID", "PT_" + String.format("%05d", k*12+t));
                seatBooing.put("TICKET_ID", "TIC_" + String.format("%05d", k*12+t));
                Response response = bookingTicketsManagementProcessor.insertData(seatBooing, true);
                if (response.getStatusCode() == StatusCode.OK) {
                    System.out.println("insert 1 row success" + seatBooing.get("ID"));
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
                itemBooking.put("PAYMENT_ITEM_ID", "PI_" + String.format("%05d",  k * 12 + t));
                itemBooking.put("ITEM_ID", "ITE_" + String.format("%05d", 1));
                itemBooking.put("QUANTITY", "1");
                Response response = bookingitemsManagementProcessor.insertData(itemBooking, true);
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
    public static void main(String[] args) throws Exception {
        AddFakeDatabase addFakeDatabase = new AddFakeDatabase();
//        addFakeDatabase.addFakeUserCategory();
//        addFakeDatabase.addFakeAccounts();
//        addFakeDatabase.addFakePromotions();
//        addFakeDatabase.addFakeTheaters();
//        addFakeDatabase.addFakeScreenRooms();
//        addFakeDatabase.addFakeSeats();
//        addFakeDatabase.addFakeShowTimes();
//        addFakeDatabase.addFakeItems();
//        addFakeDatabase.addFakeTicket();
//        addFakeDatabase.addFakeItemTicket();
//        addFakeDatabase.addFakeBookingItems();
//        addFakeDatabase.addFakeSeatTicket();
//        addFakeDatabase.addFakeBookingSeats();
//        addFakeDatabase.addFakePaymentMethod();;
        addFakeDatabase.addFakePayments();
    }
}

