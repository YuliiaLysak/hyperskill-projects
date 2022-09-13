package edu.lysak.analyzer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

public class FileTypeAnalyzer {

    public String analyzeFile(String algorithm, String filePath, String pattern, String result) throws IOException {
        return switch (Algorithm.valueFrom(algorithm)) {
            case NAIVE -> analyzeNaive(filePath, pattern, result);
            case KMP -> analyzeKMP(filePath, pattern, result);
//            case KMP -> analyzeUsingCollections(filePath, pattern, result);
            case UNKNOWN_ALGORITHM -> FileType.UNKNOWN_FILE_TYPE.getResultString();
        };
    }

    public List<FileResult> analyzeFilesAsync(String folderPath, String pattern, String resultString) throws InterruptedException {
        File folder = new File(folderPath);
        File[] files = folder.listFiles();
        List<Analyzer> analyzers = Arrays.stream(Objects.requireNonNull(files))
                .map(file -> new Analyzer(file, pattern, resultString))
                .toList();

        ExecutorService executor = Executors.newFixedThreadPool(10);
        List<Future<FileResult>> futures = executor.invokeAll(analyzers);
        executor.shutdown();

        return futures.stream()
                .map(future -> {
                    try {
                        return future.get();
                    } catch (Exception e) {
                        e.printStackTrace();
                        return new FileResult();
                    }
                })
                .toList();
    }

    private String analyzeKMP(String filePath, String pattern, String result) {
        try {
            String target = new String(Files.readAllBytes(Path.of(filePath)));
            if (findPatternInText(pattern, target) < 0) {
                return FileType.UNKNOWN_FILE_TYPE.getResultString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private int findPatternInText(String pattern, String text) {
        int index = -1;
        int patternLength = pattern.length();
        int textLength = text.length();
        char[] textCharArray = text.toCharArray();
        char[] patternArray = pattern.toCharArray();

        int[] suffixArray = generateSuffixArray(patternLength, pattern);

        int i = 0;
        boolean isMatched = true;
        int j = 0;
        while (j < patternLength && i < textLength) {
            if (textCharArray[i] == patternArray[j]) {
                j = j + 1;
                i = i + 1;
            } else {
                int newIndex = i - j + 1;
                isMatched = false;
                j = j - 1;
                if (j < 0) {
                    j = 0;
                }
                j = suffixArray[j];
                if (j == 0) {
                    i = newIndex;
                } else {
                    i = i + 1;
                }
            }

            if (j == patternLength) {
                isMatched = true;
            }

        }
        if (isMatched) {
            index = i - patternLength;
        }

        return index;
    }

    private static int[] generateSuffixArray(int patternLength, String pattern) {
        int[] suffixArray = new int[patternLength];
        char[] patternArray = pattern.toCharArray();
        int i = 1;
        while (i < patternLength) {
            for (int j = 0; j < patternLength; j++) {
                if ((i < patternLength) && (patternArray[i] == patternArray[j])) {
                    suffixArray[i] = j + 1;
                    i = i + 1;
                } else {
                    i = i + 1;
                    break;
                }

                if (i == patternLength - 1) {
                    break;
                }
            }

        }

        return suffixArray;
    }

    private String analyzeNaive(String filePath, String pattern, String result) throws IOException {
        System.out.println("Using Naive");
        String text = new String(Files.readAllBytes(Path.of(filePath)));
        if (naiveSearch(text, pattern)) {
            return result;
        } else {
            return FileType.UNKNOWN_FILE_TYPE.getResultString();
        }
    }

    private boolean naiveSearch(String text, String pattern) {
        int textLength = text.length();
        int patternLength = pattern.length();
        for (int i = 0; i < textLength - patternLength + 1; i++) {
            for (int j = 0; j < patternLength; j++) {
                if (!(text.charAt(i + j) == pattern.charAt(j))) {
                    break;
                } else if (j == patternLength - 1) {
                    return true;
                }
            }
        }
        return false;
    }

    private String analyzeUsingCollections(String filePath, String pattern, String result) throws IOException {
        byte[] fileByteArray = Files.readAllBytes(Path.of(filePath));
        byte[] patternByteArray = pattern.getBytes();
        List<Byte> fileByteList = convertToList(fileByteArray);
        List<Byte> patternByteList = convertToList(patternByteArray);
        if (Collections.indexOfSubList(fileByteList, patternByteList) < 0) {
            return FileType.UNKNOWN_FILE_TYPE.getResultString();
        } else {
            return result;
        }
    }

    private List<Byte> convertToList(byte[] byteArray) {
        return IntStream.range(0, byteArray.length)
                .mapToObj(i -> byteArray[i])
                .toList();
    }

    class Analyzer implements Callable<FileResult> {
        private final File file;
        private final String pattern;
        private final String resultString;

        Analyzer(File file, String pattern, String resultString) {
            this.file = file;
            this.pattern = pattern;
            this.resultString = resultString;
        }

        @Override
        public FileResult call() {
            String output = analyzeKMP(file.getPath(), pattern, resultString);
            return new FileResult(file.getName(), output);
        }
    }
}
