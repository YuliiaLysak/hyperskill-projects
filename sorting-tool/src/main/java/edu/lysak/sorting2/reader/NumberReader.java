package edu.lysak.sorting2.reader;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class NumberReader implements DataReader<Long> {
    private final Scanner scanner;

    public NumberReader(InputStream readStream) {
        scanner = new Scanner(readStream);
    }

    @Override
    public List<Long> readData() {
        List<String> lines = readLines(scanner);
        List<String> numbersString = lines.stream()
                .map(line -> line.split("\\s+"))
                .flatMap(Arrays::stream)
                .collect(Collectors.toList());

        List<Long> numbers = new ArrayList<>();
        for (String num : numbersString) {
            try {
                numbers.add(Long.parseLong(num));
            } catch (NumberFormatException e) {
                System.out.printf("\"%s\" is not a long. It will be skipped.%n", num);
            }
        }

        return numbers;
    }
}
