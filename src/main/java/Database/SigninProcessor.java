package Database;

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
}
