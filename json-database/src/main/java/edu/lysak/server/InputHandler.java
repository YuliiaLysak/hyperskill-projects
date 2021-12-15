package edu.lysak.server;

import java.util.Scanner;

public class InputHandler {
    private final Scanner scanner;
    private final JsonDatabase jsonDatabase;

    public InputHandler(Scanner scanner, JsonDatabase jsonDatabase) {
        this.scanner = scanner;
        this.jsonDatabase = jsonDatabase;
    }

    public void process() {
        while (scanner.hasNext()) {
            String command = scanner.nextLine();
            if ("exit".equals(command)) {
                break;
            }
            if (command.startsWith("set")) {
                executeSetCommand(command);
            } else if (command.startsWith("get")) {
                executeGetCommand(command);
            } else if (command.startsWith("delete")) {
                executeDeleteCommand(command);
            }

        }
    }

    private void executeSetCommand(String command) {
        String s = command.substring("set ".length());
        int index = Integer.parseInt(s.substring(0, s.indexOf(" ")));
        String text = s.substring(s.indexOf(" ") + 1);
        if (jsonDatabase.set(index, text)) {
            printOk();
        } else {
            printError();
        }
    }

    private void executeGetCommand(String command) {
        int index = getIndex(command);
        String text = jsonDatabase.get(index);
        if (text != null) {
            System.out.println(text);
        } else {
            printError();
        }
    }

    private void executeDeleteCommand(String command) {
        int index = getIndex(command);
        if (jsonDatabase.delete(index)) {
            printOk();
        } else {
            printError();
        }
    }

    private int getIndex(String command) {
        String[] s = command.split(" ");
        return Integer.parseInt(s[1]);
    }

    private void printOk() {
        System.out.println("OK");
    }

    private void printError() {
        System.out.println("ERROR");
    }
}
