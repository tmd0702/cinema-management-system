package UserManager;
import Utils.Role;

import java.util.Date;

public class Subscriber extends User{
    public Subscriber() {
        super();
        setRole(Role.SUBSCRIBER.getValue());
    }
    public Subscriber(String username, String id, String firstName, String lastName, Date dateOfBirth, String phone, String email, int score) {
        super(username, id, firstName, lastName, dateOfBirth, phone, email, score);
        setRole(Role.SUBSCRIBER.getValue());
    }

}
