package edu.lysak.solver;

public class Row {

    private final double[] row;
    int currentIndex = -1;


    public Row(int size) {
        if (size < 0) {
            throw new IllegalArgumentException("Length of Row shouldn't be negative");
        }
        row = new double[size];
    }

    public void add(double number) {
        if (currentIndex >= row.length) {
            throw new ArrayIndexOutOfBoundsException("Trying to insert data at index " + currentIndex + " while length of Row is " + row.length);
        }
        row[++currentIndex] = number;
    }

    public double getAt(int index) {
        if (index >= row.length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return row[index];
    }

    public int size() {
        return row.length;
    }

    public void update(int index, double newValue) {
        if (index >= row.length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        row[index] = newValue;
    }
}
