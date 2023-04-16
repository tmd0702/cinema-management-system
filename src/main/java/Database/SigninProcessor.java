package Database;

import UserManager.Manager;
import UserManager.Subscriber;
import UserManager.User;
import Utils.Role;
import Utils.StatusCode;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

public class SigninProcessor extends AuthenticationProcessor {
    public SigninProcessor() {

    }
    public StatusCode handleSigninAction(HashMap<String, String> signinInfo) {
        String username = signinInfo.get("username");
        String password = signinInfo.get("password");
        String query = String.format("SELECT PASS FROM AUTHENTICATION A JOIN USERS U ON A.ID = U.ID WHERE U.USERNAME = '%s'", username);
        try {
            Statement st = getConnector().createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next())
            {
                String truePassword = rs.getString("pass");
                System.out.println(truePassword + password+ truePassword.length()+ password.length());
                if (truePassword.equals(password)) {
                    return StatusCode.OK;
                } else {
                    return StatusCode.BAD_REQUEST;
                }
            }
            st.close();
        } catch (Exception e) {
            System.out.println(e);
            return StatusCode.BAD_REQUEST;
        }
        return StatusCode.BAD_REQUEST;
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
