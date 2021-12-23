package edu.lysak.server;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        InputHandler inputHandler = new InputHandler(new Scanner(System.in));
        inputHandler.process();
    }
}