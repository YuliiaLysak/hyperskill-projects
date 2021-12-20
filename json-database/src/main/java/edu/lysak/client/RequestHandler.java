package edu.lysak.client;

import com.google.gson.Gson;
import edu.lysak.protocol.Request;

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

        Request request;
        if (inputFile == null) {
            request = createRequestFromArgs(arguments);
        } else {
            request = createRequestFromFile(inputFile);
        }

        return gson.toJson(request);
    }

    private Request createRequestFromArgs(Arguments arguments) {
        String requestType = arguments.getRequestType();
        String key = arguments.getKey();
        String value = arguments.getValue();

        Request request = new Request();
        request.setType(requestType);
        if (key != null) {
            request.setKey(key);
        }
        if (value != null) {
            request.setValue(value);
        }
        return request;
    }

    private Request createRequestFromFile(String inputFile) throws IOException {
        String fileContent = new String(Files.readAllBytes(Path.of(INPUT_FILE_DIR + inputFile)));
        return gson.fromJson(fileContent, Request.class);
    }
}
