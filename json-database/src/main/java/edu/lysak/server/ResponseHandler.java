package edu.lysak.server;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;

public class ResponseHandler {
    private static final String TYPE = "type";
    private static final String KEY = "key";
    private static final String VALUE = "value";
    private static final String RESPONSE = "response";
    private static final String OK = "OK";
    private static final String ERROR = "ERROR";
    private static final String REASON = "reason";
    private static final String NO_SUCH_KEY = "No such key";

    private final JsonDatabase jsonDatabase;
    private final Gson gson;

    public ResponseHandler(JsonDatabase jsonDatabase, Gson gson) {
        this.jsonDatabase = jsonDatabase;
        this.gson = gson;
    }

    public JsonObject getRequest(String command) {
        return gson.fromJson(command, JsonObject.class);
    }

    public String getJsonResponse(JsonObject request) throws IOException {
        JsonObject response = new JsonObject();
        switch (request.get(TYPE).getAsString()) {
            case "exit" -> response.addProperty(RESPONSE, OK);
            case "set" -> executeSetCommand(request, response);
            case "get" -> executeGetCommand(request, response);
            case "delete" -> executeDeleteCommand(request, response);
            default -> throw new IllegalArgumentException(
                    String.format("Command %s is not supported", request.get(TYPE))
            );
        }
        return gson.toJson(response);
    }

    private void executeSetCommand(JsonObject request, JsonObject response) throws IOException {
        JsonArray keys = getKeysAsJsonArray(request);
        jsonDatabase.set(keys, request.get(VALUE));
        response.addProperty(RESPONSE, OK);
    }

    private void executeGetCommand(JsonObject request, JsonObject response) throws IOException {
        JsonArray keys = getKeysAsJsonArray(request);
        JsonElement value = jsonDatabase.get(keys);
        if (value != null) {
            response.addProperty(RESPONSE, OK);
            response.add(VALUE, value);
        } else {
            response.addProperty(RESPONSE, ERROR);
            response.addProperty(REASON, NO_SUCH_KEY);
        }
    }

    private void executeDeleteCommand(JsonObject request, JsonObject response) throws IOException {
        JsonArray keys = getKeysAsJsonArray(request);
        if (jsonDatabase.delete(keys)) {
            response.addProperty(RESPONSE, OK);
        } else {
            response.addProperty(RESPONSE, ERROR);
            response.addProperty(REASON, NO_SUCH_KEY);
        }
    }

    private JsonArray getKeysAsJsonArray(JsonObject request) {
        JsonArray keys;
        if (!request.get(KEY).isJsonArray()) {
            keys = new JsonArray();
            keys.add(request.get(KEY));
        } else {
            keys = request.get(KEY).getAsJsonArray();
        }
        return keys;
    }
}
