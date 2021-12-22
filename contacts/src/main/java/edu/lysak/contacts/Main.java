package edu.lysak.contacts;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        PhoneBook phoneBook = new PhoneBook(args);
        phoneBook.init();

        Scanner scanner = new Scanner(System.in);
        InputHandler inputHandler = new InputHandler(scanner, phoneBook);
        inputHandler.proceed();
    }
}
