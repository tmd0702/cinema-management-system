package UserManager;
import Utils.Role;

import java.util.Date;

abstract public class User {
    private String username, id, firstName, lastName, phone, email;
    private Date dateOfBirth;
    private int role, score;

    public User() {
        this.username = "";
        this.id = "";
        this.firstName = "";
        this.lastName = "";
        this.dateOfBirth = new Date();
        this.phone = "";
        this.email = "";
        this.role = Role.UNKNOWN.getValue();
        this.score = 0;
    }
    public User(String username, String id, String firstName, String lastName, Date dateOfBirth, String phone, String email, int score) {
        this.username = username;
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.phone = phone;
        this.email = email;
        this.score = score;
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
