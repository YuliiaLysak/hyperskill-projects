package edu.lysak.server;

import com.google.gson.Gson;
import edu.lysak.protocol.Request;
import edu.lysak.protocol.Response;

import java.io.IOException;

public class ResponseHandler {
    private static final String OK = "OK";
    private static final String ERROR = "ERROR";

    private final JsonDatabase jsonDatabase;
    private final Gson gson;

    public ResponseHandler(JsonDatabase jsonDatabase, Gson gson) {
        this.jsonDatabase = jsonDatabase;
        this.gson = gson;
    }

    public String getJsonResponse(Request request) throws IOException {
        Response response = new Response();
        switch (request.getType()) {
            case "exit" -> response.setResponse(OK);
            case "set" -> executeSetCommand(request, response);
            case "get" -> executeGetCommand(request, response);
            case "delete" -> executeDeleteCommand(request, response);
            default -> throw new IllegalArgumentException(
                    String.format("Command %s is not supported", request.getType())
            );
        }
        return gson.toJson(response);
    }

    public Request getRequest(String command) {
        return gson.fromJson(command, Request.class);
    }

    private void executeSetCommand(Request request, Response response) throws IOException {
        jsonDatabase.set(request.getKey(), request.getValue());
        response.setResponse(OK);
    }

    private void executeGetCommand(Request request, Response response) throws IOException {
        String value = jsonDatabase.get(request.getKey());
        if (value != null) {
            response.setResponse(OK);
            response.setValue(value);
        } else {
            response.setResponse(ERROR);
            response.setReason("No such key");
        }
    }

    private void executeDeleteCommand(Request request, Response response) throws IOException {
        if (jsonDatabase.delete(request.getKey())) {
            response.setResponse(OK);
        } else {
            response.setResponse(ERROR);
            response.setReason("No such key");
        }
    }
}
