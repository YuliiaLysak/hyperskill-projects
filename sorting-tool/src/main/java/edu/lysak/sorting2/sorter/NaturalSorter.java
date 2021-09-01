package edu.lysak.sorting2.sorter;

import java.util.Collections;
import java.util.List;

public class NaturalSorter<T extends Comparable<T>> implements Sorter<T> {

    public List<T> sort(List<T> data) {
        Collections.sort(data);
        return data;
    }
}
