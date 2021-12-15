package edu.lysak.server;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        JsonDatabase jsonDatabase = new JsonDatabase();
        InputHandler inputHandler = new InputHandler(scanner, jsonDatabase);
        inputHandler.process();
    }
}
