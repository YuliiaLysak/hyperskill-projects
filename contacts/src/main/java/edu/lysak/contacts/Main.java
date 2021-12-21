package edu.lysak.contacts;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        PhoneBook phoneBook = new PhoneBook();
        InputHandler inputHandler = new InputHandler(scanner, phoneBook);
        inputHandler.proceed();
    }
}
