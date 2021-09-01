package edu.lysak.sorting2.writer;

import java.io.PrintStream;
import java.util.List;
import java.util.Map;

public interface DataWriter<T extends Comparable<T>> {

    void writeDataNatural(List<T> data);

    void writeDataByCount(List<Map.Entry<T, Integer>> data);

    default void writeByCount(PrintStream printStream, List<Map.Entry<T, Integer>> data) {
        data.forEach(n -> printStream.printf(
                "%s: %d time(s), %d%%%n",
                n.getKey(),
                n.getValue(),
                100 * n.getValue() / data.size()
        ));
    }
}
