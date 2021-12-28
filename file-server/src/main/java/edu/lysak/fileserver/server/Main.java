package edu.lysak.fileserver.server;

import java.io.IOException;

import static edu.lysak.fileserver.util.Constants.ADDRESS;
import static edu.lysak.fileserver.util.Constants.PORT;
import static edu.lysak.fileserver.util.Constants.SERVER_STORAGE_FOLDER;

public class Main {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        FileStorage fileStorage = new FileStorage("identifiers.txt", SERVER_STORAGE_FOLDER);
        fileStorage.init();
        ResponseHandler responseHandler = new ResponseHandler(fileStorage);
        MyServerSocket server = new MyServerSocket(ADDRESS, PORT, responseHandler);
        server.start();
    }
}