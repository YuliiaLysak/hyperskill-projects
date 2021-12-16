package edu.lysak.server;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    private static final String ADDRESS = "127.0.0.1";
    private static final int PORT = 34522;

    public static void main(String[] args) throws IOException {
//        Scanner scanner = new Scanner(System.in);
//        JsonDatabase jsonDatabase = new JsonDatabase();
//        InputHandler inputHandler = new InputHandler(scanner, jsonDatabase);
//        inputHandler.process();

        MyServerSocket server = new MyServerSocket(ADDRESS, PORT);
        server.run();
    }
}
