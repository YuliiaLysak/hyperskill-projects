package edu.lysak.fileserver.client;

import java.io.IOException;
import java.util.Scanner;

import static edu.lysak.fileserver.util.Constants.CLIENT_STORAGE_FOLDER;

public class Main {

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        ClientFileStorage clientFileStorage = new ClientFileStorage(CLIENT_STORAGE_FOLDER);
        InputHandler inputHandler = new InputHandler(scanner, clientFileStorage);
        inputHandler.process();
    }
}
