package edu.lysak.numbers;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        NumberChecker checker = new NumberChecker();
        InputHandler inputHandler = new InputHandler(scanner, checker);
        inputHandler.proceed();
    }
}
