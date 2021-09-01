package edu.lysak.sorting2.reader;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public interface DataReader<T extends Comparable<T>> {

    default List<String> readLines(Scanner scanner) {
        List<String> lines = new ArrayList<>();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            lines.add(line);
        }
        return lines;
    }

    List<T> readData();
}
