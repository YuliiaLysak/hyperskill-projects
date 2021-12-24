package edu.lysak.fileserver.client;

import java.io.IOException;
import java.util.Scanner;

public class InputHandler {
    private static final String ADDRESS = "127.0.0.1";
    private static final int PORT = 23456;

    private final Scanner scanner;

    public InputHandler(Scanner scanner) {
        this.scanner = scanner;
    }

    public void process() throws IOException {
        MyClientSocket client = new MyClientSocket(ADDRESS, PORT);
        System.out.println("Client started!");

        System.out.println("Enter action (1 - get a file, 2 - create a file, 3 - delete a file):");
        String action = scanner.nextLine();
        if ("exit".equals(action)) {
            System.out.println("The request was sent.");
            client.sendRequest("exit");
            return;
        }
        System.out.println("Enter filename:");
        String fileName = scanner.nextLine();
        switch (action) {
            case "1" -> processGetRequest(client, fileName);
            case "2" -> processPutRequest(client, fileName);
            case "3" -> processDeleteRequest(client, fileName);
            default -> throw new IllegalArgumentException(action + " action is not supported");
        }
    }

    private void processGetRequest(MyClientSocket client, String fileName) throws IOException {
        String request = String.format("GET %s", fileName);
        String response = client.sendRequest(request);
        System.out.println("The request was sent.");

        int code = Integer.parseInt(response.substring(0, 3));
        if (code == 200) {
            System.out.printf("The content of the file is: %s%n", response.substring(4));
        } else if (code == 404) {
            System.out.println("The response says that the file was not found!");
        }
    }

    private void processPutRequest(MyClientSocket client, String fileName) throws IOException {
        System.out.println("Enter file content:");
        String fileContent = scanner.nextLine();
        String request = String.format("PUT %s %s", fileName, fileContent);
        String response = client.sendRequest(request);
        System.out.println("The request was sent.");

        int code = Integer.parseInt(response);
        if (code == 200) {
            System.out.println("The response says that file was created!");
        } else if (code == 403) {
            System.out.println("The response says that creating the file was forbidden!");
        }
    }

    private void processDeleteRequest(MyClientSocket client, String fileName) throws IOException {
        String request = String.format("DELETE %s", fileName);
        String response = client.sendRequest(request);
        System.out.println("The request was sent.");

        int code = Integer.parseInt(response);
        if (code == 200) {
            System.out.println("The response says that the file was successfully deleted!");
        } else if (code == 404) {
            System.out.println("The response says that the file was not found!");
        }
    }
}
