package Connector;

import Configuration.Config;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Connector {
    private Config _config;
    public Connector(Config config) {
        this._config = config;
    }
    public static JSONObject generateJsonData() {
        JSONObject jsonData = new JSONObject();
        return jsonData;
    }
    public JSONObject HTTPPostRequest(String route, JSONObject jsonData) throws Exception {
        String body = jsonData.toString();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(String.format("http://%s:%s/%s", this._config.get("API_HOST"), this._config.get("API_PORT"), route)))
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
                .uri(URI.create(String.format("http://%s:%s/%s", this._config.get("API_HOST"), this._config.get("API_PORT"), route)))
                .headers("Content-Type", "text/plain;charset=UTF-8")
                .PUT(HttpRequest.BodyPublishers.ofString(body))
                .build();
        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        JSONObject jsonResponse = new JSONObject(response.body());
        return jsonResponse;
    }
    public JSONObject HTTPDeleteRequest(String route, JSONObject jsonData) throws Exception {
        String body = jsonData.toString();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(String.format("http://%s:%s/%s", this._config.get("API_HOST"), this._config.get("API_PORT"), route)))
                .headers("Content-Type", "text/plain;charset=UTF-8")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        JSONObject jsonResponse = new JSONObject(response.body());
        return jsonResponse;
    }
}
