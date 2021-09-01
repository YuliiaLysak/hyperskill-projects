package edu.lysak.sorting2.writer;

import java.io.PrintStream;
import java.util.List;
import java.util.Map;

public class WordWriter implements DataWriter<String> {
    private final PrintStream printStream;

    public WordWriter(PrintStream printStream) {
        this.printStream = printStream;
    }

    @Override
    public void writeDataNatural(List<String> data) {
        printStream.printf("Total words: %d.%n", data.size());
        printStream.print("Sorted data: ");
        data.forEach(n -> printStream.print(n + " "));
    }

    @Override
    public void writeDataByCount(List<Map.Entry<String, Integer>> data) {
        Integer size = data.stream()
                .map(Map.Entry::getValue)
                .reduce(0, Integer::sum);
        printStream.printf("Total words: %d.%n", size);
        writeByCount(printStream, data);
    }
}
