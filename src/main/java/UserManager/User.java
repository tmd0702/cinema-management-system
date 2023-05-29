package UserManager;
import Utils.Role;

import java.util.Date;

abstract public class User {
    private String username, id, firstName, lastName, phone, email, gender;
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
    public String getGender() {
        return this.gender;
    }
    public User(String username, String id, String firstName, String lastName, Date dateOfBirth, String phone, String email, String gender, int score) {
        this.username = username;
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.phone = phone;
        this.email = email;
        this.score = score;
        this.gender = gender;
    }
    public String getId() {
        return this.id;
    }
    public String getUsername() {
        return this.username;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getScore() {
        return score;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public void setRole(int role) {
        this.role = role;
    }
    public void signOut() {

    }

}
