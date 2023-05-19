package Database;
import UserManager.Manager;
import UserManager.Subscriber;
import UserManager.User;
import Utils.*;
import Exception.*;
import com.example.GraphicalUserInterface.Main;
import com.example.GraphicalUserInterface.ManagementMain;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.HashMap;

public class AccountManagementProcessor extends Processor {
    public AccountManagementProcessor() {
        super();
        setDefaultDatabaseTable("USERS");
    }
    public Response handleSigninAction(HashMap<String, String> signinInfo) {
        String username = signinInfo.get("username");
        String password = signinInfo.get("password");
        String query = String.format("SELECT PASS FROM AUTHENTICATION A JOIN USERS U ON A.USER_ID = U.ID WHERE U.USERNAME = '%s'", username);
        try {
            Statement st = getConnector().createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next())
            {
                String truePassword = rs.getString("pass");
                System.out.println(truePassword + password+ truePassword.length()+ password.length());
                if (truePassword.equals(password)) {
                    return new Response("OK", StatusCode.OK);
                } else {
                    return new Response("Wrong password", StatusCode.BAD_REQUEST);
                }
            }
            st.close();
            return new Response("Wrong username", StatusCode.BAD_REQUEST);
        } catch (Exception e) {
            System.out.println(e);
            return new Response(e.getMessage(), StatusCode.BAD_REQUEST);
        }
    }
    public void addAuthentication(String id, String password) {
        try {
            Statement st = getConnector().createStatement();
            String insertPasswordQuery = String.format("INSERT INTO AUTHENTICATION(ID, PASS) VALUES ('%s', '%s');", id, password);
            System.out.println(insertPasswordQuery);
            st.execute(insertPasswordQuery);
            System.out.println("Success");
            st.close();
        } catch (Exception e) {
            System.out.println(e);
        }

    }
    public Response getData(int from, int quantity, String queryCondition, String sortQuery) {
        Response response = select("*", from, quantity, queryCondition, sortQuery, getDefaultDatabaseTable());
        return response;
    }
    public Response handleSignupAction(HashMap<String, String> signupInfo) {
        try {
            if (!Validator.validateEmail(signupInfo.get("email"))) {
                throw new InvalidEmailException(signupInfo.get("email") + " is invalid!");
            }
            if (!Validator.validateUsername(signupInfo.get("username"))) {
                throw new InvalidUsernameException(signupInfo.get("username") + " is invalid!");
            }
            if (!Validator.validatePassword(signupInfo.get("password"))) {
                throw new InvalidPasswordException(signupInfo.get("password") + " is invalid!");
            }
            String id = ManagementMain.getInstance().getIdGenerator().generateId("USERS");
            System.out.println(id);
            String insertUserQuery = String.format("INSERT INTO USERS(ID, USERNAME, FIRST_NAME, LAST_NAME, DOB, GENDER, ADDRESS, PHONE, EMAIL, USER_ROLE, SCORE) VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', %d);", id, signupInfo.get("username"), signupInfo.get("firstName"), signupInfo.get("lastName"), new Date(new SimpleDateFormat("dd-MM-yyyy").parse(signupInfo.get("dateOfBirth")).getTime()), signupInfo.get("gender"), signupInfo.get("address"), signupInfo.get("phone"), signupInfo.get("email"), "SUBSCRIBER", 0);
            System.out.println(insertUserQuery);
            Statement st = getConnector().createStatement();
            st.execute(insertUserQuery);
            addAuthentication(id, signupInfo.get("password"));
            st.close();
            return new Response("OK", StatusCode.OK);
        } catch (InvalidEmailException e) {
            System.out.println(e);
            return new Response(e.getMessage(), StatusCode.BAD_REQUEST);
        } catch (InvalidUsernameException e) {
            System.out.println(e);
            return new Response(e.getMessage(), StatusCode.BAD_REQUEST);
        } catch (InvalidPasswordException e) {
            System.out.println(e);
            return new Response(e.getMessage(), StatusCode.BAD_REQUEST);
        } catch (Exception e) {
            System.out.println(e);
            return new Response(e.getMessage(), StatusCode.BAD_REQUEST);
        }
    }
    public User getUser(String table, HashMap<String, String> conditionColumnValueMap) {
        User user = new Manager();
        String query = String.format("SELECT * FROM %s WHERE %s", table);
        try {
            Statement st = getConnector().createStatement();
            ResultSet rs = st.executeQuery(query);
            if (rs.getString("USER_ROLE") == Role.SUBSCRIBER.getDescription()) {
                user = new Subscriber(rs.getString("USERNAME"), rs.getString("ID"), rs.getString("FIRST_NAME"), rs.getString("LAST_NAME"), rs.getDate("DOB"), rs.getString("PHONE"), rs.getString("email"), rs.getInt("score"));
            } else if (rs.getString("USER_ROLE") == Role.MANAGER.getDescription()) {
                user = new Manager(rs.getString("USERNAME"), rs.getString("ID"), rs.getString("FIRST_NAME"), rs.getString("LAST_NAME"), rs.getDate("DOB"), rs.getString("PHONE"), rs.getString("email"), rs.getInt("score"));
            } else {
                user = new Subscriber(rs.getString("USERNAME"), rs.getString("ID"), rs.getString("FIRST_NAME"), rs.getString("LAST_NAME"), rs.getDate("DOB"), rs.getString("PHONE"), rs.getString("email"), rs.getInt("score"));
            }

            rs.close();
            st.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return user;
    }
}
