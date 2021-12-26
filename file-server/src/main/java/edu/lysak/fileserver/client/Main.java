package edu.lysak.fileserver.client;

import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        ClientFileStorage clientFileStorage = new ClientFileStorage();
        InputHandler inputHandler = new InputHandler(scanner, clientFileStorage);
        inputHandler.process();
    }
}
