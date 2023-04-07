package UserManager;
import Utils.Role;
public class Subscriber extends User{
    public Subscriber() {
        super();
        setRole(Role.SUBSCRIBER.getValue());
    }
    public Subscriber(String username, String id, String firstName, String lastName, String dateOfBirth, String phone, String email) {
        super(username, id, firstName, lastName, dateOfBirth, phone, email, Role.SUBSCRIBER.getValue());
    }

}
