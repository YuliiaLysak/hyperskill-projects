package edu.lysak.client;

import com.google.gson.Gson;
import edu.lysak.protocol.Request;

public class RequestHandler {
    private final Gson gson;

    public RequestHandler(Gson gson) {
        this.gson = gson;
    }

    public String createRequest(String requestType, String key, String value) {
        Request request = new Request();
        request.setType(requestType);
        if (key != null) {
            request.setKey(key);
        }
        if (value != null) {
            request.setValue(value);
        }

        return gson.toJson(request);
    }
}
