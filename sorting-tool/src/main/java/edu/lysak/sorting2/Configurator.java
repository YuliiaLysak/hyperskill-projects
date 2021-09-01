package edu.lysak.sorting2;

import edu.lysak.sorting2.reader.DataReader;
import edu.lysak.sorting2.reader.LineReader;
import edu.lysak.sorting2.reader.NumberReader;
import edu.lysak.sorting2.reader.WordReader;
import edu.lysak.sorting2.sorter.ByCountSorter;
import edu.lysak.sorting2.sorter.NaturalSorter;
import edu.lysak.sorting2.sorter.Sorter;
import edu.lysak.sorting2.writer.DataWriter;
import edu.lysak.sorting2.writer.LineWriter;
import edu.lysak.sorting2.writer.NumberWriter;
import edu.lysak.sorting2.writer.WordWriter;

import java.io.*;
import java.util.List;

public class Configurator {
    /*
    Possible arguments example:
    -sortingType byCount -inputFile sorting-tool/in.txt => (input file is defined)
    -sortingType byCount -inputFile sorting-tool/in.txt sorting-tool/-outputFile out.txt => (input and output files are defined)
    -sortingType natural -dataType long => (sorting numbers naturally without errors)
    -sortingType byCount -dataType long => (sorting numbers by count without errors)
    -sortingType natural -dataType long
    -dataType word -sortingType byCount

    Wrong arguments example:
    -sortingType => (missing sorting type)
    -dataType => (missing data type)
    -dataType long -sortingType natural -abc -def => (invalid arguments and input value)
     */
    private final List<String> args;

    public Configurator(List<String> args) {
        this.args = args;
    }

    public SortingTool provideSortingTool() throws FileNotFoundException {
        checkArgs();
        String dataType = getDataType();
        String sortingType = getSortingType();
        return SortingTool.builder()
                .sortingType(sortingType)
                .dataReader(getDataReader(dataType, getInputStream()))
                .dataWriter(getDataWriter(dataType, getPrintStream()))
                .sorter(getSorter(sortingType))
                .build();
    }

    private Sorter getSorter(String sortingType) {
        switch (sortingType) {
            case "natural":
                return new NaturalSorter();
            case "byCount":
                return new ByCountSorter();
        }
        return null;
    }

    private InputStream getInputStream() throws FileNotFoundException {
        String inputFile = getInputFile();
        if (inputFile != null) {
            return new FileInputStream(inputFile);
        } else {
            return System.in;
        }
    }

    private PrintStream getPrintStream() throws FileNotFoundException {
        String outputFile = getOutputFile();
        if (outputFile != null) {
            return new PrintStream(new FileOutputStream(outputFile));
        } else {
            return System.out;
        }
    }

    private void checkArgs() {
        args.stream()
                .filter(a -> !Constants.SORTING_TYPE.equals(a)
                        && !Constants.DATA_TYPE.equals(a)
                        && !Constants.INPUT_FILE.equals(a)
                        && !Constants.OUTPUT_FILE.equals(a)
                        && !Constants.NATURAL.equals(a)
                        && !Constants.WORD.equals(a)
                        && !Constants.LONG.equals(a)
                        && !Constants.LINE.equals(a)
                        && !Constants.BY_COUNT.equals(a)
                        && !a.contains(".")
                )
                .forEach(a -> System.out.printf(
                        "\"%s\" is not a valid parameter. It will be skipped.%n", a)
                );

        if (args.indexOf(Constants.SORTING_TYPE) + 1 >= args.size()) {
            System.out.println("No sorting type defined!");
            throw new IllegalArgumentException();
        }

        if (args.indexOf(Constants.DATA_TYPE) + 1 >= args.size()) {
            System.out.println("No data type defined!");
            throw new IllegalArgumentException();
        }

        if (args.indexOf(Constants.INPUT_FILE) + 1 >= args.size()) {
            System.out.println("No input file defined!");
            throw new IllegalArgumentException();
        }

        if (args.indexOf(Constants.OUTPUT_FILE) + 1 >= args.size()) {
            System.out.println("No output file defined!");
            throw new IllegalArgumentException();
        }
    }

    private String getSortingType() {
        if (args.contains(Constants.SORTING_TYPE)) {
            return args.get(args.indexOf(Constants.SORTING_TYPE) + 1);
        }
        return Constants.DEFAULT_SORTING_TYPE;
    }

    private String getDataType() {
        if (args.contains(Constants.DATA_TYPE)) {
            return args.get(args.indexOf(Constants.DATA_TYPE) + 1);
        }
        return Constants.DEFAULT_DATA_TYPE;
    }

    private String getInputFile() {
        if (args.contains(Constants.INPUT_FILE)) {
            return args.get(args.indexOf(Constants.INPUT_FILE) + 1);
        }
        return null;
    }

    private String getOutputFile() {
        if (args.contains(Constants.OUTPUT_FILE)) {
            return args.get(args.indexOf(Constants.OUTPUT_FILE) + 1);
        }
        return null;
    }

    private DataWriter getDataWriter(String dataType, PrintStream printStream) {
        switch (dataType) {
            case "long":
                return new NumberWriter(printStream);
            case "line":
                return new LineWriter(printStream);
            case "word":
                return new WordWriter(printStream);
        }
        return null;
    }

    private DataReader getDataReader(String dataType, InputStream readStream) {
        switch (dataType) {
            case "long":
                return new NumberReader(readStream);
            case "line":
                return new LineReader(readStream);
            case "word":
                return new WordReader(readStream);
        }
        return null;
    }
}
