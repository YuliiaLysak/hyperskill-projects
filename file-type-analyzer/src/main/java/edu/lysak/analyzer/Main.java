package edu.lysak.analyzer;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        long start = System.nanoTime();
        FileTypeAnalyzer fileTypeAnalyzer = new FileTypeAnalyzer();
        String result = fileTypeAnalyzer.analyzeFileType(args[0], args[1], args[2], args[3]);
        System.out.println(result);
        long end = System.nanoTime();
        double seconds = (double) (end - start) / 1_000_000_000;
        System.out.printf("It took %.3f seconds", seconds);
    }
}
