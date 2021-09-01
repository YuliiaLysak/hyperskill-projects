package edu.lysak.sorting2;

import java.io.FileNotFoundException;
import java.util.Arrays;

public class Main {
    public static void main(final String[] args) throws FileNotFoundException {
        try {
            Configurator configurator = new Configurator(Arrays.asList(args));
            SortingTool sortingTool = configurator.provideSortingTool();
            sortingTool.proceed();
        } catch (IllegalArgumentException e) {
            // add logs
        }
    }
}
