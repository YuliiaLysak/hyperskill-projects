package edu.lysak.advisor;

import java.util.List;

public class OutputHandler {
    private final int pageSize;

    public OutputHandler(int pageSize) {
        this.pageSize = pageSize;
    }

    public void printList(List<?> list, int currentPage) {
        int totalPages = (int) Math.ceil((double) list.size() / pageSize);
        getPage(list, currentPage).forEach(System.out::println);
        System.out.printf("---PAGE %d OF %d---%n", currentPage, totalPages);
    }

    private <T> List<T> getPage(List<T> sourceList, int page) {
        int fromIndex = (page - 1) * pageSize;
        return sourceList.subList(fromIndex, Math.min(fromIndex + pageSize, sourceList.size()));
    }

    public void printLinkForGettingPermission(String accessServerPoint, String clientId, String redirectUri) {
        System.out.println("use this link to request the access code:");
        System.out.printf("%s/authorize?client_id=%s&redirect_uri=%s&response_type=code%n",
                accessServerPoint, clientId, redirectUri);
        System.out.println("waiting for code...");
    }

    public void printSuccess() {
        System.out.println("Success!");
    }

    public void printTextForToken() {
        System.out.println("making http request for access_token...");
    }

    public void printAuthWarning() {
        System.out.println("Please, provide access for application.");
    }

    public void printCodeReceived() {
        System.out.println("code received");
    }

    public boolean isValidPrevPage(int currentPage) {
        return currentPage > 1;
    }

    public boolean isValidNextPage(int currentPage, int listSize) {
        int totalPages = (int) Math.ceil((double) listSize / pageSize);
        return currentPage < totalPages;
    }

    public void printPageWarning() {
        System.out.println("No more pages.");
    }
}
