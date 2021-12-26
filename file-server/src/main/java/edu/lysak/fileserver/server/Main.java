package edu.lysak.fileserver.server;

import java.io.IOException;

import static edu.lysak.fileserver.util.Constants.ADDRESS;
import static edu.lysak.fileserver.util.Constants.PORT;

public class Main {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        FileStorage fileStorage = new FileStorage();
        fileStorage.init();
        ResponseHandler responseHandler = new ResponseHandler(fileStorage);
        MyServerSocket server = new MyServerSocket(ADDRESS, PORT, responseHandler);
        server.start();
    }
}