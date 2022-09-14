package edu.lysak.hypermetro;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Error! Such a file doesn't exist!");
            return;
        }

        try {
            LinkedList<String> stations = getStations(args[0]);
            printStations(stations);
        } catch (IOException e) {
            System.out.println("Error! Such a file doesn't exist!");
        }
    }

    public static LinkedList<String> getStations(String filePath) throws IOException {
        return new LinkedList<>(Files.readAllLines(Path.of(filePath)));
    }

    public static void printStations(LinkedList<String> stations) {
        String depot = "depot";
        stations.addFirst(depot);
        stations.addLast(depot);
        for (int i = 0; i < stations.size() - 2; i++) {
            System.out.printf("%s - %s - %s%n",
                    stations.get(i),
                    stations.get(i + 1),
                    stations.get(i + 2)
            );
        }
    }
}
