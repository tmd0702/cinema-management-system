package Utils;

import Database.*;
import javafx.scene.control.RadioButton;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class AddFakeDatabase {
    Processor accountManagementProcessor, promotionManagementProcessor, theaterManagementProcessor, screenRoomManagementProcessor;
    Processor seatManagementProcessor;
    IdGenerator idGenerator;
    public AddFakeDatabase() throws Exception {
        theaterManagementProcessor = new TheaterManagementProcessor();
        this.promotionManagementProcessor = new PromotionManagementProcessor();
        this.accountManagementProcessor = new AccountManagementProcessor();
        this.screenRoomManagementProcessor = new ScreenRoomManagementProcessor();
        this.seatManagementProcessor = new SeatManagementProcessor();
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
                StatusCode status = screenRoomManagementProcessor.add(room);
                if (status == StatusCode.OK) {
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
        for (int i = 0; i < 50; ++i) {
            for (int j = 1; j <= 6; j++) {
                for (int k = 0; k < 7; k++) {
                    for (int h = 0; h < 12; h++) {
                        if (k == 0 && (h == 0 || h == 1 || h == 10 || h == 11) || k == 1 && (h == 0 || h == 11))
                            continue;
                        int t = h;
                        if(k == 0)
                            t = h - 1;
                        seat.put("ID", idGenerator.generateId(seatManagementProcessor.getDefaultDatabaseTable()));
                        seat.put("NAME", String.valueOf((char)('A' + k)) + t);
                        seat.put("SCREEN_ROOM_ID", "SR_" + String.format("%05d", (j)));

                        StatusCode status = seatManagementProcessor.add(seat);
                        if (status == StatusCode.OK) {
                            System.out.println("insert 1 row success" + seat.get("CINEMA_ID"));
                        } else {
                            System.out.println(i + " failed");
                        }
                    }
                }
            }
        }
    }
    public static void main(String[] args) throws Exception {
        AddFakeDatabase addFakeDatabase = new AddFakeDatabase();
        addFakeDatabase.addFakeAccounts();
        addFakeDatabase.addFakePromotions();
        addFakeDatabase.addFakeTheaters();
        addFakeDatabase.addFakeScreenRooms();
        addFakeDatabase.addFakeSeats();

    }
}

