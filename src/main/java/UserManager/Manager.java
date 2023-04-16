package UserManager;
import Utils.Role;

import java.util.Date;

public class Manager extends User{
    public Manager() {
        super();
        setRole(Role.MANAGER.getValue());
    }
    public Manager(String username, String id, String firstName, String lastName, Date dateOfBirth, String phone, String email, int score) {
        super(username, id, firstName, lastName, dateOfBirth, phone, email, score);
        setRole(Role.MANAGER.getValue());
    }

}
