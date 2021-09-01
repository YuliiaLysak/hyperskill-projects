package edu.lysak.sorting2.writer;

import java.io.PrintStream;
import java.util.List;
import java.util.Map;

public class NumberWriter implements DataWriter<Long> {
    private final PrintStream printStream;

    public NumberWriter(PrintStream printStream) {
        this.printStream = printStream;
    }

    @Override
    public void writeDataNatural(List<Long> data) {
        printStream.printf("Total numbers: %d.%n", data.size());
        printStream.print("Sorted data: ");
        data.forEach(n -> printStream.print(n + " "));
    }

    @Override
    public void writeDataByCount(List<Map.Entry<Long, Integer>> data) {
        Integer size = data.stream()
                .map(Map.Entry::getValue)
                .reduce(0, Integer::sum);
        printStream.printf("Total numbers: %d.%n", size);
        writeByCount(printStream, data);
    }
}
