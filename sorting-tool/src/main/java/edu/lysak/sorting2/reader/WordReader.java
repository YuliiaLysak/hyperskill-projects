package edu.lysak.sorting2.reader;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class WordReader implements DataReader<String> {
    private final Scanner scanner;

    public WordReader(InputStream readStream) {
        scanner = new Scanner(readStream);
    }

    @Override
    public List<String> readData() {
        return readLines(scanner).stream()
                .map(line -> line.split("\\s+"))
                .flatMap(Arrays::stream)
                .collect(Collectors.toList());
    }
}
