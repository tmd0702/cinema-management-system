package UserManager;
import Utils.Role;
public class Manager extends User{
    public Manager() {
        super();
        setRole(Role.MANAGER.getValue());
    }
    public Manager(String username, String id, String firstName, String lastName, String dateOfBirth, String phone, String email) {
        super(username, id, firstName, lastName, dateOfBirth, phone, email, Role.MANAGER.getValue());
    }

}
