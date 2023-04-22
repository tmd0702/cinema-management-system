package Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import Utils.Utils.*;
import java.net.URLConnection;
import java.util.HashMap;

import org.json.JSONObject;
public class HTTPGetRequestTest{
    public static String getKeywordsSearchingResults() {
        String input = "an adventure of alive toys".replace(" ", "%20"), inputLine = "", scores = "";
        String url = String.format("http://localhost:8085/semantic_searching?input=%s", input);
        try {
            URL keywordsSearchingURL = new URL(url);
            URLConnection keywordsSearchingHTTP = keywordsSearchingURL.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    keywordsSearchingHTTP.getInputStream()));

            while ((inputLine = in.readLine()) != null)
                scores = inputLine;
            in.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return scores;
    }
    public static void main(String[] args) {
        String tmp = getKeywordsSearchingResults();
        JSONObject jo = new JSONObject(tmp);
        System.out.println(jo.toString());
        HashMap<String, Object> scores = Utils.Utils.jsonToMap(jo);
//        System.out.println(tmp);
    }

}
