package Database;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;

public class SigninProcessor extends AuthenticationProcessor {
    public SigninProcessor() {

    }
    public void handleSigninAction(Map<String, String> signinInfo) {
        String username = signinInfo.get("username");
        String password = signinInfo.get("password");
        String query = String.format("SELECT PASS FROM AUTHENTICATION A JOIN USERS U ON A.ID = U.ID WHERE U.USERNAME = %s", username);
        try {
            Statement st = getConnector().createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next())
            {
                String truePassword = rs.getString("pass");
                if (truePassword != password) {

                } else {

                }
            }
            st.close();
        } catch (Exception e) {
            System.out.println(e);
        }

    }
}
