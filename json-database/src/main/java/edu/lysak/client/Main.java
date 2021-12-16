package edu.lysak.client;

import java.io.IOException;

public class Main {
    private static final String ADDRESS = "127.0.0.1";
    private static final int PORT = 34522;

    public static void main(String[] args) throws IOException {
        MyClientSocket client = new MyClientSocket(ADDRESS, PORT);
        client.run();
    }
}
