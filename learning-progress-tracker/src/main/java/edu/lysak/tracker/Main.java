package edu.lysak.tracker;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        StudentService studentService = new StudentService();
        InputHandler inputHandler = new InputHandler(scanner, studentService);
        inputHandler.process();
    }
}
