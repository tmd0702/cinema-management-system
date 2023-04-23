package SearchEngine;

import com.example.GraphicalUserInterface.Main;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

public class SearchEngine {
    private Main main;
    public SearchEngine() {
        main = Main.getInstance();

    }
    public HashMap<String, Object> getSearchResults(String input, String type) {
        input = input.replace(" ", "%20");
        String inputLine = "", scores = "";
        JSONObject jo = new JSONObject();
        String url = String.format("http://localhost:8085/%s?input=%s", type, input);
        try {
            URL keywordsSearchingURL = new URL(url);
            URLConnection keywordsSearchingHTTP = keywordsSearchingURL.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    keywordsSearchingHTTP.getInputStream()));

            while ((inputLine = in.readLine()) != null)
                scores = inputLine;
            jo = new JSONObject(scores);
            in.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return Utils.Utils.jsonToMap(jo);
    }
}
