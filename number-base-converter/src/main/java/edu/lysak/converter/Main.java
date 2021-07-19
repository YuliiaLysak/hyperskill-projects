package edu.lysak.converter;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Converter converter = new Converter();
        InputHandler inputHandler = new InputHandler(scanner, converter);
        inputHandler.proceed();
    }
}
