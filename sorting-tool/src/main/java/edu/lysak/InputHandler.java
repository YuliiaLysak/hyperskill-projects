package edu.lysak;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class InputHandler {
    private static final String SORTING_TYPE = "-sortingType";
    private static final String DATA_TYPE = "-dataType";
    private static final String INPUT_FILE = "-inputFile";
    private static final String OUTPUT_FILE = "-outputFile";
    private static final String NATURAL = "natural";
    private static final String BY_COUNT = "byCount";
    private static final String DEFAULT_SORTING_TYPE = NATURAL;
    private static final String WORD = "word";
    private static final String LONG = "long";
    private static final String LINE = "line";
    private static final String DEFAULT_DATA_TYPE = WORD;
    private final Scanner scanner;

    public InputHandler(Scanner scanner) {
        this.scanner = scanner;
    }

    public void processInput(List<String> args) {
        checkArgs(args);
        String sortingType;
        String dataType;
        String inputFile;
        String outputFile;

        try {
            sortingType = getSortingType(args);
            dataType = getDataType(args);
            inputFile = getInputFile(args);
            outputFile = getOutputFile(args);
        } catch (NoSortingTypeException | NoDataTypeException
                | NoInputFileException | NoOutputFileException e) {
            System.out.println(e.getMessage());
            return;
        }

        List<String> inputData;
        if (inputFile != null) {
            inputData = readDataFromFile(inputFile);
        } else {
            inputData = readDataFromConsole(scanner);
        }

        if (outputFile != null) {
            writeToFile(outputFile, dataType, sortingType, inputData);
        } else {
            writeToConsole(dataType, sortingType, inputData);
        }
    }

    private void writeToFile(String outputFile, String dataType, String sortingType, List<String> inputData) {
        switch (dataType) {
            case "long":
                List<Long> numbers = convertToLong(inputData);
                sortAndPrintNumbersToFile(outputFile, numbers, sortingType);
                break;
            case "line":
                sortAndPrintLinesToFile(outputFile, inputData, sortingType);
                break;
            case "word":
                List<String> words = convertToWords(inputData);
                sortAndPrintWordsToFile(outputFile, words, sortingType);
                break;
            default:
                break;
        }
    }

    private void writeToConsole(String dataType, String sortingType, List<String> inputData) {
        switch (dataType) {
            case "long":
                List<Long> numbers = convertToLong(inputData);
                sortAndPrintNumbers(numbers, sortingType);
                break;
            case "line":
                sortAndPrintLines(inputData, sortingType);
                break;
            case "word":
                List<String> words = convertToWords(inputData);
                sortAndPrintWords(words, sortingType);
                break;
            default:
                break;
        }
    }

    private List<String> readDataFromFile(String inputFile) {
        try {
            return Files.readAllLines(Paths.get(inputFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<String> readDataFromConsole(Scanner scanner) {
        List<String> lines = new ArrayList<>();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            lines.add(line);
        }
        return lines;
    }

    private List<Long> convertToLong(List<String> inputData) {
        List<Long> numbers = new ArrayList<>();
        List<String> numbersString = convertToWords(inputData);

        for (String num : numbersString) {
            try {
                numbers.add(Long.parseLong(num));
            } catch (NumberFormatException e) {
                System.out.printf("\"%s\" is not a long. It will be skipped.%n", num);
            }
        }

        return numbers;
    }

    private List<String> convertToWords(List<String> inputData) {
        return inputData.stream()
                .map(line -> line.split("\\s+"))
                .flatMap(Arrays::stream)
                .collect(Collectors.toList());
    }

    private void sortAndPrintNumbers(List<Long> numbers, String sortingType) {
        System.out.printf("Total numbers: %d.%n", numbers.size());

        switch (sortingType) {
            case NATURAL:
                Collections.sort(numbers);
                System.out.print("Sorted data: ");
                numbers.forEach(n -> System.out.print(n + " "));
                break;
            case BY_COUNT:
                numbers.stream()
                        .collect(Collectors.toMap(
                                Function.identity(),
                                n -> 1,
                                Integer::sum
                        ))
                        .entrySet().stream()
                        .sorted(Map.Entry.<Long, Integer>comparingByValue()
                                .thenComparing(Map.Entry.comparingByKey()))
                        .forEach(n -> System.out.printf(
                                "%d: %d time(s), %d%%%n",
                                n.getKey(),
                                n.getValue(),
                                100 * n.getValue() / numbers.size())
                        );
                break;
            default:
                break;
        }
    }

    private void sortAndPrintNumbersToFile(String outputFile, List<Long> numbers, String sortingType) {
        File file = new File(outputFile);
        try (PrintWriter printWriter = new PrintWriter(file)) {
            printWriter.printf("Total numbers: %d.%n", numbers.size());
            switch (sortingType) {
                case NATURAL:
                    Collections.sort(numbers);
                    printWriter.print("Sorted data: ");
                    numbers.forEach(n -> printWriter.print(n + " "));
                    break;
                case BY_COUNT:
                    numbers.stream()
                            .collect(Collectors.toMap(
                                    Function.identity(),
                                    n -> 1,
                                    Integer::sum
                            ))
                            .entrySet().stream()
                            .sorted(Map.Entry.<Long, Integer>comparingByValue()
                                    .thenComparing(Map.Entry.comparingByKey()))
                            .forEach(n -> printWriter.printf(
                                    "%d: %d time(s), %d%%%n",
                                    n.getKey(),
                                    n.getValue(),
                                    100 * n.getValue() / numbers.size())
                            );
                    break;
                default:
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void sortAndPrintWords(List<String> words, String sortingType) {
        System.out.printf("Total words: %d.%n", words.size());

        switch (sortingType) {
            case NATURAL:
                Collections.sort(words);
                System.out.print("Sorted data: ");
                words.forEach(n -> System.out.print(n + " "));
                break;
            case BY_COUNT:
                words.stream()
                        .collect(Collectors.toMap(
                                Function.identity(),
                                n -> 1,
                                Integer::sum
                        ))
                        .entrySet().stream()
                        .sorted(Map.Entry.<String, Integer>comparingByValue()
                                .thenComparing(Map.Entry.comparingByKey()))
                        .forEach(n -> System.out.printf(
                                "%s: %d time(s), %d%%%n",
                                n.getKey(),
                                n.getValue(),
                                100 * n.getValue() / words.size())
                        );
                break;
            default:
                break;
        }
    }

    private void sortAndPrintWordsToFile(String outputFile, List<String> words, String sortingType) {
        File file = new File(outputFile);
        try (PrintWriter printWriter = new PrintWriter(file)) {
            printWriter.printf("Total words: %d.%n", words.size());
            switch (sortingType) {
                case NATURAL:
                    Collections.sort(words);
                    printWriter.print("Sorted data: ");
                    words.forEach(n -> printWriter.print(n + " "));
                    break;
                case BY_COUNT:
                    words.stream()
                            .collect(Collectors.toMap(
                                    Function.identity(),
                                    n -> 1,
                                    Integer::sum
                            ))
                            .entrySet().stream()
                            .sorted(Map.Entry.<String, Integer>comparingByValue()
                                    .thenComparing(Map.Entry.comparingByKey()))
                            .forEach(n -> printWriter.printf(
                                    "%s: %d time(s), %d%%%n",
                                    n.getKey(),
                                    n.getValue(),
                                    100 * n.getValue() / words.size())
                            );
                    break;
                default:
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sortAndPrintLines(List<String> lines, String sortingType) {
        System.out.printf("Total lines: %d.%n", lines.size());

        switch (sortingType) {
            case NATURAL:
                Collections.sort(lines);
                System.out.println("Sorted data: ");
                lines.forEach(System.out::println);
                break;
            case BY_COUNT:
                lines.stream()
                        .collect(Collectors.toMap(
                                Function.identity(),
                                n -> 1,
                                Integer::sum
                        ))
                        .entrySet().stream()
                        .sorted(Map.Entry.<String, Integer>comparingByValue()
                                .thenComparing(Map.Entry.comparingByKey()))
                        .forEach(n -> System.out.printf(
                                "%s: %d time(s), %d%%%n",
                                n.getKey(),
                                n.getValue(),
                                100 * n.getValue() / lines.size())
                        );
                break;
            default:
                break;
        }
    }

    private void sortAndPrintLinesToFile(String outputFile, List<String> lines, String sortingType) {
        File file = new File(outputFile);
        try (PrintWriter printWriter = new PrintWriter(file)) {
            printWriter.printf("Total lines: %d.%n", lines.size());
            switch (sortingType) {
                case NATURAL:
                    Collections.sort(lines);
                    printWriter.println("Sorted data: ");
                    lines.forEach(printWriter::println);
                    break;
                case BY_COUNT:
                    lines.stream()
                            .collect(Collectors.toMap(
                                    Function.identity(),
                                    n -> 1,
                                    Integer::sum
                            ))
                            .entrySet().stream()
                            .sorted(Map.Entry.<String, Integer>comparingByValue()
                                    .thenComparing(Map.Entry.comparingByKey()))
                            .forEach(n -> printWriter.printf(
                                    "%s: %d time(s), %d%%%n",
                                    n.getKey(),
                                    n.getValue(),
                                    100 * n.getValue() / lines.size())
                            );
                    break;
                default:
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkArgs(List<String> args) {
        args.stream()
                .filter(a -> !SORTING_TYPE.equals(a)
                        && !DATA_TYPE.equals(a)
                        && !INPUT_FILE.equals(a)
                        && !OUTPUT_FILE.equals(a)
                        && !NATURAL.equals(a)
                        && !WORD.equals(a)
                        && !LONG.equals(a)
                        && !LINE.equals(a)
                        && !BY_COUNT.equals(a)
                        && !a.contains(".")
                )
                .forEach(a -> System.out.printf("\"%s\" is not a valid parameter. It will be skipped.%n", a));
    }

    private String getSortingType(List<String> args) {
        if (args.contains(SORTING_TYPE)) {
            if (args.indexOf(SORTING_TYPE) + 1 >= args.size()) {
                throw new NoSortingTypeException();
            }
            return args.get(args.indexOf(SORTING_TYPE) + 1);
        }
        return DEFAULT_SORTING_TYPE;
    }

    private String getDataType(List<String> args) {
        if (args.contains(DATA_TYPE)) {
            if (args.indexOf(DATA_TYPE) + 1 >= args.size()) {
                throw new NoDataTypeException();
            }
            return args.get(args.indexOf(DATA_TYPE) + 1);
        }
        return DEFAULT_DATA_TYPE;
    }

    private String getInputFile(List<String> args) {
        if (args.contains(INPUT_FILE)) {
            if (args.indexOf(INPUT_FILE) + 1 >= args.size()) {
                throw new NoInputFileException();
            }
            return args.get(args.indexOf(INPUT_FILE) + 1);
        }
        return null;
    }

    private String getOutputFile(List<String> args) {
        if (args.contains(OUTPUT_FILE)) {
            if (args.indexOf(OUTPUT_FILE) + 1 >= args.size()) {
                throw new NoOutputFileException();
            }
            return args.get(args.indexOf(OUTPUT_FILE) + 1);
        }
        return null;
    }
}
