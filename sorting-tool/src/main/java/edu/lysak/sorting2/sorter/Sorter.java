package edu.lysak.sorting2.sorter;

import java.util.List;

public interface Sorter<T extends Comparable<T>> {

    List sort(List<T> data);
}
