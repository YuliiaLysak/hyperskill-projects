package edu.lysak.sorting2.reader;

import java.io.InputStream;
import java.util.List;
import java.util.Scanner;

public class LineReader implements DataReader<String> {
    private final Scanner scanner;

    public LineReader(InputStream readStream) {
        scanner = new Scanner(readStream);
    }

    @Override
    public List<String> readData() {
        return readLines(scanner);
    }
}
