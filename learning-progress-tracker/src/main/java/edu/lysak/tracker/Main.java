package edu.lysak.tracker;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Learning Progress Tracker");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String command = scanner.nextLine();
            if (command.isBlank()) {
                System.out.println("No input.");
            } else if ("exit".equals(command)) {
                System.out.println("Bye!");
                break;
            } else {
                System.out.println("Error: unknown command!");
            }
        }
    }
}
