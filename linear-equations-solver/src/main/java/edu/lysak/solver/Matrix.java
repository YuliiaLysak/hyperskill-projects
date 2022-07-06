package edu.lysak.solver;

import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

public class Matrix implements Iterable<Row> {

    private final Row[] matrix;
    private int currentIndex = -1;

    public Matrix(int size) {
        if (size < 0) {
            throw new IllegalArgumentException("Length of Matrix shouldn't be negative");
        }
        matrix = new Row[size];
    }

    public void add(Row row) {
        if (currentIndex >= matrix.length) {
            throw new ArrayIndexOutOfBoundsException("Trying to insert data at index " + currentIndex + " while length of Matrix is " + matrix.length);
        }

        matrix[++currentIndex] = row;
    }

    public Row getAt(int index) {
        if (index >= matrix.length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return matrix[index];
    }

    public int size() {
        return matrix.length;
    }

    @Override
    public Iterator<Row> iterator() {
        AtomicInteger index = new AtomicInteger();
        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                return index.get() < matrix.length;
            }

            @Override
            public Row next() {
                return matrix[index.getAndIncrement()];
            }
        };
    }
}
