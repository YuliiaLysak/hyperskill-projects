package edu.lysak.fileserver.server;

import java.io.IOException;

public class Main {
    private static final String ADDRESS = "127.0.0.1";
    private static final int PORT = 23456;

    public static void main(String[] args) throws IOException {
        FileStorage fileStorage = new FileStorage();
        MyServerSocket server = new MyServerSocket(ADDRESS, PORT, fileStorage);
        server.start();
    }
}