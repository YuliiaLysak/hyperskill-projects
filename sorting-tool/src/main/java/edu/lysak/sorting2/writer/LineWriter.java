package edu.lysak.sorting2.writer;

import java.io.PrintStream;
import java.util.List;
import java.util.Map;

public class LineWriter implements DataWriter<String> {
    private final PrintStream printStream;

    public LineWriter(PrintStream printStream) {
        this.printStream = printStream;
    }

    @Override
    public void writeDataNatural(List<String> data) {
        printStream.printf("Total lines: %d.%n", data.size());
        printStream.println("Sorted data: ");
        data.forEach(printStream::println);
    }

    @Override
    public void writeDataByCount(List<Map.Entry<String, Integer>> data) {
        Integer size = data.stream()
                .map(Map.Entry::getValue)
                .reduce(0, Integer::sum);
        printStream.printf("Total lines: %d.%n", size);
        writeByCount(printStream, data);
    }
}
