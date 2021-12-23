package edu.lysak.server;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class InputHandler {
    private final Scanner scanner;
    private final Set<String> files = new HashSet<>();

    public InputHandler(Scanner scanner) {
        this.scanner = scanner;
    }

    public void process() {
        while (true) {
            String input = scanner.nextLine();
            if ("exit".equals(input)) {
                return;
            }
            String[] commandLine = input.split(" ");
            String command = commandLine[0];
            String fileName = commandLine[1];
            switch (command) {
                case "add":
                    boolean isAdded = false;
                    if (isCorrectFile(fileName)) {
                        isAdded = files.add(fileName);
                    }

                    if (isAdded) {
                        System.out.printf("The file %s added successfully%n", fileName);
                    } else {
                        System.out.printf("Cannot add the file %s%n", fileName);
                    }
                    break;
                case "get":
                    String file = getFile(fileName);
                    if (file != null) {
                        System.out.printf("The file %s was sent%n", fileName);
                    } else {
                        System.out.printf("The file %s not found%n", fileName);
                    }
                    break;
                case "delete":
                    if (files.remove(fileName)) {
                        System.out.printf("The file %s was deleted%n", fileName);
                    } else {
                        System.out.printf("The file %s not found%n", fileName);
                    }
                    break;
                default:
                    throw new IllegalArgumentException(command + " command is not supported");
            }
        }
    }

    private String getFile(String fileName) {
        return files.stream()
                .filter(it -> it.equals(fileName))
                .findFirst()
                .orElse(null);
    }

    private boolean isCorrectFile(String fileName) {
        for (int i = 1; i < 11; i++) {
            String file = "file" + i;
            if (file.equals(fileName)) {
                return true;
            }
        }
        return false;
    }
}
