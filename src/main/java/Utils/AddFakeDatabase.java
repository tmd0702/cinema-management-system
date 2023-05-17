package Utils;

import Database.*;
import MovieManager.Movie;
import com.example.GraphicalUserInterface.Main;
import javafx.scene.control.RadioButton;
import javafx.scene.image.Image;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.time.LocalTime;
import MovieManager.MovieManager;
import com.example.GraphicalUserInterface.Main;
public class AddFakeDatabase {
    Processor accountManagementProcessor, promotionManagementProcessor, theaterManagementProcessor, screenRoomManagementProcessor;
    Processor seatManagementProcessor, showTimeManagementProcessor, scheduleManagementProcessor;
    IdGenerator idGenerator;
    static MovieManagementProcessor movieManagementProcessor;
    Main main;
    public AddFakeDatabase() throws Exception {
        theaterManagementProcessor = new TheaterManagementProcessor();
        this.promotionManagementProcessor = new PromotionManagementProcessor();
        this.accountManagementProcessor = new AccountManagementProcessor();
        this.screenRoomManagementProcessor = new ScreenRoomManagementProcessor();
        this.seatManagementProcessor = new SeatManagementProcessor();
        this.showTimeManagementProcessor = new ShowTimeManagementProcessor();
        this.scheduleManagementProcessor = new ScheduleManagementProcessor();
        this.movieManagementProcessor = new MovieManagementProcessor();
        this.idGenerator = new IdGenerator();
        this.main = new Main();
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
            account.put("ID", idGenerator.generateId(accountManagementProcessor.getDefaultDatabaseTable()));
            account.put("USERNAME", "admin" + (i + 1));
            Response response = accountManagementProcessor.add(account);
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

        for (int i=0;i<200;++i) {
            promotion.put("ID", idGenerator.generateId(promotionManagementProcessor.getDefaultDatabaseTable()));
            promotion.put("PROMOTION_NAME", "Khuyen mai dot " + i);
            Response response = promotionManagementProcessor.add(promotion);
            if (response.getStatusCode() == StatusCode.OK) {
                System.out.println("insert 1 row success");
            } else {
                System.out.println(i + " failed");
            }
        }
    }
    public void addFakeTheaters() throws Exception {
        HashMap<String, String> theater = new HashMap<String, String>();

        theater.put("NAME", "4HB THU DUC");
        theater.put("ADDRESS", "test");
        theater.put("CINE_AREA", "TPHCM");
        for (int i=0;i<50;++i) {
            theater.put("ID", idGenerator.generateId(theaterManagementProcessor.getDefaultDatabaseTable()));
            Response response = theaterManagementProcessor.add(theater);
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
        for (int i=0;i<50;++i) {
            for(int j = 1; j <= 6; j++) {
                room.put("ID", idGenerator.generateId(screenRoomManagementProcessor.getDefaultDatabaseTable()));
                room.put("NAME", "ROOM_" + j);
                room.put("CINEMA_ID", "CIN_" + String.format("%05d", (i + 1)));
                Response response = screenRoomManagementProcessor.add(room);
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
        for (int i = 0; i < 50; ++i) { // i: cinema counter
            for (int j = 1; j <= 6; j++) { // j: screen room counter per cinema
                sr_id += 1;
                for (int k = 0; k < 7; k++) { // k : rows number of seat per screen room

                    for (int h = 0; h < 12; h++) { // h: columns number of seat per screen room

                        int t = h + 1;
                        seat.put("ID", idGenerator.generateId(seatManagementProcessor.getDefaultDatabaseTable()));
                        seat.put("NAME", String.valueOf((char)('A' + k)) + t);
                        seat.put("SCREEN_ROOM_ID", "SR_" + String.format("%05d", (sr_id)));


                        Response response = seatManagementProcessor.add(seat);
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
        int sr_id = 0;
        int sch_id = 0;
        for (int i=0;i<50;++i) {
            for(int j = 1; j <= 6; j++) {
                sr_id += 1;
                LocalDateTime now = LocalDateTime.now();
                for(int d = 0; d < 7; d++) {
                    LocalTime localTime = LocalTime.of(12,40);
                    for (int t = 0; t < 15; t++) {
                        sch_id += 1;
                        time.put("ID", idGenerator.generateId(showTimeManagementProcessor.getDefaultDatabaseTable()));
                        time.put("START_TIME", localTime.toString());
                        System.out.println(localTime.toString());
                        time.put("SHOW_DATE", now.format(dateTimeFormatter));
                        System.out.println(now.format(dateTimeFormatter));
                        time.put("ROOM_ID", "SR_" + String.format("%05d", (sr_id)));
                        time.put("SCHEDULE_ID", "SCH_" + String.format("%05d",sch_id ));
                        Response response = showTimeManagementProcessor.add(time);
                        if (response.getStatusCode() == StatusCode.OK) {
                            System.out.println("insert 1 row success");
                        } else {
                            System.out.println(i + " failed");
                        }
                       localTime =  localTime.plusMinutes(40);
                    }
                    now = now.plusDays(1);
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        AddFakeDatabase addFakeDatabase = new AddFakeDatabase();
        //addFakeDatabase.addFakeAccounts();
//        addFakeDatabase.addFakePromotions();
//        addFakeDatabase.addFakeTheaters();
//        addFakeDatabase.addFakeScreenRooms();
        //addFakeDatabase.addFakeSeats();
        //addFakeDatabase.addFakeShowTimes();

    }
}

