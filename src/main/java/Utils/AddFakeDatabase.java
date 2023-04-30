package Utils;

import Database.AccountManagementProcessor;
import javafx.scene.control.RadioButton;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class AddFakeDatabase {
    AccountManagementProcessor accountManagementProcessor;
    IdGenerator idGenerator;
    public AddFakeDatabase() throws Exception {
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
    public static void main(String[] args) throws Exception {
        AddFakeDatabase addFakeDatabase = new AddFakeDatabase();
        addFakeDatabase.addFakeAccounts();
    }
}

