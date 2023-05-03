package Utils;

import Database.AccountManagementProcessor;
import Database.Processor;
import Database.PromotionManagementProcessor;
import Database.TheaterManagementProcessor;
import javafx.scene.control.RadioButton;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;

public class AddFakeDatabase {
    Processor accountManagementProcessor, promotionManagementProcessor, theaterManagementProcessor;

    IdGenerator idGenerator;
    public AddFakeDatabase() throws Exception {
        theaterManagementProcessor = new TheaterManagementProcessor();
        this.promotionManagementProcessor = new PromotionManagementProcessor();
        this.accountManagementProcessor = new AccountManagementProcessor();
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
            StatusCode status = accountManagementProcessor.add(account);
            if (status == StatusCode.OK) {
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
            StatusCode status = promotionManagementProcessor.add(promotion);
            if (status == StatusCode.OK) {
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
        for (int i=0;i<200;++i) {
            theater.put("ID", idGenerator.generateId(theaterManagementProcessor.getDefaultDatabaseTable()));
            StatusCode status = theaterManagementProcessor.add(theater);
            if (status == StatusCode.OK) {
                System.out.println("insert 1 row success");
            } else {
                System.out.println(i + " failed");
            }
        }
    }
    public static void main(String[] args) throws Exception {
        AddFakeDatabase addFakeDatabase = new AddFakeDatabase();
        addFakeDatabase.addFakeAccounts();
        addFakeDatabase.addFakePromotions();
        addFakeDatabase.addFakeTheaters();
    }
}

