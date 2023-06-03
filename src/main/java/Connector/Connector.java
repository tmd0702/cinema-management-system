package Connector;

import Configuration.Config;
import Utils.Response;
import Utils.StatusCode;
import Utils.Utils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;

public class Connector {
    private Config config;
    public Connector(Config config) {
        this.config = config;
    }
    public static String constructURLParamsFromJSONObject(JSONObject jsonData) {
        String urlParams = "";
        ArrayList<String> keys = new ArrayList<String>(jsonData.keySet());
        for (int i=0; i<jsonData.length();++i) {
            urlParams += keys.get(i) + "=" + jsonData.get(keys.get(i)).toString().replace(" ", "%20");
            if (i < jsonData.length() - 1) {
                urlParams += "&";
            }
        }
        return urlParams;
    }
    public JSONObject HTTPGetRequest(String route, JSONObject jsonData) throws Exception {
        String params = constructURLParamsFromJSONObject(jsonData);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(String.format("http://%s:%s%s?%s", this.config.get("API_HOST"), this.config.get("API_PORT"), route, params)))
                .headers("Content-Type", "text/plain;charset=UTF-8")
                .GET()
                .build();
        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        JSONObject jsonResponse = new JSONObject(response.body());
        return jsonResponse;
    }
    public JSONObject HTTPPostRequest(String route, JSONObject jsonData) throws Exception {
        String body = "";
        if (jsonData.length() > 0) {
            body = jsonData.toString();
        }
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(String.format("http://%s:%s%s",this.config.get("API_HOST"),this.config.get("API_PORT"),route)))
                .headers("Content-Type", "text/plain;charset=UTF-8")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        JSONObject jsonResponse = new JSONObject(response.body());
        return jsonResponse;
    }
    public JSONObject HTTPPutRequest(String route, JSONObject jsonData) throws Exception {
        String body = jsonData.toString();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(String.format("http://%s:%s%s", this.config.get("API_HOST"), this.config.get("API_PORT"), route)))
                .headers("Content-Type", "text/plain;charset=UTF-8")
                .PUT(HttpRequest.BodyPublishers.ofString(body))
                .build();
        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        JSONObject jsonResponse = new JSONObject(response.body());
        return jsonResponse;
    }
    public HashMap<String, Double> HTTPSearchEngineRequest(JSONObject jsonData) throws Exception{
        JSONObject requestResponse = HTTPGetRequest(this.config.get("API_SEARCH_ENGINE_ROUTE"), jsonData);
        return Utils.sortMapByValue(Utils.jsonToMap(requestResponse));
    }
    public JSONObject HTTPDeleteRequest(String route, JSONObject jsonData) throws Exception {
        String params = constructURLParamsFromJSONObject(jsonData);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(String.format("http://%s:%s%s?%s", this.config.get("API_HOST"), this.config.get("API_PORT"), route, params)))
                .headers("Content-Type", "text/plain;charset=UTF-8")
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        JSONObject jsonResponse = new JSONObject(response.body());
        return jsonResponse;
    }
    public Response HTTPSignInRequest(JSONObject jsonData) throws Exception {
        JSONObject requestResponse = HTTPPostRequest(this.config.get("API_SIGN_IN_ROUTE"), jsonData);
        return new Response(requestResponse.get("error_message").toString(), StatusCode.getByValue(Integer.parseInt(requestResponse.get("status_code").toString())));
    }
    public Response HTTPSignUpRequest(JSONObject jsonData) throws Exception {
        JSONObject requestResponse = HTTPPostRequest(this.config.get("API_SIGN_UP_ROUTE"), jsonData);
        return new Response(requestResponse.get("error_message").toString(), StatusCode.getByValue(Integer.parseInt(requestResponse.get("status_code").toString())));
    }
    public Response HTTPInsertDataRequest(JSONObject jsonData) throws Exception {
        JSONObject requestResponse = HTTPPostRequest(this.config.get("API_INSERT_DATA_ROUTE"), jsonData);
        return new Response(requestResponse.get("error_message").toString(), StatusCode.getByValue(Integer.parseInt(requestResponse.get("status_code").toString())));
    }
    public Response HTTPUpdateDataRequest(JSONObject jsonData) throws Exception {
        JSONObject requestResponse = HTTPPutRequest(this.config.get("API_UPDATE_DATA_ROUTE"), jsonData);
        return new Response(requestResponse.get("error_message").toString(), StatusCode.getByValue(Integer.parseInt(requestResponse.get("status_code").toString())));
    }
    public Response HTTPDeleteDataRequest(JSONObject jsonData) throws Exception {
        JSONObject requestResponse = HTTPDeleteRequest(this.config.get("API_DELETE_DATA_ROUTE"), jsonData);
        return new Response(requestResponse.get("error_message").toString(), StatusCode.getByValue(Integer.parseInt(requestResponse.get("status_code").toString())));
    }
    public Response HTTPRollbackRequest() throws Exception {
        JSONObject requestResponse = HTTPPostRequest(this.config.get("API_ROLLBACK_ROUTE"), new JSONObject());
        return new Response(requestResponse.get("error_message").toString(), StatusCode.getByValue(Integer.parseInt(requestResponse.get("status_code").toString())));
    }
    public Response HTTPCommitRequest() throws Exception {
        JSONObject requestResponse = HTTPPostRequest(this.config.get("API_COMMIT_ROUTE"), new JSONObject());
        return new Response(requestResponse.get("error_message").toString(), StatusCode.getByValue(Integer.parseInt(requestResponse.get("status_code").toString())));
    }
    public ArrayList<ArrayList<String>> getDataFetcherFromJSONData(JSONObject jsonData) {
        ArrayList<ArrayList<String>> dataFetcher = new ArrayList<ArrayList<String>>();
        ArrayList<String> columnNames = Utils.convertJSONArrayToArrayList((JSONArray) jsonData.get("column_names"));
        ArrayList<String> columnTypes = Utils.convertJSONArrayToArrayList((JSONArray) jsonData.get("column_datatypes"));
        dataFetcher.add(columnNames);
        dataFetcher.add(columnTypes);
        JSONArray values = (JSONArray) jsonData.get("values");
        for (int i = 0; i < values.length(); ++i) {
            dataFetcher.add(Utils.convertJSONArrayToArrayList((JSONArray) values.get(i)));
        }
        return dataFetcher;
    }
    public Response HTTPSelectDataRequest(JSONObject jsonData) throws Exception {
        JSONObject requestResponse = HTTPGetRequest(this.config.get("API_SELECT_DATA_ROUTE"), jsonData);
        return new Response(requestResponse.get("error_message").toString(), StatusCode.getByValue(Integer.parseInt(requestResponse.get("status_code").toString())), getDataFetcherFromJSONData((JSONObject) requestResponse.get("data")));
    }
    public static void main(String[] args) throws Exception {
        Connector connector = new Connector(new Config());
        JSONObject params = new JSONObject();

        params.put("processor", "USERS");
        params.put("start_index", "0");
        params.put("quantity", "-1");
        params.put("query_condition", "");
        params.put("sort_query", "DOB DESC");
        params.put("count", true);

        Response response = connector.HTTPSelectDataRequest(params);
        System.out.println(response.getStatusCode() + " " + response.getMessage());
        System.out.println(response.getData().get(2));
    }
}
