package edu.lysak.analyzer;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        FileTypeAnalyzer fileTypeAnalyzer = new FileTypeAnalyzer();
//        processFile(args, fileTypeAnalyzer);
//        processFilesAsync(args, fileTypeAnalyzer);
        processFilesAsyncWithDb(args, fileTypeAnalyzer);
    }

    private static void processFilesAsyncWithDb(String[] args, FileTypeAnalyzer fileTypeAnalyzer) throws IOException, InterruptedException {
        String folderPath = args[0];
        String dbPath = args[1];
        List<FileResult> results = fileTypeAnalyzer.analyzeFilesAsyncWithDb(folderPath, dbPath);
        results.forEach((it) -> System.out.printf("%s: %s%n", it.getFileName(), it.getFileType()));
    }

    private static void processFilesAsync(String[] args, FileTypeAnalyzer fileTypeAnalyzer) throws InterruptedException {
        String folderPath = args[0];
        String pattern = args[1];
        String resultString = args[2];
        List<FileResult> results = fileTypeAnalyzer.analyzeFilesAsync(folderPath, pattern, resultString);
        results.forEach((it) -> System.out.printf("%s: %s%n", it.getFileName(), it.getFileType()));
    }

    private static void processFile(String[] args, FileTypeAnalyzer fileTypeAnalyzer) throws IOException {
        long start = System.nanoTime();
        String algorithm = args[0];
        String filePath = args[1];
        String pattern = args[2];
        String resultString = args[3];
        String result = fileTypeAnalyzer.analyzeFile(algorithm, filePath, pattern, resultString);
        System.out.println(result);
        long end = System.nanoTime();
        double seconds = (double) (end - start) / 1_000_000_000;
        System.out.printf("It took %.3f seconds", seconds);
    }
}
