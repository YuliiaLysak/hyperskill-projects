package edu.lysak.server;

import com.google.gson.Gson;

import java.io.IOException;

public class Main {
    private static final String ADDRESS = "127.0.0.1";
    private static final int PORT = 34522;

    public static void main(String[] args) throws IOException {
        Gson gson = new Gson();
        JsonDatabase jsonDatabase = new JsonDatabase(gson);
        ResponseHandler responseHandler = new ResponseHandler(jsonDatabase, gson);
        MyServerSocket server = new MyServerSocket(responseHandler, ADDRESS, PORT);
        server.run();
    }
}
