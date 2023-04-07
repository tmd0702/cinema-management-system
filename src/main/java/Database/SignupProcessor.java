package Database;

import Utils.Validator;

import java.util.Map;

public class SignupProcessor extends AuthenticationProcessor {
    public SignupProcessor() {

    }
    public void validateSignupInfo(Map <String, String> signupInfo) {
        String email = signupInfo.get("email");
        String username = signupInfo.get("username");
        String password = signupInfo.get("password");
        if (!Validator.validateEmail(email)) {

        }
        if (!Validator.validateUsername(username)) {

        }
        if (!Validator.validatePassword(password)) {

        }
    }
    public void handleSignupAction(Map<String, String> signupInfo) {
//        String query = String.format("SELECT PASS FROM AUTHENTICATION A JOIN USERS U ON A.ID = U.ID WHERE U.USERNAME = %s", username);
//        try {
//            Statement st = getConnector().createStatement();
//            ResultSet rs = st.executeQuery(query);
//            while (rs.next())
//            {
//                String truePassword = rs.getString("pass");
//                if (truePassword != signupInfo.get("password")) {
//
//                } else {
//
//                }
//            }
//            st.close();
//        } catch (Exception e) {
//            System.out.println(e);
//        }
    }
}
