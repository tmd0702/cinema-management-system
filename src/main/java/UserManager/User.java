package UserManager;
import Utils.Role;

abstract public class User {
    private String username, id, firstName, lastName, dateOfBirth, phone, email;
    private int role;

    public User() {
        this.username = "";
        this.id = "";
        this.firstName = "";
        this.lastName = "";
        this.dateOfBirth = "";
        this.phone = "";
        this.email = "";
        this.role = Role.UNKNOWN.getValue();
    }
    public User(String username, String id, String firstName, String lastName, String dateOfBirth, String phone, String email, int role) {
        this.username = username;
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.phone = phone;
        this.email = email;
        this.role = role;
    }
    public void setRole(int role) {
        this.role = role;
    }
    public void signin() {

    }
    public void signup() {

    }
    public void signout() {

    }

}
