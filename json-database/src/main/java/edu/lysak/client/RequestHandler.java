package edu.lysak.client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class RequestHandler {
    private static final String INPUT_FILE_DIR = "json-database/src/main/resources/client/data/";

    private final Gson gson;

    public RequestHandler(Gson gson) {
        this.gson = gson;
    }

    public String createRequest(Arguments arguments) throws IOException {
        String inputFile = arguments.getInputFile();

        JsonObject request;
        if (inputFile == null) {
            request = createRequestFromArgs(arguments);
        } else {
            request = createRequestFromFile(inputFile);
        }

        return gson.toJson(request);
    }

    private JsonObject createRequestFromArgs(Arguments arguments) {
        String requestType = arguments.getRequestType();
        String key = arguments.getKey();
        String value = arguments.getValue();

        JsonObject request = new JsonObject();
        request.addProperty("type", requestType);
        if (key != null) {
            request.addProperty("key", key);
        }
        if (value != null) {
            request.addProperty("value", value);
        }
        return request;
    }

    private JsonObject createRequestFromFile(String inputFile) throws IOException {
        String fileContent = new String(Files.readAllBytes(Path.of(INPUT_FILE_DIR + inputFile)));
        return JsonParser.parseString(fileContent).getAsJsonObject();
    }
}
