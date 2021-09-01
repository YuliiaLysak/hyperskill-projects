package edu.lysak.sorting1;

import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(final String[] args) {
        Scanner scanner = new Scanner(System.in);
        InputHandler inputHandler = new InputHandler(scanner);
        inputHandler.processInput(Arrays.asList(args));
    }
}
