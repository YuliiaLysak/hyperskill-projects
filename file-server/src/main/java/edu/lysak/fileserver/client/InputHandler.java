package edu.lysak.fileserver.client;

import java.io.IOException;
import java.util.Scanner;

import static edu.lysak.fileserver.util.Constants.*;

public class InputHandler {

    private final Scanner scanner;
    private final ClientFileStorage clientFileStorage;

    public InputHandler(Scanner scanner, ClientFileStorage clientFileStorage) {
        this.scanner = scanner;
        this.clientFileStorage = clientFileStorage;
    }

    public void process() throws IOException {
        MyClientSocket client = new MyClientSocket(ADDRESS, PORT);
        System.out.println("Client started!");

        System.out.println("Enter action (1 - get a file, 2 - create a file, 3 - delete a file):");
        String action = scanner.nextLine();
        if ("exit".equals(action)) {
            System.out.println("The request was sent.");
            client.sendExitRequest("exit");
            return;
        }

        switch (action) {
            case "1" -> processGetRequest(client);
            case "2" -> processPutRequest(client);
            case "3" -> processDeleteRequest(client);
            default -> throw new IllegalArgumentException(action + " action is not supported");
        }
    }

    private void processGetRequest(MyClientSocket client) throws IOException {
        String option = getOption("get");
        String request = switch (option) {
            case "1" -> String.format("%s %s %s", GET, BY_NAME, getFileName());
            case "2" -> String.format("%s %s %s", GET, BY_ID, getFileId());
            default -> throw new IllegalArgumentException(option + " option is not supported");
        };

        byte[] fileBytes = client.sendGetRequest(request);
        System.out.println("The request was sent.");

        if (fileBytes != null) {
            System.out.println("The file was downloaded! Specify a name for it:");
            String localFileName = scanner.nextLine();
            clientFileStorage.addFile(localFileName, fileBytes);
            System.out.println("File saved on the hard drive!");
        } else {
            System.out.println("The response says that this file is not found!");
        }
    }

    private void processPutRequest(MyClientSocket client) throws IOException {
        System.out.println("Enter name of the file:");
        String fileName = scanner.nextLine();
        String fileExtension = fileName.substring(fileName.indexOf("."));
        byte[] file = clientFileStorage.getFileByName(fileName);

        System.out.println("Enter name of the file to be saved on server:");
        String fileNameOnServer = scanner.nextLine();
        String request = String.format("%s %s %s", PUT, fileNameOnServer, fileExtension);

        String response = client.sendPutRequest(request, file);
        System.out.println("The request was sent.");

        String[] responseParts = response.split(" ");
        String code = responseParts[0];
        if (SUCCESS.equals(code)) {
            System.out.println("Response says that file is saved! ID = " + responseParts[1]);
        } else if (FORBIDDEN.equals(code)) {
            System.out.println("The response says that creating the file was forbidden!");
        }
    }

    private void processDeleteRequest(MyClientSocket client) throws IOException {
        String option = getOption("delete");
        String request = switch (option) {
            case "1" -> String.format("%s %s %s", DELETE, BY_NAME, getFileName());
            case "2" -> String.format("%s %s %s", DELETE, BY_ID, getFileId());
            default -> throw new IllegalArgumentException(option + " option is not supported");
        };

        String response = client.sendDeleteRequest(request);
        System.out.println("The request was sent.");

        if (SUCCESS.equals(response)) {
            System.out.println("The response says that this file was deleted successfully!");
        } else if (NOT_FOUND.equals(response)) {
            System.out.println("The response says that this file is not found!");
        }
    }

    private String getFileName() {
        System.out.println("Enter name:");
        return scanner.nextLine();
    }

    private int getFileId() {
        System.out.println("Enter id:");
        return Integer.parseInt(scanner.nextLine());
    }

    private String getOption(String action) {
        System.out.printf("Do you want to %s the file by name or by id (1 - name, 2 - id):%n", action);
        return scanner.nextLine();
    }
}
