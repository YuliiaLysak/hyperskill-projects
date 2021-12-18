package edu.lysak.client;

import com.beust.jcommander.JCommander;
import com.google.gson.Gson;

import java.io.IOException;

public class Main {
    private static final String ADDRESS = "127.0.0.1";
    private static final int PORT = 34522;

    public static void main(String[] args) throws IOException {
        Arguments arguments = new Arguments();
        JCommander.newBuilder()
                .addObject(arguments)
                .build()
                .parse(args);

        RequestHandler requestHandler = new RequestHandler(new Gson());
        MyClientSocket client = new MyClientSocket(requestHandler, ADDRESS, PORT);
        client.run(arguments.getRequestType(), arguments.getKey(), arguments.getValue());
    }
}
