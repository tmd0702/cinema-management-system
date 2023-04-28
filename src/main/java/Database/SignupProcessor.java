package Database;
import Utils.IdGenerator;
import Utils.StatusCode;
import Utils.Validator;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import Exception.*;
import java.sql.*;
import Utils.IdGenerator;
public class SignupProcessor extends AuthenticationProcessor {
    private IdGenerator idGenerator;
    public SignupProcessor() throws Exception {
        idGenerator = new IdGenerator();
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
    public StatusCode handleSignupAction(HashMap<String, String> signupInfo) {
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
            String id = idGenerator.generateId("USERS");
            System.out.println(id);
            String insertUserQuery = String.format("INSERT INTO USERS(ID, USERNAME, FIRST_NAME, LAST_NAME, DOB, GENDER, ADDRESS, PHONE, EMAIL, USER_ROLE, SCORE) VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', %d);", id, signupInfo.get("username"), signupInfo.get("firstName"), signupInfo.get("lastName"), new Date(new SimpleDateFormat("dd-MM-yyyy").parse(signupInfo.get("dateOfBirth")).getTime()), signupInfo.get("gender"), signupInfo.get("address"), signupInfo.get("phone"), signupInfo.get("email"), "SUBSCRIBER", 0);
            System.out.println(insertUserQuery);
            Statement st = getConnector().createStatement();
            st.execute(insertUserQuery);
            addAuthentication(id, signupInfo.get("password"));
            st.close();
            return StatusCode.OK;
        } catch (InvalidEmailException e) {
            System.out.println(e);
        } catch (InvalidUsernameException e) {
            System.out.println(e);
        } catch (InvalidPasswordException e) {
            System.out.println(e);
        } catch (Exception e) {
            System.out.println(e);
        }
        return StatusCode.BAD_REQUEST;
    }
}
