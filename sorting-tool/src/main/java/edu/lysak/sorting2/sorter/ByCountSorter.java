package edu.lysak.sorting2.sorter;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ByCountSorter<T extends Comparable<T>> implements Sorter<T> {

    public List<Map.Entry<T, Integer>> sort(List<T> data) {
        return data.stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        n -> 1,
                        Integer::sum
                ))
                .entrySet().stream()
                .sorted(Map.Entry.<T, Integer>comparingByValue()
                        .thenComparing(Map.Entry.comparingByKey()))
                .collect(Collectors.toList());
    }
}
