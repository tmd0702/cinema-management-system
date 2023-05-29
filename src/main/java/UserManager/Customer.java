package UserManager;
import Utils.Role;

import java.util.Date;

public class Customer extends User {
    public Customer() {
        super();
        setRole(Role.CUSTOMER.getValue());
    }
    public Customer(String username, String id, String firstName, String lastName, Date dateOfBirth, String phone, String email, String gender, int score) {
        super(username, id, firstName, lastName, dateOfBirth, phone, email, gender, score);
        setRole(Role.CUSTOMER.getValue());
    }

}
