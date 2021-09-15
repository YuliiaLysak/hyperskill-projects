package edu.lysak.advisor;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class InputHandler {
    private static int currentPage;
    private static List<?> result;

    private final Scanner scanner;
    private final AuthHttpServer server;
    private final OutputHandler outputHandler;

    public InputHandler(Scanner scanner, AuthHttpServer server, OutputHandler outputHandler) {
        this.scanner = scanner;
        this.server = server;
        this.outputHandler = outputHandler;
    }

    public void proceed() throws IOException, InterruptedException {
        String userRequest;
        boolean accessGranted = false;
        do {
            userRequest = scanner.nextLine();
            if ("auth".equals(userRequest)) {
                server.provideAuthentication();
                accessGranted = true;
                continue;
            }

            if (!accessGranted) {
                outputHandler.printAuthWarning();
                continue;
            }

            switch (userRequest) {
                case "featured":
                    result = server.makeRequestForFeaturedPlaylists();
                    currentPage = 1;
                    break;
                case "new":
                    result = server.makeRequestForNewReleases();
                    currentPage = 1;
                    break;
                case "categories":
                    result = server.makeRequestForCategories();
                    currentPage = 1;
                    break;
                case "prev":
                    if (outputHandler.isValidPrevPage(currentPage)) {
                        currentPage--;
                    } else {
                        outputHandler.printPageWarning();
                        continue;
                    }
                    break;
                case "next":
                    if (outputHandler.isValidNextPage(currentPage, result.size())) {
                        currentPage++;
                    } else {
                        outputHandler.printPageWarning();
                        continue;
                    }
                    break;
                case "exit":
                    continue;
            }

            if (userRequest.contains("playlists")) {
                try {
                    result = server.makeRequestForCategoryByName(userRequest.substring(10));
                    currentPage = 1;
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
            }

            if (result != null) {
                outputHandler.printList(result, currentPage);
            }

        } while (!"exit".equals(userRequest));
    }
}
