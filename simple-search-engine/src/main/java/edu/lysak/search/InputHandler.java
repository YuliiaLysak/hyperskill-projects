package edu.lysak.search;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class InputHandler {
    private final Scanner scanner;
    private final SearchEngine searchEngine;

    public InputHandler(Scanner scanner, SearchEngine searchEngine) {
        this.scanner = scanner;
        this.searchEngine = searchEngine;
    }

    public void proceed(String fileName) throws IOException {
        List<String> people = getDataFromFile(fileName);

        int option;
        do {
            printMenu();
            option = Integer.parseInt(scanner.nextLine());
            switch (option) {
                case 0 -> System.out.println("\nBye!");
                case 1 -> provideSearch(people);
                case 2 -> printAllPeople(people);
                default -> System.out.println("\nIncorrect option! Try again.");
            }
        } while (option != 0);
    }

    private void provideSearch(List<String> people) {
        Strategy strategy = getStrategy();
        List<String> searchData = getSearchData();
        List<Integer> resultIndexes = searchEngine.findMatches(strategy, people, searchData);
        if (resultIndexes.isEmpty()) {
            System.out.println("No matching people found.");
            return;
        }
        printSearchResult(getSearchResult(resultIndexes, people));
    }

    private Strategy getStrategy() {
        System.out.println("\nSelect a matching strategy: ALL, ANY, NONE");
        String s = scanner.nextLine().toUpperCase();
        return Strategy.valueOf(s);
    }

    private List<String> getSearchResult(List<Integer> indexes, List<String> people) {
        List<String> result = new ArrayList<>();
        for (Integer index : indexes) {
            result.add(people.get(index));
        }
        return result;
    }

    private List<String> getSearchData() {
        System.out.println("\nEnter a name or email to search all suitable people.");
        return Arrays.asList(scanner.nextLine().toLowerCase().split("\\s+"));
    }

    private void printAllPeople(List<String> lines) {
        System.out.println("\n=== List of people ===");
        lines.forEach(System.out::println);
        System.out.println();
    }

    private void printSearchResult(List<String> foundPeople) {
        System.out.printf("%n%d persons found:%n", foundPeople.size());
        foundPeople.forEach(System.out::println);
        System.out.println();
    }

    private void printMenu() {
        System.out.println("=== Menu ===");
        System.out.println("1. Find a person");
        System.out.println("2. Print all people");
        System.out.println("0. Exit");
    }

    private List<String> getDataFromFile(String fileName) throws IOException {
        return Files.readAllLines(Path.of(fileName));
    }
}
